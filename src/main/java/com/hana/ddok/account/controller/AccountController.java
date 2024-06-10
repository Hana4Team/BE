package com.hana.ddok.account.controller;

import com.hana.ddok.account.dto.*;
import com.hana.ddok.account.service.AccountService;
import com.hana.ddok.account.dto.AccountMoneyboxSaveReq;
import com.hana.ddok.account.dto.AccountMoneyboxSaveRes;
import com.hana.ddok.account.dto.AccountDepositSaveReq;
import com.hana.ddok.account.dto.AccountDepositSaveRes;
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
@Tag(name = "Account", description = "계좌 API")
public class AccountController {
    private final AccountService accountService;

    @GetMapping("/account")
    @Operation(summary = "계좌 조회")
    public ResponseEntity<List<AccountFindAllRes>> accountFindAll(@ModelAttribute AccountFindAllReq accountFindAllReq, @AuthenticationPrincipal UsersDetails usersDetails) {
        List<AccountFindAllRes> accountFindAllResList = accountService.accountFindAll(accountFindAllReq, usersDetails.getUsername());
        return ResponseEntity.ok(accountFindAllResList);
    }

    @PostMapping("/account/moneybox")
    @Operation(summary = "머니박스 개설")
    public ResponseEntity<AccountMoneyboxSaveRes> accountMoneyboxSave(@RequestBody AccountMoneyboxSaveReq accountMoneyboxSaveReq, @AuthenticationPrincipal UsersDetails usersDetails) {
        AccountMoneyboxSaveRes AccountMoneyboxSaveRes = accountService.accountMoneyboxSave(accountMoneyboxSaveReq, usersDetails.getUsername());
        return ResponseEntity.ok(AccountMoneyboxSaveRes);
    }

    @PostMapping("/account/saving100")
    @Operation(summary = "100일적금 개설")
    public ResponseEntity<AccountSaving100SaveRes> accountSaving100Save(@RequestBody AccountSaving100SaveReq accountSaving100SaveReq, @AuthenticationPrincipal UsersDetails usersDetails) {
        AccountSaving100SaveRes accountSaving100SaveRes = accountService.accountSaving100Save(accountSaving100SaveReq, usersDetails.getUsername());
        return ResponseEntity.ok(accountSaving100SaveRes);
    }

    @PostMapping("/account/saving")
    @Operation(summary = "적금 개설")
    public ResponseEntity<AccountSavingSaveRes> accountSavingSave(@RequestBody AccountSavingSaveReq accountSavingSaveReq, @AuthenticationPrincipal UsersDetails usersDetails) {
        AccountSavingSaveRes accountSavingSaveRes = accountService.accountSavingSave(accountSavingSaveReq, usersDetails.getUsername());
        return ResponseEntity.ok(accountSavingSaveRes);
    }

    @PostMapping("/account/deposit")
    @Operation(summary = "예금 개설")
    public ResponseEntity<AccountDepositSaveRes> accountDepositSave(@RequestBody AccountDepositSaveReq accountDepositSaveReq, @AuthenticationPrincipal UsersDetails usersDetails) {
        AccountDepositSaveRes accountDepositSaveRes = accountService.accountDepositSave(accountDepositSaveReq, usersDetails.getUsername());
        return ResponseEntity.ok(accountDepositSaveRes);
    }

    @DeleteMapping("/account")
    @Operation(summary = "계좌 해지")
    public ResponseEntity<AccountDeleteRes> accountDelete(@RequestBody AccountDeleteReq accountDeleteReq) {
        AccountDeleteRes accountDeleteRes = accountService.accountDelete(accountDeleteReq);
        return ResponseEntity.ok(accountDeleteRes);
    }

    @PostMapping("/account/password")
    @Operation(summary = "계좌 비밀번호 확인")
    public ResponseEntity<AccountPasswordCheckRes> accountPasswordCheck(@RequestBody AccountPasswordCheckReq accountPasswordCheckReq) {
        AccountPasswordCheckRes accountPasswordCheckRes = accountService.accountPasswordCheck(accountPasswordCheckReq);
        return ResponseEntity.ok(accountPasswordCheckRes);
    }
}
