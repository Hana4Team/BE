package com.hana.ddok.budget.dto;

import com.hana.ddok.budget.domain.Budget;

public record BudgetFindByCategoryRes(
        Integer sum,
        Integer shopping,
        Integer food,
        Integer traffic,
        Integer hospital,
        Integer fee,
        Integer education,
        Integer leisure,
        Integer society,
        Integer daily,
        Integer overseas

) {
    public BudgetFindByCategoryRes(Budget budget) {
        this(
                budget.getSum(),
                budget.getShopping(),
                budget.getFood(),
                budget.getTraffic(),
                budget.getHospital(),
                budget.getFee(),
                budget.getEducation(),
                budget.getLeisure(),
                budget.getSociety(),
                budget.getDaily(),
                budget.getOverseas()
        );
    }
}
