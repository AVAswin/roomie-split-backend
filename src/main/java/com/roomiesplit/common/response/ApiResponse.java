package com.roomiesplit.common.response;

public record ApiResponse<T>(
        boolean success,
        String message,
        T data
) {
}
