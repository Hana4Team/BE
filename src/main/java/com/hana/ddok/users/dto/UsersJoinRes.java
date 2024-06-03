package com.hana.ddok.users.dto;

import com.hana.ddok.account.domain.Account;
import com.hana.ddok.products.domain.Products;
import com.hana.ddok.users.domain.Users;

public record UsersJoinRes(
        Boolean success,
        Long usersId,
        String phoneNumber
) {
}
