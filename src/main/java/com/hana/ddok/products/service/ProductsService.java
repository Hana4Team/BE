package com.hana.ddok.products.service;

import com.hana.ddok.common.exception.EntityNotFoundException;
import com.hana.ddok.products.dto.res.ProductsFindAllResDto;
import com.hana.ddok.products.dto.res.ProductsFindByIdResDto;
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
    public List<ProductsFindAllResDto> productsFindAll(Integer type) {
        List<ProductsFindAllResDto> productsFindAllResDtoList = productsRepository.findByType(type).stream()
                .map(ProductsFindAllResDto::new)
                .toList();
        return productsFindAllResDtoList;
    }

    @Transactional(readOnly = true)
    public ProductsFindByIdResDto productsFindById(Long productsId) {
        ProductsFindByIdResDto productsFindByIdResDto = productsRepository.findById(productsId)
                .map(ProductsFindByIdResDto::new)
                .orElseThrow(() -> new EntityNotFoundException("해당 상품을 찾을 수 없습니다."));
        return productsFindByIdResDto;
    }
}
