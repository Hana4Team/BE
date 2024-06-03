package com.hana.ddok.budget.dto;

import com.hana.ddok.account.domain.Account;
import com.hana.ddok.budget.domain.Budget;

public record BudgetUpdateRes(
        Long budgetId
) {
    public BudgetUpdateRes(Budget budget) {
        this(
                budget.getBudgetId()
        );
    }
}
