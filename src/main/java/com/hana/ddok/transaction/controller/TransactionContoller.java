package com.hana.ddok.transaction.controller;

import com.hana.ddok.moneybox.domain.MoneyboxType;
import com.hana.ddok.transaction.dto.*;
import com.hana.ddok.transaction.service.TransactionService;
import com.hana.ddok.users.domain.UsersDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/v1")
@RestController
@RequiredArgsConstructor
public class TransactionContoller {
    private final TransactionService transactionService;

    @PostMapping("/transaction")
    public ResponseEntity<TransactionSaveRes> transactionSave(@RequestBody TransactionSaveReq transactionSaveReq) {
        TransactionSaveRes transactionSaveRes = transactionService.transactionSave(transactionSaveReq);
        return ResponseEntity.ok(transactionSaveRes);
    }

    @GetMapping("/transaction/{accountId}")
    public ResponseEntity<TransactionFindAllRes> transactionFindAll(@PathVariable Long accountId, @RequestParam Integer year, @RequestParam Integer month) {
        TransactionFindAllRes transactionFindAllRes = transactionService.transactionFindAll(accountId, year, month);
        return ResponseEntity.ok(transactionFindAllRes);
    }

    @PostMapping("/transaction/moneybox")
    public ResponseEntity<TransactionMoneyboxSaveRes> transactionMoneyboxSave(@RequestBody TransactionMoneyboxSaveReq transactionMoneyboxSaveReq, @AuthenticationPrincipal UsersDetails usersDetails) {
        TransactionMoneyboxSaveRes transactionMoneyboxSaveRes = transactionService.transactionMoneyboxSave(transactionMoneyboxSaveReq, usersDetails.getUsername());
        return ResponseEntity.ok(transactionMoneyboxSaveRes);
    }

    @GetMapping("/transaction/moneybox")
    public ResponseEntity<TransactionMoneyboxFindAllRes> transactionMoneyboxFindAll(@RequestParam MoneyboxType type, @RequestParam Integer year, @RequestParam Integer month, @AuthenticationPrincipal UsersDetails usersDetails) {
        TransactionMoneyboxFindAllRes transactionMoneyboxFindAllRes = transactionService.transactionMoneyboxFindAll(type, year, month, usersDetails.getUsername());
        return ResponseEntity.ok(transactionMoneyboxFindAllRes);
    }

    @PostMapping("/transaction/spend")
    public ResponseEntity<TransactionSpendSaveRes> transactionSpendSave(@RequestBody TransactionSpendSaveReq transactionSpendSaveReq) {
        TransactionSpendSaveRes transactionSpendSaveRes = transactionService.transactionSpendSave(transactionSpendSaveReq);
        return ResponseEntity.ok(transactionSpendSaveRes);
    }

    @GetMapping("/transaction/saving100Check")
    public ResponseEntity<TransactionSaving100CheckRes> transactionSaving100Check(@AuthenticationPrincipal UsersDetails usersDetails) {
        TransactionSaving100CheckRes transactionSaving100Check = transactionService.transactionSaving100Check(usersDetails.getUsername());
        return ResponseEntity.ok(transactionSaving100Check);
    }
}
