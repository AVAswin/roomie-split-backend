package com.roomiesplit.auth.controller;

import com.roomiesplit.auth.dto.RegisterRequest;
import com.roomiesplit.auth.service.AuthService;
import com.roomiesplit.common.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
}