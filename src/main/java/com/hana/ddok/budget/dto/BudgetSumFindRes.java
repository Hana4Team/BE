package com.hana.ddok.budget.dto;

import com.hana.ddok.budget.domain.Budget;

public record BudgetSumFindRes(
        Long sum
) {
    public BudgetSumFindRes(Budget budget) {
        this(
                budget.getSum()
        );
    }
}
