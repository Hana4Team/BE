package com.hana.ddok.account.domain;

import com.hana.ddok.account.exception.AccountBalanceInvalid;
import com.hana.ddok.common.domain.BaseEntity;
import com.hana.ddok.products.domain.Products;
import com.hana.ddok.users.domain.Users;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @Column(name = "balance", nullable = false)
    private Long balance;

    @Column(name = "interest", nullable = false)
    private Float interest;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "is_mission_connected")
    private Boolean isMissionConnected;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted;

    @ManyToOne
    @JoinColumn(name = "users_id")
    private Users users;

    @ManyToOne
    @JoinColumn(name = "products_id")
    private Products products;

    public void updateBalance(Long balance) {
        this.balance += balance;
        if (this.balance < 0) {
            throw new AccountBalanceInvalid();
        }
    }

    public void updateIsMissionConnected(Boolean isMissionConnected) {
        this.isMissionConnected = isMissionConnected;
    }
}