package com.hana.ddok.account.dto;

import com.hana.ddok.account.domain.Account;

public record AccountFindbyMissionRes(

) {
    public AccountFindbyMissionRes(Account account) {
        this();
        // TODO : 수정해야함
    }
}
