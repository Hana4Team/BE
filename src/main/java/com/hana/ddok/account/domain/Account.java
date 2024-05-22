package com.hana.ddok.account.domain;

import com.hana.ddok.common.domain.BaseEntity;
import com.hana.ddok.products.domain.Products;
import com.hana.ddok.users.domain.Users;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "account")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Account extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    private Long accountId;

    @Column(name = "account_number", nullable = false)
    private String accountNumber;

    @Column(name = "type", nullable = false)
    private Integer type;

    @Column(name = "balance", nullable = false)
    private Long balance;

    @Column(name = "payment")
    private Integer payment;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "is_deleted", nullable = false)
    private Integer isDeleted;

    @ManyToOne
    @JoinColumn(name = "users_id")
    private Users users;

    @ManyToOne
    @JoinColumn(name = "products_id")
    private Products products;
}