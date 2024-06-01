package com.hana.ddok.account.dto;

import com.hana.ddok.account.domain.Account;
import com.hana.ddok.account.domain.Moneybox;

public record MoneyboxFindAllRes(
        String accountNumber,
        Long parkingBalance,
        Long expenseBalance,
        Long savingBalance
) {
    public MoneyboxFindAllRes(Account account, Moneybox moneybox) {
        this(
                account.getAccountNumber(),
                moneybox.getParkingBalance(),
                moneybox.getExpenseBalance(),
                moneybox.getSavingBalance()
        );
    }
}
