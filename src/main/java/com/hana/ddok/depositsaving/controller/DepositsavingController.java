package com.hana.ddok.depositsaving.controller;

import com.hana.ddok.depositsaving.dto.DepositsavingSaveReq;
import com.hana.ddok.depositsaving.dto.DepositsavingSaveRes;
import com.hana.ddok.depositsaving.service.DepositsavingService;
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

    @PostMapping("/depositsaving")
    public ResponseEntity<DepositsavingSaveRes> depositsavingSave(@RequestBody DepositsavingSaveReq depositsavingSaveReq, @AuthenticationPrincipal UsersDetails usersDetails) {
        DepositsavingSaveRes depositsavingSaveRes = depositsavingService.depositsavingSave(depositsavingSaveReq, usersDetails.getUsername());
        return ResponseEntity.ok(depositsavingSaveRes);
    }
}
