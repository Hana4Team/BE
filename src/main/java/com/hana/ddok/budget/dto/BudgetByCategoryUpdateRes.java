package com.hana.ddok.budget.dto;

import com.hana.ddok.budget.domain.Budget;

public record BudgetByCategoryUpdateRes(
        Long budgetId
) {
    public BudgetByCategoryUpdateRes(Budget budget) {
        this(
                budget.getBudgetId()
        );
    }
}
