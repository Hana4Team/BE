package com.hana.ddok.products.domain;

import com.hana.ddok.common.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@Entity
@Table(name = "products")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Products extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "products_id")
    private Long productsId;

    @Column(name = "name", nullable = false, length = 20)
    private String name;

    @Column(name = "type", nullable = false)
    private Integer type;

    @Column(name = "title")
    private String title;

    @Column(name = "summary1")
    private String summary1;

    @Column(name = "summary2")
    private String summary2;

    @Column(name = "period")
    private String period;

    @Column(name = "payment")
    private Integer payment;

    @Column(name = "desc1")
    private String desc1;

    @Column(name = "desc_detail1")
    private String descDetail1;

    @Column(name = "desc_image1")
    private String descImage1;

    @Column(name = "desc2")
    private String desc2;

    @Column(name = "desc_detail2")
    private String descDetail2;

    @Column(name = "desc_image2")
    private String descImage2;
}