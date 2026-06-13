package com.roomiesplit.expense.dto;

import java.math.BigDecimal;

public record MyDueResponse(

        String expenseTitle,

        String owedTo,

        BigDecimal amount

) {
}