package com.hana.ddok.budget.controller;

import com.hana.ddok.budget.dto.BudgetFindRes;
import com.hana.ddok.budget.dto.BudgetUpdateReq;
import com.hana.ddok.budget.dto.BudgetUpdateRes;
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
}
