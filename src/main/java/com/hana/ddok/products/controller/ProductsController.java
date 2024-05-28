package com.hana.ddok.products.controller;

import com.hana.ddok.products.dto.res.DepositFindAllResDto;
import com.hana.ddok.products.dto.res.DepositFindByIdResDto;
import com.hana.ddok.products.service.ProductsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/deposit/{productsId}")
    public DepositFindByIdResDto depositFindById(@PathVariable Long productsId) {
        DepositFindByIdResDto depositFindByIdResDto = productsService.depositFindById(productsId);
        return depositFindByIdResDto;
    }
}
