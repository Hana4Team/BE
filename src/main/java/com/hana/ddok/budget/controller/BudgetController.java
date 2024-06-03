package com.hana.ddok.budget.controller;

import com.hana.ddok.budget.dto.*;
import com.hana.ddok.budget.service.BudgetService;
import com.hana.ddok.users.domain.UsersDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1")
@RestController
@RequiredArgsConstructor
public class BudgetController {
    private final BudgetService budgetService;

    @GetMapping("/budget")
    public ResponseEntity<BudgetFindRes> budgetFind(@AuthenticationPrincipal UsersDetails usersDetails) {
        BudgetFindRes budgetFindRes = budgetService.budgetFind(usersDetails.getUsername());
        return ResponseEntity.ok(budgetFindRes);
    }

    @PutMapping("/budget")
    public ResponseEntity<BudgetUpdateRes> budgetUpdate(@RequestBody BudgetUpdateReq budgetUpdateReq, @AuthenticationPrincipal UsersDetails usersDetails) {
        BudgetUpdateRes budgetUpdateRes = budgetService.budgetUpdate(budgetUpdateReq, usersDetails.getUsername());
        return ResponseEntity.ok(budgetUpdateRes);
    }

    @GetMapping("/budget/category")
    public ResponseEntity<BudgetFindByCategoryRes> budgetFindByCategory(@AuthenticationPrincipal UsersDetails usersDetails) {
        BudgetFindByCategoryRes budgetFindByCategoryRes = budgetService.budgetFindByCategory(usersDetails.getUsername());
        return ResponseEntity.ok(budgetFindByCategoryRes);
    }

    @PutMapping("/budget/category")
    public ResponseEntity<BudgetByCategoryUpdateRes> budgetByCategoryUpdate(@RequestBody BudgetByCategoryUpdateReq budgetByCategoryUpdateReq, @AuthenticationPrincipal UsersDetails usersDetails) {
        BudgetByCategoryUpdateRes budgetByCategoryUpdateRes = budgetService.budgetByCategoryUpdate(budgetByCategoryUpdateReq, usersDetails.getUsername());
        return ResponseEntity.ok(budgetByCategoryUpdateRes);
    }
}
