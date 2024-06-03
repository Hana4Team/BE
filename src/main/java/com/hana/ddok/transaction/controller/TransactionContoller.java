package com.hana.ddok.transaction.controller;

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

    @PostMapping("transaction/deposit")
    public ResponseEntity<TransactionDepositSaveRes> transactionDepositSave(@RequestBody TransactionDepositSaveReq transactionDepositSaveReq) {
        TransactionDepositSaveRes transactionDepositSaveRes = transactionService.transactionDepositSave(transactionDepositSaveReq);
        return ResponseEntity.ok(transactionDepositSaveRes);
    }

    @PostMapping("transaction/moneybox")
    public ResponseEntity<TransactionMoneyboxSaveRes> transactionMoneyboxSave(@RequestBody TransactionMoneyboxSaveReq transactionMoneyboxSaveReq, @AuthenticationPrincipal UsersDetails usersDetails) {
        TransactionMoneyboxSaveRes transactionMoneyboxSaveRes = transactionService.transactionMoneyboxSave(transactionMoneyboxSaveReq, usersDetails.getUsername());
        return ResponseEntity.ok(transactionMoneyboxSaveRes);
    }
}
