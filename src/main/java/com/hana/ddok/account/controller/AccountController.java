package com.hana.ddok.account.controller;

import com.hana.ddok.account.dto.*;
import com.hana.ddok.account.service.AccountService;
import com.hana.ddok.account.dto.AccountMoneyboxSaveReq;
import com.hana.ddok.account.dto.AccountMoneyboxSaveRes;
import com.hana.ddok.account.dto.AccountDepositSaveReq;
import com.hana.ddok.account.dto.AccountDepositSaveRes;
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
    public ResponseEntity<List<AccountFindAllRes>> accountFindAll(@ModelAttribute AccountFindAllReq accountFindAllReq, @AuthenticationPrincipal UsersDetails usersDetails) {
        List<AccountFindAllRes> accountFindAllResList = accountService.accountFindAll(accountFindAllReq, usersDetails.getUsername());
        return ResponseEntity.ok(accountFindAllResList);
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

    @PostMapping("/account/saving")
    public ResponseEntity<AccountSavingSaveRes> accountSavingSave(@RequestBody AccountSavingSaveReq accountSavingSaveReq, @AuthenticationPrincipal UsersDetails usersDetails) {
        AccountSavingSaveRes accountSavingSaveRes = accountService.accountSavingSave(accountSavingSaveReq, usersDetails.getUsername());
        return ResponseEntity.ok(accountSavingSaveRes);
    }

    @PostMapping("/account/deposit")
    public ResponseEntity<AccountDepositSaveRes> accountDepositSave(@RequestBody AccountDepositSaveReq accountDepositSaveReq, @AuthenticationPrincipal UsersDetails usersDetails) {
        AccountDepositSaveRes accountDepositSaveRes = accountService.accountDepositSave(accountDepositSaveReq, usersDetails.getUsername());
        return ResponseEntity.ok(accountDepositSaveRes);
    }

    @DeleteMapping("/account/depositsaving")
    public ResponseEntity<AccountDepositDeleteRes> accountDepositDelete(@RequestBody AccountDepositsavingDeleteReq accountDepositsavingDeleteReq) {
        AccountDepositDeleteRes accountDepositDeleteRes = accountService.accountDepositDelete(accountDepositsavingDeleteReq);
        return ResponseEntity.ok(accountDepositDeleteRes);
    }

    @PostMapping("/account/password")
    public ResponseEntity<AccountPasswordCheckRes> accountPasswordCheck(@RequestBody AccountPasswordCheckReq accountPasswordCheckReq) {
        AccountPasswordCheckRes accountPasswordCheckRes = accountService.accountPasswordCheck(accountPasswordCheckReq);
        return ResponseEntity.ok(accountPasswordCheckRes);
    }
}
