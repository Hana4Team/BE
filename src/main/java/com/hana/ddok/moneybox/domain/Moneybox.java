package com.hana.ddok.account.domain;

import com.hana.ddok.common.domain.BaseEntity;
import com.hana.ddok.products.domain.Account;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

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

    @Column(name = "expense_total", nullable = false)
    private Long expenseTotal;

    @Column(name = "is_charged", nullable = false)
    private Boolean isCharged;

    @OneToOne
    @JoinColumn(name = "account_id")
    private Account account;
}