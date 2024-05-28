package com.hana.ddok.products.controller;

import com.hana.ddok.products.dto.res.ProductsFindAllResDto;
import com.hana.ddok.products.dto.res.ProductsFindByIdResDto;
import com.hana.ddok.products.service.ProductsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/v1")
@RestController
@RequiredArgsConstructor
public class ProductsController {
    private final ProductsService productsService;

    @GetMapping("/products")
    public List<ProductsFindAllResDto> productsFindAll(@RequestParam Integer type) {
        List<ProductsFindAllResDto> productsFindAllResDtoList = productsService.productsFindAll(type);
        return productsFindAllResDtoList;
    }

    @GetMapping("/products/{productsId}")
    public ProductsFindByIdResDto productsFindById(@PathVariable Long productsId) {
        ProductsFindByIdResDto productsFindByIdResDto = productsService.productsFindById(productsId);
        return productsFindByIdResDto;
    }
}
