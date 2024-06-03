package com.hana.ddok.products.exception;

import com.hana.ddok.common.exception.ValueInvalidException;

public class ProductsTypeInvalid extends ValueInvalidException {
    public ProductsTypeInvalid() {
        super("이 상품에 가입할 수 없습니다.");
    }
}
