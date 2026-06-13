package com.roomiesplit.dashboard.service;

import com.roomiesplit.dashboard.dto.DashboardSummaryResponse;
import com.roomiesplit.expense.entity.Expense;
import com.roomiesplit.expense.entity.ExpenseParticipant;
import com.roomiesplit.expense.entity.ExpenseStatus;
import com.roomiesplit.expense.repository.ExpenseParticipantRepository;
import com.roomiesplit.expense.repository.ExpenseRepository;
import com.roomiesplit.house.entity.House;
import com.roomiesplit.house.entity.HouseMember;
import com.roomiesplit.house.repository.HouseMemberRepository;
import com.roomiesplit.user.entity.User;
import com.roomiesplit.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final UserRepository userRepository;
    private final HouseMemberRepository houseMemberRepository;
    private final ExpenseRepository expenseRepository;
    private final ExpenseParticipantRepository expenseParticipantRepository;

    public DashboardSummaryResponse getSummary(
            String email) {
        User user = userRepository
                .findByEmail(email)
                .orElseThrow(
                        () -> new RuntimeException(
                                "User not found"
                        )
                );

        HouseMember membership =
                houseMemberRepository
                        .findByUser(user)
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "User not in a house"
                                )
                        );
        House house =
                membership.getHouse();

        int memberCount =
                houseMemberRepository
                        .findByHouse(house)
                        .size();

        List<Expense> expenses =
                expenseRepository
                        .findByHouse(house);

        int totalExpenses =
                expenses.size();

        BigDecimal totalExpenseAmount =
                expenses.stream()
                        .map(Expense::getAmount)
                        .reduce(
                                BigDecimal.ZERO,
                                BigDecimal::add
                        );

        BigDecimal pendingDues =
                expenseParticipantRepository
                        .findByUser(user)
                        .stream()
                        .filter(participant ->
                                participant.getStatus()
                                        == ExpenseStatus.PENDING
                        )
                        .map(ExpenseParticipant::getShareAmount)
                        .reduce(
                                BigDecimal.ZERO,
                                BigDecimal::add
                        );

        int paidExpenses =
                (int) expenseParticipantRepository
                        .findByUser(user)
                        .stream()
                        .filter(participant ->
                                participant.getStatus()
                                        == ExpenseStatus.PAID
                        )
                        .count();

        return new DashboardSummaryResponse(
                house.getName(),
                memberCount,
                totalExpenses,
                totalExpenseAmount,
                pendingDues,
                paidExpenses
        );

    }
}