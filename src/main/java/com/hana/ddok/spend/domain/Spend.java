package com.hana.ddok.spend.domain;

import com.hana.ddok.common.domain.BaseEntity;
import com.hana.ddok.transaction.domain.Transaction;
import com.hana.ddok.users.domain.Users;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Entity
@Builder
@Table(name = "spend")
@NoArgsConstructor
@AllArgsConstructor
public class Spend extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "spend_id")
    private Long spendId;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private SpendType type;

    @ManyToOne
    @JoinColumn(name = "transaction_id")
    private Transaction transaction;
}