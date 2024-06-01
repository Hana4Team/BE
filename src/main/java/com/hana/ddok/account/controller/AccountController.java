package com.hana.ddok.account.controller;

import com.hana.ddok.account.dto.*;
import com.hana.ddok.account.service.AccountService;
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

    @PostMapping("/account")
    public ResponseEntity<AccountSaveRes> accountSave(@RequestBody AccountSaveReq accountSaveReq, @AuthenticationPrincipal UsersDetails usersDetails) {
        AccountSaveRes accountSaveRes = accountService.accountSave(accountSaveReq, usersDetails.getUsername());
        return ResponseEntity.ok(accountSaveRes);
    }

    @GetMapping("/account")
    public ResponseEntity<List<AccountFindAllRes>> accountFindAll(@RequestBody AccountFindAllReq accountFindAllReq, @AuthenticationPrincipal UsersDetails usersDetails) {
        List<AccountFindAllRes> accountFindAllResList = accountService.accountFindAll(accountFindAllReq, usersDetails.getUsername());
        return ResponseEntity.ok(accountFindAllResList);
    }
}
