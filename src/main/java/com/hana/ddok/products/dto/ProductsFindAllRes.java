package com.hana.ddok.products.dto;

import com.hana.ddok.products.domain.Products;
import lombok.Getter;

@Getter
public class ProductsFindAllRes {
    private Long productsId;
    private String name;
    private String title;
    private String summary;
    private Float interest1;
    private Float interest2;

    public ProductsFindAllRes(Products products) {
        this.productsId = products.getProductsId();
        this.name = products.getName();
        this.title = products.getTitle();
        this.summary = products.getSummary();
        this.interest1 = products.getInterest1();
        this.interest2 = products.getInterest2();
    }
}
