package com.roomiesplit.expense.dto;

import java.math.BigDecimal;

public record MyDueResponse(
        Long expenseId,

        String expenseTitle,

        String owedTo,

        BigDecimal amount

) {
}