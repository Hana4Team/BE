package com.hana.ddok.budget.dto;

public record BudgetByCategoryUpdateReq(
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
}
