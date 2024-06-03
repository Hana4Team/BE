package com.hana.ddok.depositsaving.domain;

import com.hana.ddok.account.domain.Account;
import com.hana.ddok.common.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Entity
@Table(name = "depositsaving")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Depositsaving extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "depositsaving_id")
    private Long depositsavingId;

    @Column(name = "payment")
    private Integer payment;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "mission_connected")
    private Boolean missionConnected;

    @OneToOne
    @JoinColumn(name = "withdrawal_account_id")
    private Account withdrawalAccount;

    @OneToOne
    @JoinColumn(name = "account_id")
    private Account account;
}
