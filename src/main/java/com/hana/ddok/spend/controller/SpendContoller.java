package com.hana.ddok.spend.controller;

import com.hana.ddok.spend.dto.SpendFindAllRes;
import com.hana.ddok.spend.service.SpendService;
import com.hana.ddok.transaction.dto.*;
import com.hana.ddok.transaction.service.TransactionService;
import com.hana.ddok.users.domain.UsersDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/v1")
@RestController
@RequiredArgsConstructor
@Tag(name = "Spend", description = "지출 API")
public class SpendContoller {
    private final SpendService spendService;

    @GetMapping("spend")
    @Operation(summary = "지출 전체 조회")
    public ResponseEntity<SpendFindAllRes> spendFindAll(@RequestParam Integer year, @RequestParam Integer month, @AuthenticationPrincipal UsersDetails usersDetails) {
        SpendFindAllRes spendFindAllRes = spendService.spendFindAll(year, month, usersDetails.getUsername());
        return ResponseEntity.ok(spendFindAllRes);
    }
}
