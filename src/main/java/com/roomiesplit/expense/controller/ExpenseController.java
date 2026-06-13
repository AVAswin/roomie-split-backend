package com.roomiesplit.expense.controller;

import com.roomiesplit.common.response.ApiResponse;
import com.roomiesplit.expense.dto.*;
import com.roomiesplit.expense.service.ExpenseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/expenses")
@RequiredArgsConstructor
public class ExpenseController {

    private final ExpenseService expenseService;

    @PostMapping
    public ApiResponse<CreateExpenseResponse>
    createExpense(
            @Valid
            @RequestBody
            CreateExpenseRequest request,

            Authentication authentication
    ) {

        return new ApiResponse<>(
                true,
                "Expense created successfully",
                expenseService.createExpense(
                        authentication.getName(),
                        request
                )
        );
    }

    @GetMapping
    public ApiResponse<List<ExpenseResponse>>
    getExpenses(
            Authentication authentication
    ) {

        return new ApiResponse<>(
                true,
                "Expenses fetched successfully",
                expenseService.getExpenses(
                        authentication.getName()
                )
        );
    }

    @GetMapping("/my-dues")
    public ApiResponse<List<MyDueResponse>>
    getMyDues(
            Authentication authentication
    ) {

        return new ApiResponse<>(
                true,
                "Dues fetched successfully",
                expenseService.getMyDues(
                        authentication.getName()
                )
        );
    }

    @PostMapping("/{expenseId}/settle")
    public ApiResponse<Void> settleExpense(
            @PathVariable
            Long expenseId,

            Authentication authentication
    ) {

        expenseService.settleExpense(
                expenseId,
                authentication.getName()
        );

        return new ApiResponse<>(
                true,
                "Expense settled successfully",
                null
        );
    }

    @GetMapping("/my-paid")
    public ApiResponse<List<MyPaidResponse>>
    getMyPaidExpenses(
            Authentication authentication
    ) {

        return new ApiResponse<>(
                true,
                "Paid expenses fetched successfully",
                expenseService.getMyPaidExpenses(
                        authentication.getName()
                )
        );
    }
}