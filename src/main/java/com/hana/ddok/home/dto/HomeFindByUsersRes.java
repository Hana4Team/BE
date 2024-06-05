package com.hana.ddok.home.dto;

import com.hana.ddok.home.domain.Home;
import com.hana.ddok.products.domain.Products;
import lombok.Getter;

public record HomeFindByUsersRes(
        String name,
        String image,
        String background
) {

    public HomeFindByUsersRes(Home home) {
        this(
                home.getName(),
                home.getImage(),
                home.getBackground()
        );
    }
}
