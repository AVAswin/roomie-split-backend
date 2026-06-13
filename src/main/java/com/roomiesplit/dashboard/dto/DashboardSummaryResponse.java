package com.roomiesplit.dashboard.dto;

import java.math.BigDecimal;

public record DashboardSummaryResponse(

        String houseName,

        Integer memberCount,

        Integer totalExpenses,

        BigDecimal totalExpenseAmount,

        BigDecimal pendingDues,

        Integer paidExpenses

) {
}