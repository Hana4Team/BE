package com.hana.ddok.moneybox.controller;

import com.hana.ddok.moneybox.dto.MoneyboxFindAllRes;
import com.hana.ddok.moneybox.dto.MoneyboxFindBySavingRes;
import com.hana.ddok.moneybox.service.MoneyboxService;
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
@Tag(name = "Moneybox", description = "머니박스 API")
public class MoneyboxController {
    private final MoneyboxService moneyboxService;

    @GetMapping("/moneybox")
    @Operation(summary = "머니박스 전체 조회")
    public ResponseEntity<MoneyboxFindAllRes> moneyboxFindAll(@AuthenticationPrincipal UsersDetails usersDetails) {
        MoneyboxFindAllRes moneyboxFindAllRes = moneyboxService.moneyboxFindAll(usersDetails.getUsername());
        return ResponseEntity.ok(moneyboxFindAllRes);
    }

    @GetMapping("/moneybox/saving")
    @Operation(summary = "머니박스 저축 조회")
    public ResponseEntity<MoneyboxFindBySavingRes> moneyboxFindBySaving(@AuthenticationPrincipal UsersDetails usersDetails) {
        MoneyboxFindBySavingRes moneyboxFindBySavingRes = moneyboxService.moneyboxFindBySavingRes(usersDetails.getUsername());
        return ResponseEntity.ok(moneyboxFindBySavingRes);
    }
}
