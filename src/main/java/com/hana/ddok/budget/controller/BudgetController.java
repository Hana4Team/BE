package com.hana.ddok.budget.controller;

import com.hana.ddok.budget.dto.*;
import com.hana.ddok.budget.service.BudgetService;
import com.hana.ddok.users.domain.UsersDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1")
@RestController
@RequiredArgsConstructor
@Tag(name = "Budget", description = "예산 API")
public class BudgetController {
    private final BudgetService budgetService;

    @GetMapping("/budget")
    @Operation(summary = "예산 sum 조회")
    public ResponseEntity<BudgetSumFindRes> budgetSumFind(@AuthenticationPrincipal UsersDetails usersDetails) {
        BudgetSumFindRes budgetSumFindRes = budgetService.budgetSumFind(usersDetails.getUsername());
        return ResponseEntity.ok(budgetSumFindRes);
    }

    @PutMapping("/budget")
    @Operation(summary = "예산 sum 수정")
    public ResponseEntity<BudgetSumUpdateRes> budgetSumUpdate(@RequestBody BudgetSumUpdateReq budgetSumUpdateReq, @AuthenticationPrincipal UsersDetails usersDetails) {
        BudgetSumUpdateRes budgetSumUpdateRes = budgetService.budgetSumUpdate(budgetSumUpdateReq, usersDetails.getUsername());
        return ResponseEntity.ok(budgetSumUpdateRes);
    }

    @GetMapping("/budget/category")
    @Operation(summary = "예산 카테고리별 조회")
    public ResponseEntity<BudgetFindByCategoryRes> budgetFindByCategory(@AuthenticationPrincipal UsersDetails usersDetails) {
        BudgetFindByCategoryRes budgetFindByCategoryRes = budgetService.budgetFindByCategory(usersDetails.getUsername());
        return ResponseEntity.ok(budgetFindByCategoryRes);
    }

    @PutMapping("/budget/category")
    @Operation(summary = "예산 카테고리별 수정")
    public ResponseEntity<BudgetByCategoryUpdateRes> budgetByCategoryUpdate(@RequestBody BudgetByCategoryUpdateReq budgetByCategoryUpdateReq, @AuthenticationPrincipal UsersDetails usersDetails) {
        BudgetByCategoryUpdateRes budgetByCategoryUpdateRes = budgetService.budgetByCategoryUpdate(budgetByCategoryUpdateReq, usersDetails.getUsername());
        return ResponseEntity.ok(budgetByCategoryUpdateRes);
    }
}
