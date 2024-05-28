package com.hana.ddok.account.controller;

import com.hana.ddok.account.domain.Account;
import com.hana.ddok.account.dto.AccountFindAllRes;
import com.hana.ddok.account.dto.AccountSaveReq;
import com.hana.ddok.account.dto.AccountSaveRes;
import com.hana.ddok.account.dto.MoneyboxFindAllRes;
import com.hana.ddok.account.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/v1")
@RestController
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @PostMapping("/account")
    public ResponseEntity<AccountSaveRes> accountSave(@RequestBody AccountSaveReq accountSaveReq) {
        AccountSaveRes accountSaveRes = accountService.accountSave(accountSaveReq);
        return ResponseEntity.ok(accountSaveRes);
    }

    @GetMapping("/account")
    public ResponseEntity<List<AccountFindAllRes>> accountFindAll() {
        List<AccountFindAllRes> accountFindAllResList = accountService.accountFindAll();
        return ResponseEntity.ok(accountFindAllResList);
    }

    @GetMapping("/account/moneybox")
    public ResponseEntity<MoneyboxFindAllRes> moneyboxFindAll() {
        MoneyboxFindAllRes moneyboxFindAll = accountService.moneyboxFindAll();
        return ResponseEntity.ok(moneyboxFindAll);
    }
}
