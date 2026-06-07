package com.roomiesplit.auth.dto;

public record LoginRequest(
        String email,
        String password
) {
}
