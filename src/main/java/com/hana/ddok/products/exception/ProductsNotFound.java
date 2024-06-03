package com.hana.ddok.products.exception;

import com.hana.ddok.common.exception.EntityNotFoundException;

public class ProductsNotFound extends EntityNotFoundException {
    public ProductsNotFound() {
        super("상품을 찾을 수 없습니다.");
    }
}
