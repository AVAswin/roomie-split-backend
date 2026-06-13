package com.roomiesplit.expense.repository;

import com.roomiesplit.expense.entity.Expense;
import com.roomiesplit.expense.entity.ExpenseParticipant;
import com.roomiesplit.expense.entity.ExpenseStatus;
import com.roomiesplit.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ExpenseParticipantRepository
        extends JpaRepository<ExpenseParticipant, Long> {

    List<ExpenseParticipant> findByExpense(
            Expense expense
    );

    List<ExpenseParticipant> findByUser(
            User user
    );

    Optional<ExpenseParticipant>
    findByExpenseIdAndUser(
            Long expenseId,
            User user
    );

    List<ExpenseParticipant> findByStatus(
            ExpenseStatus status
    );
}