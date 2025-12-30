package com.tiffinservice.boxservice.user.controller;

import com.tiffinservice.boxservice.user.walletdto.WalletResponseDTO;
import com.tiffinservice.boxservice.user.walletdto.WalletTopUpRequestDTO;
import com.tiffinservice.boxservice.user.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user/wallet")
@RequiredArgsConstructor
public class WalletController {

    private final WalletService walletService;

    @PostMapping("/{userId}/topup")
    public ResponseEntity<String> topUpWallet(
            @PathVariable Long userId,
            @RequestBody WalletTopUpRequestDTO request) {
        walletService.credit(userId, request.getAmount(), "WALLET_TOPUP");
        return ResponseEntity.ok("Wallet topped up successfully");
    }

    @GetMapping("/{userId}")
    public ResponseEntity<WalletResponseDTO> getBalance(@PathVariable Long userId) {
        return ResponseEntity.ok(
                WalletResponseDTO.builder()
                        .userId(userId)
                        .balance(walletService.getBalance(userId))
                        .build()
        );
    }
}
