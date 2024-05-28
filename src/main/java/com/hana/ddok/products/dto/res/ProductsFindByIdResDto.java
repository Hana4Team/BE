package com.hana.ddok.products.dto.res;

import com.hana.ddok.products.domain.Products;
import lombok.Getter;

@Getter
public class ProductsFindByIdResDto {
    private Long productsId;
    private String name;
    private String title;
    private String summary;
    private Float interest1;
    private Float interest2;
    private String period;
    private Integer payment1;
    private Integer payment2;
    private String desc1;
    private String descDetail1;
    private String descImage1;
    private String desc2;
    private String descDetail2;
    private String descImage2;

    public ProductsFindByIdResDto(Products products) {
        this.productsId = products.getProductsId();
        this.name = products.getName();
        this.title = products.getTitle();
        this.summary = products.getSummary();
        this.interest1 = products.getInterest1();
        this.interest2 = products.getInterest2();
        this.period = products.getPeriod();
        this.payment1 = products.getPayment1();
        this.payment2 = products.getPayment2();
        this.desc1 = products.getDesc1();
        this.descDetail1 = products.getDescDetail1();
        this.descImage1 = products.getDescImage1();
        this.desc2 = products.getDesc2();
        this.descDetail2 = products.getDescDetail2();
        this.descImage2 = products.getDescImage2();
    }
}
