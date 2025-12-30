package com.tiffinservice.boxservice.user.walletdto;

import lombok.*;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WalletResponseDTO {
    private Long userId;
    private BigDecimal balance;
}
