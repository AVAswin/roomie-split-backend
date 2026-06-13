package com.roomiesplit.expense.service;

import com.roomiesplit.expense.dto.*;
import com.roomiesplit.expense.entity.Expense;
import com.roomiesplit.expense.entity.ExpenseParticipant;
import com.roomiesplit.expense.entity.ExpenseStatus;
import com.roomiesplit.expense.repository.ExpenseParticipantRepository;
import com.roomiesplit.expense.repository.ExpenseRepository;
import com.roomiesplit.house.entity.House;
import com.roomiesplit.house.entity.HouseMember;
import com.roomiesplit.house.repository.HouseMemberRepository;
import com.roomiesplit.notification.service.NotificationService;
import com.roomiesplit.user.entity.User;
import com.roomiesplit.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final UserRepository userRepository;
    private final HouseMemberRepository houseMemberRepository;
    private final ExpenseRepository expenseRepository;
    private final ExpenseParticipantRepository expenseParticipantRepository;
    private final NotificationService notificationService;

    @Transactional
    public CreateExpenseResponse createExpense(String email, CreateExpenseRequest request) {
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
                                        "User does not belong to a house"
                                )
                        );
        House house =
                membership.getHouse();

        List<HouseMember> members =
                houseMemberRepository
                        .findByHouse(house);

        BigDecimal shareAmount =
                request.amount()
                        .divide(
                                BigDecimal.valueOf(
                                        members.size()
                                ),
                                2,
                                RoundingMode.HALF_UP
                        );

        Expense expense =
                Expense.builder()
                        .title(request.title())
                        .description(
                                request.description()
                        )
                        .amount(
                                request.amount()
                        )
                        .paidBy(user)
                        .house(house)
                        .createdAt(
                                LocalDateTime.now()
                        )
                        .build();
        expense = expenseRepository.save(expense);

        for (HouseMember member : members) {

            if(member.getUser().getId()
                    .equals(user.getId())) {
                continue;
            }

            ExpenseParticipant participant =
                    ExpenseParticipant.builder()
                            .expense(expense)
                            .user(member.getUser())
                            .shareAmount(shareAmount)
                            .status(ExpenseStatus.PENDING)
                            .build();

            expenseParticipantRepository.save(participant);

            notificationService.createNotification(

                    member.getUser(),

                    "New Expense Added",

                    user.getName()
                            + " added "
                            + request.title()
                            + ". You owe ₹"
                            + shareAmount
            );
        }

        return new CreateExpenseResponse(
                expense.getId(),
                members.size(),
                shareAmount
        );
    }

    public List<ExpenseResponse> getExpenses(String email) {
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

        return expenseRepository
                .findByHouse(house)
                .stream()
                .map(expense ->
                        new ExpenseResponse(
                                expense.getId(),
                                expense.getTitle(),
                                expense.getAmount(),
                                expense.getPaidBy().getName(),
                                expense.getCreatedAt()
                        )
                )
                .toList();
    }

    public List<MyDueResponse> getMyDues(
            String email
    ) {

        User user = userRepository
                .findByEmail(email)
                .orElseThrow(
                        () -> new RuntimeException(
                                "User not found"
                        )
                );

        return expenseParticipantRepository
                .findByUser(user)
                .stream()

                .filter(participant ->
                        participant.getStatus()
                                == ExpenseStatus.PENDING
                )

                .map(participant ->
                        new MyDueResponse(

                                participant
                                        .getExpense()
                                        .getTitle(),

                                participant
                                        .getExpense()
                                        .getPaidBy()
                                        .getName(),

                                participant
                                        .getShareAmount()
                        )
                )
                .toList();
    }

    @Transactional
    public void settleExpense(
            Long expenseId,
            String email
    ) {

        User user = userRepository
                .findByEmail(email)
                .orElseThrow(
                        () -> new RuntimeException(
                                "User not found"
                        )
                );

        ExpenseParticipant participant =
                expenseParticipantRepository
                        .findByExpenseIdAndUser(
                                expenseId,
                                user
                        )
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Due not found"
                                )
                        );

        if (participant.getStatus()
                == ExpenseStatus.PAID) {

            throw new RuntimeException(
                    "Expense already settled"
            );
        }

        participant.setStatus(
                ExpenseStatus.PAID
        );

        expenseParticipantRepository
                .save(participant);
    }

    public List<MyPaidResponse> getMyPaidExpenses(
            String email
    ) {

        User user = userRepository
                .findByEmail(email)
                .orElseThrow(
                        () -> new RuntimeException(
                                "User not found"
                        )
                );

        return expenseParticipantRepository
                .findByUser(user)
                .stream()

                .filter(participant ->
                        participant.getStatus()
                                == ExpenseStatus.PAID
                )

                .map(participant ->
                        new MyPaidResponse(

                                participant
                                        .getExpense()
                                        .getId(),

                                participant
                                        .getExpense()
                                        .getTitle(),

                                participant
                                        .getExpense()
                                        .getPaidBy()
                                        .getName(),

                                participant
                                        .getShareAmount()
                        )
                )
                .toList();
    }
}