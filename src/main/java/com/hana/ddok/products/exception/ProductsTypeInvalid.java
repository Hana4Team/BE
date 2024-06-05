package com.hana.ddok.products.exception;

import com.hana.ddok.common.exception.ValueInvalidException;

public class ProductsTypeInvalid extends ValueInvalidException {
    public ProductsTypeInvalid() {
        super("상품의 타입이 올바르지 않습니다.");
    }
}
