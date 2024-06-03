package com.hana.ddok.budget.dto;

import com.hana.ddok.budget.domain.Budget;

public record BudgetFindRes(
        Integer sum
) {
    public BudgetFindRes(Budget budget) {
        this(
                budget.getSum()
        );
    }
}
