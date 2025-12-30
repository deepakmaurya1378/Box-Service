package com.tiffinservice.boxservice.order.service.impl;

import com.tiffinservice.boxservice.order.dto.ManualOrderRequestDTO;
import com.tiffinservice.boxservice.order.dto.OrderResponseDTO;
import com.tiffinservice.boxservice.order.entity.Order;
import com.tiffinservice.boxservice.order.entity.OrderItem;
import com.tiffinservice.boxservice.common.enums.*;
import com.tiffinservice.boxservice.user.entity.Address;
import com.tiffinservice.boxservice.user.entity.User;
import com.tiffinservice.boxservice.user.entity.Wallet;
import com.tiffinservice.boxservice.user.entity.WalletTransaction;
import com.tiffinservice.boxservice.vendor.entity.Vendor;
import com.tiffinservice.boxservice.vendor.entity.VendorMenu;
import com.tiffinservice.boxservice.order.mapper.OrderMapper;
import com.tiffinservice.boxservice.order.repository.OrderItemRepository;
import com.tiffinservice.boxservice.order.repository.OrderRepository;
import com.tiffinservice.boxservice.user.repository.AddressRepository;
import com.tiffinservice.boxservice.user.repository.UserRepository;
import com.tiffinservice.boxservice.user.repository.WalletRepository;
import com.tiffinservice.boxservice.user.repository.WalletTransactionRepository;
import com.tiffinservice.boxservice.vendor.repository.VendorMenuRepository;
import com.tiffinservice.boxservice.vendor.repository.VendorRepository;
import com.tiffinservice.boxservice.order.service.OrderService;
import com.tiffinservice.boxservice.vendor.service.VendorUnavailabilityService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepo;
    private final OrderItemRepository orderItemRepo;
    private final UserRepository userRepo;
    private final VendorRepository vendorRepo;
    private final VendorMenuRepository menuRepo;
    private final AddressRepository addressRepo;
    private final WalletRepository walletRepo;
    private final WalletTransactionRepository walletTxnRepo;
    private final VendorUnavailabilityService vendorUnavailabilityService;

    @Override
    @Transactional
    public OrderResponseDTO createManualOrder(Long userId, ManualOrderRequestDTO dto) {


        User user = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        Vendor vendor = vendorRepo.findById(dto.getVendorId()).orElseThrow(() -> new RuntimeException("Vendor not found"));

        if (vendorUnavailabilityService.isVendorUnavailable(vendor.getId(), LocalDate.now())) {
            throw new RuntimeException("Vendor is not available today. Please choose another vendor.");
        }


        boolean autoOrderExists =
                orderRepo.existsByUser_IdAndOrderDateAndShiftTypeAndOrderType(user.getId(), LocalDate.now(), dto.getShiftType(), OrderType.AUTO);

        if (autoOrderExists) {
            throw new RuntimeException("Auto order already exists for this shift. Manual order is not allowed.");
        }

        Address address = addressRepo.findById(dto.getAddressId()).orElseThrow(() -> new RuntimeException("Address not found"));

        Wallet wallet = walletRepo.findByUserId(user.getId()).orElseThrow(() -> new RuntimeException("Wallet not found for user"));


        Order order = orderRepo.save(
                Order.builder()
                        .user(user)
                        .vendor(vendor)
                        .deliveryAddress(address)
                        .orderType(OrderType.MANUAL)
                        .shiftType(dto.getShiftType())
                        .orderDate(LocalDate.now())
                        .status(OrderStatus.CREATED)
                        .build()
        );

        BigDecimal totalAmount = BigDecimal.ZERO;


        for (ManualOrderRequestDTO.Item item : dto.getItems()) {

            VendorMenu menu = menuRepo.findById(item.getMenuId())
                    .orElseThrow(() ->
                            new RuntimeException("Menu item not found")
                    );

            if (!Boolean.TRUE.equals(menu.getIsAvailable())) {
                throw new RuntimeException(
                        "Menu item not available: " + menu.getMealTitle()
                );
            }

            BigDecimal itemTotal = menu.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));

            totalAmount = totalAmount.add(itemTotal);

            orderItemRepo.save(
                    OrderItem.builder()
                            .order(order)
                            .menu(menu)
                            .itemName(menu.getMealTitle())
                            .price(menu.getPrice())
                            .quantity(item.getQuantity())
                            .itemTotal(itemTotal)
                            .build()
            );
        }

        debitWallet(wallet, totalAmount, order.getId());

        order.setTotalAmount(totalAmount);
        order.setStatus(OrderStatus.CONFIRMED);
        order.setConfirmedAt(LocalDateTime.now());

        Order savedOrder = orderRepo.save(order);
        return OrderMapper.toResponse(savedOrder);
    }

    @Override
    public List<OrderResponseDTO> getUserOrders(Long userId) {

        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return orderRepo.findByUser(user)
                .stream()
                .map(OrderMapper::toResponse)
                .toList();
    }

    private void debitWallet(Wallet wallet, BigDecimal amount, Long orderId) {

        if (wallet.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient wallet balance");
        }

        wallet.setBalance(wallet.getBalance().subtract(amount));
        walletRepo.save(wallet);

        walletTxnRepo.save(
                WalletTransaction.builder()
                        .wallet(wallet)
                        .amount(amount.negate())
                        .type(WalletTransactionType.DEBIT)
                        .reference("ORDER_" + orderId)
                        .createdAt(LocalDateTime.now())
                        .build()
        );
    }
}
