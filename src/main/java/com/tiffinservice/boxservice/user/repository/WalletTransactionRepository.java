package com.tiffinservice.boxservice.user.repository;

import com.tiffinservice.boxservice.user.entity.WalletTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletTransactionRepository
        extends JpaRepository<WalletTransaction, Long> {
}
