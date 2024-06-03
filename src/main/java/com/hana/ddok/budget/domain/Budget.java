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
    private Integer sum;

    @Column(name = "shopping", nullable = false)
    private Integer shopping;

    @Column(name = "food", nullable = false)
    private Integer food;

    @Column(name = "traffic", nullable = false)
    private Integer traffic;

    @Column(name = "hospital", nullable = false)
    private Integer hospital;

    @Column(name = "fee", nullable = false)
    private Integer fee;

    @Column(name = "education", nullable = false)
    private Integer education;

    @Column(name = "leisure", nullable = false)
    private Integer leisure;

    @Column(name = "society", nullable = false)
    private Integer society;

    @Column(name = "daily", nullable = false)
    private Integer daily;

    @Column(name = "overseas", nullable = false)
    private Integer overseas;

    @OneToOne(mappedBy = "budget", cascade = CascadeType.ALL, orphanRemoval = true)
    private Users users;

    public void setSum(Integer sum) {
        this.sum = sum;
    }
}
