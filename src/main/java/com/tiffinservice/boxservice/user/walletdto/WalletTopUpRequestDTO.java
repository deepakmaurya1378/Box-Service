package com.tiffinservice.boxservice.user.walletdto;

import lombok.*;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WalletTopUpRequestDTO {
    private BigDecimal amount;
}
