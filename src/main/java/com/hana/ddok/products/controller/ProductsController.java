package com.hana.ddok.products.controller;

import com.hana.ddok.products.dto.res.ProductsFindAllRes;
import com.hana.ddok.products.dto.res.ProductsFindByIdRes;
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
    public List<ProductsFindAllRes> productsFindAll(@RequestParam Integer type) {
        List<ProductsFindAllRes> productsFindAllResList = productsService.productsFindAll(type);
        return productsFindAllResList;
    }

    @GetMapping("/products/{productsId}")
    public ProductsFindByIdRes productsFindById(@PathVariable Long productsId) {
        ProductsFindByIdRes productsFindByIdRes = productsService.productsFindById(productsId);
        return productsFindByIdRes;
    }
}
