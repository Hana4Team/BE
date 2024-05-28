package com.hana.ddok.account.controller;

import com.hana.ddok.account.dto.AccountFindAllRes;
import com.hana.ddok.account.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/api/v1")
@RestController
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @GetMapping("/account")
    public List<AccountFindAllRes> accountFindAll() {
        List<AccountFindAllRes> accountFindAllResList = accountService.accountFindAll();
        return accountFindAllResList;
    }
}
