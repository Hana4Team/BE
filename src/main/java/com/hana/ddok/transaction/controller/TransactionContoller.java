package com.hana.ddok.transaction.controller;

import com.hana.ddok.transaction.domain.Transaction;
import com.hana.ddok.transaction.dto.*;
import com.hana.ddok.transaction.service.TransactionService;
import com.hana.ddok.users.domain.UsersDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping("transaction/withdrawal")
    public ResponseEntity<TransactionWithdrawalSaveRes> transactionWithdrawalSave(@RequestBody TransactionWithdrawalSaveReq transactionWithdrawalSaveReq) {
        TransactionWithdrawalSaveRes transactionWithdrawalSaveRes = transactionService.transactionWithdrawalSave(transactionWithdrawalSaveReq);
        return ResponseEntity.ok(transactionWithdrawalSaveRes);
    }

    @PostMapping("transaction/moneybox")
    public ResponseEntity<TransactionMoneyboxSaveRes> transactionMoneyboxSave(@RequestBody TransactionMoneyboxSaveReq transactionMoneyboxSaveReq, @AuthenticationPrincipal UsersDetails usersDetails) {
        TransactionMoneyboxSaveRes transactionMoneyboxSaveRes = transactionService.transactionMoneyboxSave(transactionMoneyboxSaveReq, usersDetails.getUsername());
        return ResponseEntity.ok(transactionMoneyboxSaveRes);
    }
}
