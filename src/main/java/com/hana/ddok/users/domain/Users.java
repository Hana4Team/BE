package com.hana.ddok.users.domain;
import com.hana.ddok.budget.domain.Budget;
import com.hana.ddok.budget.exception.BudgetNotFound;
import com.hana.ddok.common.exception.EntityNotFoundException;
import com.hana.ddok.common.exception.ValueInvalidException;
import com.hana.ddok.home.domain.Home;
import com.hana.ddok.home.exception.HomeNotFound;
import com.hana.ddok.users.exception.UsersPointsInvalid;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;


@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users")
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "users_id", nullable = false)
    private Long usersId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "step", nullable = false)
    private Integer step;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "step_status", nullable = false)
    private UsersStepStatus stepStatus;

    @Column(name = "points", nullable = false)
    private Integer points;

    @Column(name = "read_news", nullable = false)
    private Boolean readNews;

    @ManyToOne
    @JoinColumn(name = "home_id", nullable = false)
    private Home home;

    @OneToOne
    @JoinColumn(name = "budget_id")
    private Budget budget;

    public void updateReadNews(Boolean readNews) {
        this.readNews = readNews;
    }

    public void updateStep(Integer step) {
        this.step = step;
    }
    public void updateStepStatus(UsersStepStatus stepStatus) {
        this.stepStatus = stepStatus;
    }

    public void updatePoints(Integer points) {
        if (points < 0) {
            throw new UsersPointsInvalid();
        }
        this.points = points;
    }

    public void updateHome(Home home) {
        if (home == null) {
            throw new HomeNotFound();
        }
        this.home = home;
    }

    public void updateBudget(Budget budget) {
        if (budget == null) {
            throw new BudgetNotFound();
        }
        this.budget = budget;
    }
}
