package com.roomiesplit.auth.service;

import com.roomiesplit.auth.dto.RegisterRequest;
import com.roomiesplit.common.exception.UserAlreadyExistsException;
import com.roomiesplit.user.entity.User;
import com.roomiesplit.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

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
                .createdAt(LocalDateTime.now())
                .build();

        userRepository.save(user);
    }
}