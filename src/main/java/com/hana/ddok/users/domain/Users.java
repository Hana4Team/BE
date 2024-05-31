package com.hana.ddok.users.domain;
import com.hana.ddok.budget.domain.Budget;
import com.hana.ddok.common.exception.EntityNotFoundException;
import com.hana.ddok.common.exception.ValueInvalidException;
import com.hana.ddok.home.domain.Home;
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

    @Column(name = "step_status")
    private Integer stepStatus;

    @Column(name = "points", nullable = false)
    private Integer points;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "home_id", nullable = false)
    private Home home;

    @OneToOne
    @JoinColumn(name = "budget_id")
    private Budget budget;

    public void updateStep(Integer step) {
        this.step = step;
    }
    public void updateStepStatus(Integer stepStatus) {
        this.stepStatus = stepStatus;
    }

    public void updatePoints(Integer points) {
        if (points < 0) {
            throw new ValueInvalidException("하나머니는 마이너스가 될 수 없습니다.");
        }
        this.points = points;
    }

    public void updateHome(Home home) {
        if (home == null) {
            throw new EntityNotFoundException("집을 찾을 수 없습니다.");
        }
        this.home = home;
    }

}
