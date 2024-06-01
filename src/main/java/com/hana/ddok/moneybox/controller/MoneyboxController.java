package com.hana.ddok.moneybox.controller;

import com.hana.ddok.moneybox.dto.MoneyboxFindAllRes;
import com.hana.ddok.moneybox.dto.MoneyboxFindBySavingRes;
import com.hana.ddok.moneybox.dto.MoneyboxSaveReq;
import com.hana.ddok.moneybox.dto.MoneyboxSaveRes;
import com.hana.ddok.moneybox.service.MoneyboxService;
import com.hana.ddok.users.domain.UsersDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1")
@RestController
@RequiredArgsConstructor
public class MoneyboxController {
    private final MoneyboxService moneyboxService;

    @PostMapping("/account/moneybox")
    public ResponseEntity<MoneyboxSaveRes> moneyboxSave(@RequestBody MoneyboxSaveReq moneyboxSaveReq, @AuthenticationPrincipal UsersDetails usersDetails) {
        MoneyboxSaveRes MoneyboxSaveRes = moneyboxService.moneyboxSave(moneyboxSaveReq, usersDetails.getUsername());
        return ResponseEntity.ok(MoneyboxSaveRes);
    }

    @GetMapping("/account/moneybox")
    public ResponseEntity<MoneyboxFindAllRes> moneyboxFindAll(@AuthenticationPrincipal UsersDetails usersDetails) {
        MoneyboxFindAllRes moneyboxFindAllRes = moneyboxService.moneyboxFindAll(usersDetails.getUsername());
        return ResponseEntity.ok(moneyboxFindAllRes);
    }

    @GetMapping("/account/moneybox/saving")
    public ResponseEntity<MoneyboxFindBySavingRes> moneyboxFindBySaving(@AuthenticationPrincipal UsersDetails usersDetails) {
        MoneyboxFindBySavingRes moneyboxFindBySavingRes = moneyboxService.moneyboxFindBySavingRes(usersDetails.getUsername());
        return ResponseEntity.ok(moneyboxFindBySavingRes);
    }
}
