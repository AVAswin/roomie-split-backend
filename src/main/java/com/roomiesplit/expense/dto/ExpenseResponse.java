package com.roomiesplit.expense.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ExpenseResponse(
        Long id,
        String title,
        BigDecimal amount,
        String paidBy,
        LocalDateTime createdAt
) {
}