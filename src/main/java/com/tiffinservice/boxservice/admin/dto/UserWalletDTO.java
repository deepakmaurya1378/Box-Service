package com.tiffinservice.boxservice.admin.dto;

import com.tiffinservice.boxservice.user.entity.Wallet;
import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserWalletDTO {

    private Long walletId;
    private BigDecimal balance;

    public static UserWalletDTO from(Wallet wallet) {
        return UserWalletDTO.builder()
                .walletId(wallet.getId())
                .balance(wallet.getBalance())
                .build();
    }
}
