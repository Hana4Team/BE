package com.hana.ddok.depositsaving.controller;

import com.hana.ddok.depositsaving.dto.DepositsavingFindbyTypeRes;
import com.hana.ddok.depositsaving.service.DepositsavingService;
import com.hana.ddok.products.domain.ProductsType;
import com.hana.ddok.users.domain.UsersDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1")
@RestController
@RequiredArgsConstructor
public class DepositsavingController {
    private final DepositsavingService depositsavingService;

    @GetMapping("/depositsaving")
    public ResponseEntity<DepositsavingFindbyTypeRes> depositsavingFindByType(@RequestParam ProductsType type, @AuthenticationPrincipal UsersDetails usersDetails) {
        DepositsavingFindbyTypeRes depositsavingFindbyTypeRes = depositsavingService.depositsavingFindByType(type, usersDetails.getUsername());
        return ResponseEntity.ok(depositsavingFindbyTypeRes);
    }
}
