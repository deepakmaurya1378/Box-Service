package com.tiffinservice.boxservice.order.service.impl;

import com.tiffinservice.boxservice.common.exception.BadRequestException;
import com.tiffinservice.boxservice.common.exception.ConflictException;
import com.tiffinservice.boxservice.common.exception.ResourceNotFoundException;
import com.tiffinservice.boxservice.common.exception.VendorLeaveNotAllowedException;
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

        User user = userRepo.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User", "id", userId)
                );

        Vendor vendor = vendorRepo.findById(dto.getVendorId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Vendor", "id", dto.getVendorId())
                );

        if (!Boolean.TRUE.equals(vendor.getIsOpen())) {
            throw new ConflictException("Vendor is currently closed. Please try later.");
        }

        if (vendor.getStatus() != VendorStatus.APPROVED) {
            throw new ConflictException("Vendor is not approved. Orders are not allowed.");
        }

        if (vendorUnavailabilityService.isVendorUnavailable(vendor.getId(), LocalDate.now())) {
            throw new VendorLeaveNotAllowedException(
                    "Vendor is not available today. Please choose another vendor."
            );
        }

        Address address = addressRepo.findById(dto.getAddressId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Address", "id", dto.getAddressId())
                );

        Wallet wallet = walletRepo.findByUserId(user.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Wallet", "userId", user.getId())
                );

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
                    .orElseThrow(() -> new ResourceNotFoundException("VendorMenu", "id", item.getMenuId()));

            if (!Boolean.TRUE.equals(menu.getIsAvailable())
                    || menu.getAvailableQuantity() == null
                    || menu.getAvailableQuantity() < item.getQuantity()) {

                throw new ConflictException("Meal is not available right now");
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

            menu.setAvailableQuantity(menu.getAvailableQuantity() - item.getQuantity());

            if (menu.getAvailableQuantity() == 0) {menu.setIsAvailable(false);}

            menuRepo.save(menu);
        }

        debitWallet(wallet, totalAmount, order.getId());
        order.setTotalAmount(totalAmount);
        order.setStatus(OrderStatus.CONFIRMED);
        order.setConfirmedAt(LocalDateTime.now());
        return OrderMapper.toResponse(orderRepo.save(order));
    }

    @Override
    public List<OrderResponseDTO> getUserOrders(Long userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        return orderRepo.findByUser(user)
                .stream()
                .map(OrderMapper::toResponse)
                .toList();
    }

    private void debitWallet(Wallet wallet, BigDecimal amount, Long orderId) {

        if (wallet.getBalance().compareTo(amount) < 0) {
            throw new BadRequestException("Insufficient wallet balance");
        }

        wallet.setBalance(wallet.getBalance().subtract(amount));
        walletRepo.save(wallet);
        walletTxnRepo.save(WalletTransaction.builder()
                        .wallet(wallet)
                        .amount(amount.negate())
                        .type(WalletTransactionType.DEBIT)
                        .reference("ORDER_" + orderId)
                        .createdAt(LocalDateTime.now())
                        .build()
        );
    }
}
