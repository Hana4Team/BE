package com.hana.ddok.depositsaving.controller;

import com.hana.ddok.depositsaving.dto.DepositsavingFindbyTypeRes;
import com.hana.ddok.depositsaving.service.DepositsavingService;
import com.hana.ddok.products.domain.ProductsType;
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
@Tag(name = "Depositsaving", description = "예적금 API")
public class DepositsavingController {
    private final DepositsavingService depositsavingService;

    @GetMapping("/depositsaving")
    @Operation(summary = "예적금계좌 상세 조회")
    public ResponseEntity<DepositsavingFindbyTypeRes> depositsavingFindByType(@RequestParam ProductsType type, @AuthenticationPrincipal UsersDetails usersDetails) {
        DepositsavingFindbyTypeRes depositsavingFindbyTypeRes = depositsavingService.depositsavingFindByType(type, usersDetails.getUsername());
        return ResponseEntity.ok(depositsavingFindbyTypeRes);
    }
}
