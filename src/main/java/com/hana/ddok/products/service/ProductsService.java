package com.hana.ddok.products.service;

import com.hana.ddok.common.exception.EntityNotFoundException;
import com.hana.ddok.products.dto.ProductsFindAllRes;
import com.hana.ddok.products.dto.ProductsFindByIdRes;
import com.hana.ddok.products.repository.ProductsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductsService {
    private final ProductsRepository productsRepository;

    @Transactional(readOnly = true)
    public List<ProductsFindAllRes> productsFindAll(Integer type) {
        List<ProductsFindAllRes> productsFindAllResList = productsRepository.findByType(type).stream()
                .map(ProductsFindAllRes::new)
                .toList();
        return productsFindAllResList;
    }

    @Transactional(readOnly = true)
    public ProductsFindByIdRes productsFindById(Long productsId) {
        ProductsFindByIdRes productsFindByIdRes = productsRepository.findById(productsId)
                .map(ProductsFindByIdRes::new)
                .orElseThrow(() -> new EntityNotFoundException("해당 상품을 찾을 수 없습니다."));
        return productsFindByIdRes;
    }
}
