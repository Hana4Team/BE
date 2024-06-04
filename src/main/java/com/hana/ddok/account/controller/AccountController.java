package com.hana.ddok.account.controller;

import com.hana.ddok.account.dto.*;
import com.hana.ddok.account.service.AccountService;
import com.hana.ddok.account.dto.AccountFindbyMissionRes;
import com.hana.ddok.account.dto.AccountMoneyboxSaveReq;
import com.hana.ddok.account.dto.AccountMoneyboxSaveRes;
import com.hana.ddok.account.dto.AccountDepositsavingSaveReq;
import com.hana.ddok.account.dto.AccountDepositsavingSaveRes;
import com.hana.ddok.users.domain.UsersDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/v1")
@RestController
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @GetMapping("/account")
    public ResponseEntity<List<AccountFindAllRes>> accountFindAll(@RequestBody AccountFindAllReq accountFindAllReq, @AuthenticationPrincipal UsersDetails usersDetails) {
        List<AccountFindAllRes> accountFindAllResList = accountService.accountFindAll(accountFindAllReq, usersDetails.getUsername());
        return ResponseEntity.ok(accountFindAllResList);
    }

    @GetMapping("/account/{accountId}")
    public ResponseEntity<AccountFindByIdRes> accountFindById(@PathVariable Long accountId, @RequestParam Integer year, @RequestParam Integer month) {
        AccountFindByIdRes accountFindByIdRes = accountService.accountFindById(accountId, year, month);
        return ResponseEntity.ok(accountFindByIdRes);
    }

    @GetMapping("/account/mission")
    public ResponseEntity<AccountFindbyMissionRes> accountFindByMission(@AuthenticationPrincipal UsersDetails usersDetails) {
        AccountFindbyMissionRes accountFindbyMissionRes = accountService.accountFindByMission(usersDetails.getUsername());
        return ResponseEntity.ok(accountFindbyMissionRes);
    }

    @PostMapping("/account/moneybox")
    public ResponseEntity<AccountMoneyboxSaveRes> accountMoneyboxSave(@RequestBody AccountMoneyboxSaveReq accountMoneyboxSaveReq, @AuthenticationPrincipal UsersDetails usersDetails) {
        AccountMoneyboxSaveRes AccountMoneyboxSaveRes = accountService.accountMoneyboxSave(accountMoneyboxSaveReq, usersDetails.getUsername());
        return ResponseEntity.ok(AccountMoneyboxSaveRes);
    }

    @PostMapping("/account/saving100")
    public ResponseEntity<AccountSaving100SaveRes> accountSaving100Save(@RequestBody AccountSaving100SaveReq accountSaving100SaveReq, @AuthenticationPrincipal UsersDetails usersDetails) {
        AccountSaving100SaveRes accountSaving100SaveRes = accountService.accountSaving100Save(accountSaving100SaveReq, usersDetails.getUsername());
        return ResponseEntity.ok(accountSaving100SaveRes);
    }

    @PostMapping("/account/depositsaving")
    public ResponseEntity<AccountDepositsavingSaveRes> accountDepositsavingSave(@RequestBody AccountDepositsavingSaveReq accountDepositsavingSaveReq, @AuthenticationPrincipal UsersDetails usersDetails) {
        AccountDepositsavingSaveRes accountDepositsavingSaveRes = accountService.accountDepositsavingSave(accountDepositsavingSaveReq, usersDetails.getUsername());
        return ResponseEntity.ok(accountDepositsavingSaveRes);
    }
}
