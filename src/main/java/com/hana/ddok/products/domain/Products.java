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

    @Column(name = "name", nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private ProductsType type;

    @Column(name = "title")
    private String title;

    @Column(name = "summary")
    private String summary;

    @Column(name = "interest1", nullable = false)
    private Float interest1;

    @Column(name = "interest2", nullable = false)
    private Float interest2;

    @Column(name = "period")
    private String period;

    @Column(name = "payment1")
    private Long payment1;

    @Column(name = "payment2")
    private Long payment2;

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