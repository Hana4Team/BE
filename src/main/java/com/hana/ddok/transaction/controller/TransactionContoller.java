package com.hana.ddok.transaction.controller;

import com.hana.ddok.transaction.dto.TransactionSaveReq;
import com.hana.ddok.transaction.dto.TransactionSaveRes;
import com.hana.ddok.transaction.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
}
