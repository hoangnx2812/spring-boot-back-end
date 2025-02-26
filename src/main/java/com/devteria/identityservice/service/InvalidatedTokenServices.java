package com.devteria.identityservice.service;

import com.devteria.identityservice.repository.InvalidatedTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class InvalidatedTokenServices {

    InvalidatedTokenRepository invalidatedTokenRepository;

    @Scheduled(cron = "0 0 0 * * ?") // Chạy vào 00:00 mỗi ngày
    public void deleteExpiredTokens() {
        invalidatedTokenRepository.deleteByExpiryTimeAfterNow();
    }


}
