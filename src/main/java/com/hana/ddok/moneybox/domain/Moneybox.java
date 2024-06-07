package com.hana.ddok.moneybox.domain;

import com.hana.ddok.account.domain.Account;
import com.hana.ddok.account.exception.AccountBalanceInvalid;
import com.hana.ddok.common.domain.BaseEntity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "moneybox")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Moneybox extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "moneybox_id")
    private Long moneyboxId;

    @Column(name = "parking_balance", nullable = false)
    private Long parkingBalance;

    @Column(name = "expense_balance", nullable = false)
    private Long expenseBalance;

    @Column(name = "saving_balance", nullable = false)
    private Long savingBalance;

    @Column(name = "is_charged", nullable = false)
    private Boolean isCharged;

    @OneToOne
    @JoinColumn(name = "account_id")
    private Account account;

    public void updateParkingBalance(Long balance) {
        parkingBalance += balance;
        if (parkingBalance < 0) {
            throw new AccountBalanceInvalid();
        }
    }

    public void updateExpenseBalance(Long balance) {
        expenseBalance += balance;
        if (expenseBalance < 0) {
            throw new AccountBalanceInvalid();
        }
    }

    public void updateSavingBalance(Long balance) {
        savingBalance += balance;
        if (savingBalance < 0) {
            throw new AccountBalanceInvalid();
        }
    }
}