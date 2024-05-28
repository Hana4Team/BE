package com.hana.ddok.products.controller;

import com.hana.ddok.products.dto.res.DepositFindAllResDto;
import com.hana.ddok.products.service.ProductsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/api")
@RestController
@RequiredArgsConstructor
public class ProductsController {
    private final ProductsService productsService;

    @GetMapping("/deposit")
    public List<DepositFindAllResDto> depositFindAll(@RequestParam Integer type) {
        List<DepositFindAllResDto> depositFindAllResDtoList = productsService.depositFindAll(type);
        return depositFindAllResDtoList;
    }
}
