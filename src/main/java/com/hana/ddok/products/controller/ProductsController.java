package com.hana.ddok.products.controller;

import com.hana.ddok.products.domain.ProductsType;
import com.hana.ddok.products.dto.ProductsFindAllRes;
import com.hana.ddok.products.dto.ProductsFindByIdRes;
import com.hana.ddok.products.service.ProductsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/v1")
@RestController
@RequiredArgsConstructor
@Tag(name = "Products", description = "상품 API")
public class ProductsController {
    private final ProductsService productsService;

    @GetMapping("/products")
    @Operation(summary = "상품 전체 조회")
    public ResponseEntity<List<ProductsFindAllRes>> productsFindAll(@RequestParam ProductsType type) {
        List<ProductsFindAllRes> productsFindAllResList = productsService.productsFindAll(type);
        return ResponseEntity.ok(productsFindAllResList);
    }

    @GetMapping("/products/{productsId}")
    @Operation(summary = "상품 상세 조회")
    public ResponseEntity<ProductsFindByIdRes> productsFindById(@PathVariable Long productsId) {
        ProductsFindByIdRes productsFindByIdRes = productsService.productsFindById(productsId);
        return ResponseEntity.ok(productsFindByIdRes);
    }
}
