package com.hana.ddok.mission.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "mission")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Mission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mission_id")
    private Long missionId;

    @Column(name = "step", nullable = false)
    private Integer step;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "sub_title", nullable = false)
    private String subTitle;

    @Column(name = "desc", nullable = false)
    private String desc;

    @Column(name = "points")
    private Integer points;
}
