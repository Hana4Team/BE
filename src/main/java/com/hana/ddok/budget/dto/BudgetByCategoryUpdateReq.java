package com.hana.ddok.budget.dto;

public record BudgetByCategoryUpdateReq(
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
}
