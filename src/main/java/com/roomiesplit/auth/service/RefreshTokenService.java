package com.roomiesplit.auth.service;

import com.roomiesplit.auth.entity.RefreshToken;
import com.roomiesplit.auth.repository.RefreshTokenRepository;
import com.roomiesplit.common.security.JwtService;
import com.roomiesplit.user.entity.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtService jwtService;

    public RefreshToken createRefreshToken(User user) {

        refreshTokenRepository
                .findByUser(user)
                .ifPresent(existing ->
                        refreshTokenRepository.delete(existing)
                );

        String token =
                jwtService.generateRefreshToken(user);

        RefreshToken refreshToken =
                RefreshToken.builder()
                        .token(token)
                        .user(user)
                        .expiryDate(
                                LocalDateTime.now().plusDays(7)
                        )
                        .build();

        return refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken validateRefreshToken(
            String token
    ) {

        RefreshToken refreshToken =
                refreshTokenRepository
                        .findByToken(token)
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Refresh token not found"
                                )
                        );

        if (refreshToken.getExpiryDate()
                .isBefore(LocalDateTime.now())) {

            refreshTokenRepository.delete(refreshToken);

            throw new RuntimeException(
                    "Refresh token expired"
            );
        }

        return refreshToken;
    }

//    public void deleteRefreshToken(
//            User user
//    ) {
//
//        refreshTokenRepository
//                .deleteByUser(user);
//    }
}