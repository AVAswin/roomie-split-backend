package com.roomiesplit.auth.controller;

import com.roomiesplit.auth.dto.*;
import com.roomiesplit.auth.service.AuthService;
import com.roomiesplit.common.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ApiResponse<Void> register(
            @RequestBody @Valid RegisterRequest request
    ) {

        authService.register(request);

        return new ApiResponse<>(
                true,
                "User registered successfully",
                null
        );
    }

    @PostMapping("/login")
    public ApiResponse<AuthResponse> login(
            @RequestBody LoginRequest request
    ) {

        return new ApiResponse<>(
                true,
                "Login successful",
                authService.login(request)
        );
    }

    @PostMapping("/logout")
    public ApiResponse<Void> logout(
            Authentication authentication
    ) {

        authService.logout(
                authentication.getName()
        );

        return new ApiResponse<>(
                true,
                "Logged out successfully",
                null
        );
    }
}