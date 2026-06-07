package com.roomiesplit.auth.service;

import com.roomiesplit.auth.dto.AuthResponse;
import com.roomiesplit.auth.dto.LoginRequest;
import com.roomiesplit.auth.dto.RegisterRequest;
import com.roomiesplit.auth.repository.RefreshTokenRepository;
import com.roomiesplit.common.exception.UserAlreadyExistsException;
import com.roomiesplit.common.security.JwtService;
import com.roomiesplit.user.entity.Role;
import com.roomiesplit.user.entity.User;
import com.roomiesplit.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final RefreshTokenRepository refreshTokenRepository;

    public void register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.email())) {
            throw new UserAlreadyExistsException(
                    "Email already registered"
            );
        }

        User user = User.builder()
                .name(request.name())
                .email(request.email())
                .password(
                        passwordEncoder.encode(
                                request.password()
                        )
                )
                .role(Role.USER)
                .createdAt(LocalDateTime.now())
                .build();

        userRepository.save(user);
    }

    public AuthResponse login(
            LoginRequest request
    ) {

        User user = userRepository
                .findByEmail(request.email())
                .orElseThrow(
                        () -> new RuntimeException(
                                "Invalid credentials"
                        )
                );

        if (!passwordEncoder.matches(
                request.password(),
                user.getPassword()
        )) {

            throw new RuntimeException(
                    "Invalid credentials"
            );
        }

        String accessToken =
                jwtService.generateToken(user);

        String refreshToken =
                refreshTokenService
                        .createRefreshToken(user)
                        .getToken();

        return new AuthResponse(
                accessToken,
                refreshToken
        );
    }

    @Transactional
    public void logout(
            String email
    ) {

        User user = userRepository
                .findByEmail(email)
                .orElseThrow(
                        () -> new RuntimeException(
                                "User not found"
                        )
                );


        refreshTokenRepository
                .deleteByUser(user);
    }
}