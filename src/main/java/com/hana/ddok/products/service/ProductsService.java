package com.hana.ddok.products.service;

import com.hana.ddok.common.exception.EntityNotFoundException;
import com.hana.ddok.products.domain.ProductsType;
import com.hana.ddok.products.dto.ProductsFindAllRes;
import com.hana.ddok.products.dto.ProductsFindByIdRes;
import com.hana.ddok.products.repository.ProductsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductsService {
    private final ProductsRepository productsRepository;

    @Transactional(readOnly = true)
    public List<ProductsFindAllRes> productsFindAll(ProductsType type) {
        List<ProductsFindAllRes> productsFindAllResList = productsRepository.findAllByType(type).stream()
                .map(ProductsFindAllRes::new)
                .collect(Collectors.toList());
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
