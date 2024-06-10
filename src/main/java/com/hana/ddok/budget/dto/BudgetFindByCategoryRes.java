package com.hana.ddok.budget.dto;

import com.hana.ddok.budget.domain.Budget;

public record BudgetFindByCategoryRes(
        Long sum,
        Long shopping,
        Long food,
        Long traffic,
        Long hospital,
        Long fee,
        Long education,
        Long leisure,
        Long society,
        Long daily,
        Long overseas

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
