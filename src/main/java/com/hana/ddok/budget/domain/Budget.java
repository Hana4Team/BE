package com.hana.ddok.budget.domain;


import com.hana.ddok.account.exception.AccountBalanceInvalid;
import com.hana.ddok.users.domain.Users;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "budget")
public class Budget {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "budget_id", nullable = false)
    private Long budgetId;

    @Column(name = "sum", nullable = false)
    private Long sum;

    @Column(name = "shopping", nullable = false)
    private Long shopping;

    @Column(name = "food", nullable = false)
    private Long food;

    @Column(name = "traffic", nullable = false)
    private Long traffic;

    @Column(name = "hospital", nullable = false)
    private Long hospital;

    @Column(name = "fee", nullable = false)
    private Long fee;

    @Column(name = "education", nullable = false)
    private Long education;

    @Column(name = "leisure", nullable = false)
    private Long leisure;

    @Column(name = "society", nullable = false)
    private Long society;

    @Column(name = "daily", nullable = false)
    private Long daily;

    @Column(name = "overseas", nullable = false)
    private Long overseas;

    public void updateSum(Long sum) {
        this.sum = sum;
    }
}
