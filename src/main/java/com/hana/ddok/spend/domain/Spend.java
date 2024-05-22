package com.hana.ddok.spend.domain;

import com.hana.ddok.common.domain.BaseEntity;
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

    @Column(name = "spend_date", nullable = false)
    private LocalDate spendDate;

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

    @ManyToOne
    @JoinColumn(name = "users_id")
    private Users users;
}