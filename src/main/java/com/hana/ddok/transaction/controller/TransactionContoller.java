package com.hana.ddok.transaction.controller;

import com.hana.ddok.transaction.dto.TransactionFindAllRes;
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

    @PostMapping("transaction")
    public ResponseEntity<TransactionSaveRes> transactionSave(@RequestBody TransactionSaveReq transactionSaveReq) {
        TransactionSaveRes transactionSaveRes = transactionService.transactionSave(transactionSaveReq);
        return ResponseEntity.ok(transactionSaveRes);
    }

    @GetMapping("/transaction/{accountId}")
    public ResponseEntity<List<TransactionFindAllRes>> transactionFindAll(@PathVariable Long accountId, @RequestParam Integer year, @RequestParam Integer month) {
        List<TransactionFindAllRes> transactionFindAllResList = transactionService.transactionFindAll(accountId, year, month);
        return ResponseEntity.ok(transactionFindAllResList);
    }

    @PostMapping("transaction/moneybox")
    public ResponseEntity<TransactionMoneyboxSaveRes> transactionMoneyboxSave(@RequestBody TransactionMoneyboxSaveReq transactionMoneyboxSaveReq, @AuthenticationPrincipal UsersDetails usersDetails) {
        TransactionMoneyboxSaveRes transactionMoneyboxSaveRes = transactionService.transactionMoneyboxSave(transactionMoneyboxSaveReq, usersDetails.getUsername());
        return ResponseEntity.ok(transactionMoneyboxSaveRes);
    }

    @PostMapping("transaction/spend")
    public ResponseEntity<TransactionSpendSaveRes> transactionSpendSave(@RequestBody TransactionSpendSaveReq transactionSpendSaveReq) {
        TransactionSpendSaveRes transactionSpendSaveRes = transactionService.transactionSpendSave(transactionSpendSaveReq);
        return ResponseEntity.ok(transactionSpendSaveRes);
    }
}
