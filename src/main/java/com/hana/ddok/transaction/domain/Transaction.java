package com.hana.ddok.transaction.domain;

import com.hana.ddok.account.domain.Account;
import com.hana.ddok.common.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "transaction")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Transaction extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_id")
    private Long transactionId;

    @Column(name = "amount", nullable = false)
    private Integer amount;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "type", nullable = false)
    private TransactionType type;

    @Column(name = "sender_title")
    private String senderTitle;

    @Column(name = "recipient_title")
    private String recipientTitle;

    @ManyToOne
    @JoinColumn(name = "sender_account_id")
    private Account senderAccount;

    @ManyToOne
    @JoinColumn(name = "recipient_account_id")
    private Account recipientAccount;
}