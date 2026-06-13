package com.roomiesplit.expense.dto;

import java.math.BigDecimal;

public record CreateExpenseResponse(

        Long expenseId,

        Integer splitCount,

        BigDecimal individualShare

) {}