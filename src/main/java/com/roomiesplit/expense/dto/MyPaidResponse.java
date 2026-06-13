package com.roomiesplit.expense.dto;

import java.math.BigDecimal;

public record MyPaidResponse(

        Long expenseId,

        String expenseTitle,

        String paidTo,

        BigDecimal amount

) {
}