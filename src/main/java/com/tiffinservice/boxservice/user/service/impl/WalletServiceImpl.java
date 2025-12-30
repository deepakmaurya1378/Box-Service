package com.tiffinservice.boxservice.user.service.impl;

import com.tiffinservice.boxservice.user.entity.User;
import com.tiffinservice.boxservice.user.entity.Wallet;
import com.tiffinservice.boxservice.user.entity.WalletTransaction;
import com.tiffinservice.boxservice.common.enums.WalletTransactionType;
import com.tiffinservice.boxservice.user.repository.UserRepository;
import com.tiffinservice.boxservice.user.repository.WalletRepository;
import com.tiffinservice.boxservice.user.repository.WalletTransactionRepository;
import com.tiffinservice.boxservice.user.service.WalletService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepository;
    private final WalletTransactionRepository transactionRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public void credit(Long userId, BigDecimal amount, String reference) {

        Wallet wallet = getOrCreateWallet(userId);

        wallet.setBalance(wallet.getBalance().add(amount));

        transactionRepository.save(
                WalletTransaction.builder()
                        .wallet(wallet)
                        .amount(amount)
                        .type(WalletTransactionType.CREDIT)
                        .reference(reference)
                        .build()
        );
    }

    @Override
    @Transactional
    public void debit(Long userId, BigDecimal amount, String reference) {

        Wallet wallet = getOrCreateWallet(userId);

        if (wallet.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient wallet balance");
        }

        wallet.setBalance(wallet.getBalance().subtract(amount));

        transactionRepository.save(
                WalletTransaction.builder()
                        .wallet(wallet)
                        .amount(amount)
                        .type(WalletTransactionType.DEBIT)
                        .reference(reference)
                        .build()
        );
    }

    @Override
    public BigDecimal getBalance(Long userId) {
        return getOrCreateWallet(userId).getBalance();
    }

    private Wallet getOrCreateWallet(Long userId) {
        return walletRepository.findByUserId(userId)
                .orElseGet(() -> {
                    User user = userRepository.findById(userId)
                            .orElseThrow(() ->
                                    new RuntimeException("User not found with id: " + userId)
                            );
                    Wallet wallet = Wallet.builder()
                            .user(user)
                            .balance(BigDecimal.ZERO)
                            .build();
                    return walletRepository.save(wallet);
                });
    }
}
