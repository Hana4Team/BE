package com.hana.ddok.budget.dto;

import com.hana.ddok.account.domain.Account;
import com.hana.ddok.products.domain.Products;
import com.hana.ddok.users.domain.Users;

import java.time.LocalDate;

public record BudgetUpdateReq(
        Integer sum
) {

}
