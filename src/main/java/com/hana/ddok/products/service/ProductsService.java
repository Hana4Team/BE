package com.hana.ddok.products.service;

import com.hana.ddok.common.exception.EntityNotFoundException;
import com.hana.ddok.products.domain.Products;
import com.hana.ddok.products.dto.res.DepositFindAllResDto;
import com.hana.ddok.products.dto.res.DepositFindByIdResDto;
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
    public List<DepositFindAllResDto> depositFindAll(Integer type) {
        List<DepositFindAllResDto> depositFindAllResDtoList = productsRepository.findByType(type).stream()
                .map(DepositFindAllResDto::new)
                .toList();
        return depositFindAllResDtoList;
    }

    @Transactional(readOnly = true)
    public DepositFindByIdResDto depositFindById(Long productsId) {
        DepositFindByIdResDto depositFindByIdResDto = productsRepository.findById(productsId)
                .map(DepositFindByIdResDto::new)
                .orElseThrow(() -> new EntityNotFoundException("해당 상품을 찾을 수 없습니다."));
        return depositFindByIdResDto;
    }
}
