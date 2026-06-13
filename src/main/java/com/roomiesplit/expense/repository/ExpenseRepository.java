package com.roomiesplit.expense.repository;

import com.roomiesplit.expense.entity.Expense;
import com.roomiesplit.house.entity.House;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExpenseRepository
        extends JpaRepository<Expense, Long> {

    List<Expense> findByHouse(House house);
}