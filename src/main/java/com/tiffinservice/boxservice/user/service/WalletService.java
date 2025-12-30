package com.tiffinservice.boxservice.user.service;

import java.math.BigDecimal;

public interface WalletService {

    void credit(Long userId, BigDecimal amount, String reference);

    void debit(Long userId, BigDecimal amount, String reference);

    BigDecimal getBalance(Long userId);
}
