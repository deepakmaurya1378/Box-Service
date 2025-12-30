package com.tiffinservice.boxservice.order.service.impl;

import com.tiffinservice.boxservice.common.enums.*;
import com.tiffinservice.boxservice.common.utils.GeoUtils;
import com.tiffinservice.boxservice.order.entity.Order;
import com.tiffinservice.boxservice.order.entity.OrderItem;
import com.tiffinservice.boxservice.order.repository.OrderItemRepository;
import com.tiffinservice.boxservice.order.repository.OrderRepository;
import com.tiffinservice.boxservice.order.service.AutoOrderService;
import com.tiffinservice.boxservice.user.entity.Address;
import com.tiffinservice.boxservice.user.entity.User;
import com.tiffinservice.boxservice.user.entity.Wallet;
import com.tiffinservice.boxservice.user.entity.WalletTransaction;
import com.tiffinservice.boxservice.user.repository.AddressRepository;
import com.tiffinservice.boxservice.user.repository.WalletRepository;
import com.tiffinservice.boxservice.user.repository.WalletTransactionRepository;
import com.tiffinservice.boxservice.vendor.entity.Vendor;
import com.tiffinservice.boxservice.vendor.entity.VendorMenu;
import com.tiffinservice.boxservice.vendor.repository.VendorMenuRepository;
import com.tiffinservice.boxservice.vendor.service.VendorUnavailabilityService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AutoOrderServiceImpl implements AutoOrderService {

    private final OrderRepository orderRepo;
    private final OrderItemRepository orderItemRepo;
    private final VendorMenuRepository menuRepo;
    private final WalletRepository walletRepo;
    private final WalletTransactionRepository walletTxnRepo;
    private final AddressRepository addressRepo;
    private final VendorUnavailabilityService vendorUnavailabilityService;

    @Override
    @Transactional
    public void createAutoOrder(
            User user,
            Vendor vendor,
            ShiftType shiftType,
            LocalDate orderDate,
            LocalDateTime scheduledAt
    ) {

        // ===================== 1️⃣ Vendor Unavailability =====================
        if (vendorUnavailabilityService.isVendorUnavailable(vendor.getId(), orderDate)) {
            log.info(
                    "Auto order skipped: vendor {} unavailable on {}",
                    vendor.getId(), orderDate
            );
            return;
        }

        // ===================== 2️⃣ Prevent duplicate AUTO =====================
        if (orderRepo.existsByUser_IdAndOrderDateAndShiftTypeAndOrderType(
                user.getId(), orderDate, shiftType, OrderType.AUTO
        )) {
            return;
        }

        // ===================== 3️⃣ Prevent AUTO if MANUAL exists =====================
        if (orderRepo.existsByUser_IdAndOrderDateAndShiftTypeAndOrderType(
                user.getId(), orderDate, shiftType, OrderType.MANUAL
        )) {
            return;
        }

        // ===================== 4️⃣ SHIFT-WISE ADDRESS =====================
        Address address = addressRepo
                .findByUser_IdAndAddressType(
                        user.getId(),
                        AddressType.valueOf(shiftType.name())
                )
                .orElse(null);

        if (address == null) {
            log.warn(
                    "Auto order skipped: no {} address for user {}",
                    shiftType, user.getId()
            );
            return;
        }

        // ===================== 5️⃣ DISTANCE CHECK =====================
        double distanceKm = GeoUtils.distanceKm(
                address.getLatitude().doubleValue(),
                address.getLongitude().doubleValue(),
                vendor.getLatitude().doubleValue(),
                vendor.getLongitude().doubleValue()
        );

        if (BigDecimal.valueOf(distanceKm)
                .compareTo(vendor.getDeliveryRadiusKm()) > 0) {

            log.warn(
                    "Auto order skipped: vendor {} cannot deliver to {} address ({} km)",
                    vendor.getId(), shiftType, distanceKm
            );
            return;
        }

        // ===================== 6️⃣ MENU SELECTION =====================
        MealType mealType = mapShiftToMeal(shiftType);

        DayOfWeek dayOfWeek =
                DayOfWeek.valueOf(orderDate.getDayOfWeek().name());

        VendorMenu menu = findMenuForAutoOrder(
                vendor.getId(),
                mealType,
                dayOfWeek
        );

        if (menu == null) {
            log.info(
                    "Auto order skipped: no menu for vendor {}, meal {}, day {}",
                    vendor.getId(), mealType, dayOfWeek
            );
            return;
        }

        // ===================== 7️⃣ WALLET CHECK =====================
        Wallet wallet = walletRepo.findByUserId(user.getId())
                .orElseThrow(() ->
                        new RuntimeException("Wallet not found for user " + user.getId())
                );

        BigDecimal amount = menu.getPrice();

        if (wallet.getBalance().compareTo(amount) < 0) {
            log.info(
                    "Auto order skipped: insufficient wallet balance for user {}",
                    user.getId()
            );
            return;
        }

        // ===================== 8️⃣ CREATE ORDER =====================
        Order order = orderRepo.save(
                Order.builder()
                        .user(user)
                        .vendor(vendor)
                        .deliveryAddress(address)
                        .orderType(OrderType.AUTO)
                        .shiftType(shiftType)
                        .orderDate(orderDate)
                        .status(OrderStatus.CONFIRMED)
                        .autoGenerated(true)
                        .scheduledAt(scheduledAt)
                        .confirmedAt(LocalDateTime.now())
                        .totalAmount(amount)
                        .build()
        );

        orderItemRepo.save(
                OrderItem.builder()
                        .order(order)
                        .menu(menu)
                        .itemName(menu.getMealTitle())
                        .price(menu.getPrice())
                        .quantity(1)
                        .itemTotal(menu.getPrice())
                        .build()
        );

        debitWallet(wallet, amount, order.getId());

        log.info(
                "Auto order created successfully for user {} on {} ({})",
                user.getId(), orderDate, shiftType
        );
    }

    private VendorMenu findMenuForAutoOrder(Long vendorId, MealType mealType, DayOfWeek dayOfWeek) {
        List<VendorMenu> dayMealMenus = menuRepo.findByVendor_IdAndMealTypeAndDayOfWeek(vendorId, mealType, dayOfWeek);
        if (!dayMealMenus.isEmpty()) { return dayMealMenus.get(0) ;}
        List<VendorMenu> dailyMenus = menuRepo.findByVendor_IdAndIsDailyTrue(vendorId);
        return dailyMenus.isEmpty() ? null : dailyMenus.get(0);
    }

    private void debitWallet(Wallet wallet, BigDecimal amount, Long orderId) {

        wallet.setBalance(wallet.getBalance().subtract(amount));
        walletRepo.save(wallet);

        walletTxnRepo.save(WalletTransaction.builder()
                        .wallet(wallet)
                        .amount(amount.negate())
                        .type(WalletTransactionType.DEBIT)
                        .reference("AUTO_ORDER_" + orderId)
                        .createdAt(LocalDateTime.now())
                        .build()
        );
    }

    private MealType mapShiftToMeal(ShiftType shiftType) {
        return switch (shiftType) {
            case BREAKFAST -> MealType.BREAKFAST;
            case LUNCH -> MealType.LUNCH;
            case DINNER -> MealType.DINNER;
        };
    }
}
