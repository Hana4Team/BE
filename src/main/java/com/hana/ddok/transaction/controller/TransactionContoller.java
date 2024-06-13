package com.hana.ddok.transaction.controller;

import com.hana.ddok.moneybox.domain.MoneyboxType;
import com.hana.ddok.transaction.dto.*;
import com.hana.ddok.transaction.service.TransactionService;
import com.hana.ddok.users.domain.UsersDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1")
@RestController
@RequiredArgsConstructor
@Tag(name = "Transaction", description = "거래 API")
public class TransactionContoller {
    private final TransactionService transactionService;

    @PostMapping("/transaction")
    @Operation(summary = "계좌 간 송금")
    public ResponseEntity<TransactionSaveRes> transactionSave(@RequestBody TransactionSaveReq transactionSaveReq) {
        TransactionSaveRes transactionSaveRes = transactionService.transactionSave(transactionSaveReq);
        return ResponseEntity.ok(transactionSaveRes);
    }

    @GetMapping("/transaction/{accountId}")
    @Operation(summary = "계좌 간 송금 조회")
    public ResponseEntity<TransactionFindAllRes> transactionFindAll(@PathVariable Long accountId, @RequestParam Integer year, @RequestParam Integer month, @AuthenticationPrincipal UsersDetails usersDetails) {
        TransactionFindAllRes transactionFindAllRes = transactionService.transactionFindAll(accountId, year, month, usersDetails.getUsername());
        return ResponseEntity.ok(transactionFindAllRes);
    }

    @PostMapping("/transaction/moneybox")
    @Operation(summary = "머니박스 간 송금")
    public ResponseEntity<TransactionMoneyboxSaveRes> transactionMoneyboxSave(@RequestBody TransactionMoneyboxSaveReq transactionMoneyboxSaveReq, @AuthenticationPrincipal UsersDetails usersDetails) {
        TransactionMoneyboxSaveRes transactionMoneyboxSaveRes = transactionService.transactionMoneyboxSave(transactionMoneyboxSaveReq, usersDetails.getUsername());
        return ResponseEntity.ok(transactionMoneyboxSaveRes);
    }

    @GetMapping("/transaction/moneybox")
    @Operation(summary = "머니박스 간 송금 조회")
    public ResponseEntity<TransactionMoneyboxFindAllRes> transactionMoneyboxFindAll(@RequestParam MoneyboxType type, @RequestParam Integer year, @RequestParam Integer month, @AuthenticationPrincipal UsersDetails usersDetails) {
        TransactionMoneyboxFindAllRes transactionMoneyboxFindAllRes = transactionService.transactionMoneyboxFindAll(type, year, month, usersDetails.getUsername());
        return ResponseEntity.ok(transactionMoneyboxFindAllRes);
    }

    @PostMapping("/transaction/spend")
    @Operation(summary = "지출")
    public ResponseEntity<TransactionSpendSaveRes> transactionSpendSave(@RequestBody TransactionSpendSaveReq transactionSpendSaveReq, @AuthenticationPrincipal UsersDetails usersDetails) {
        TransactionSpendSaveRes transactionSpendSaveRes = transactionService.transactionSpendSave(transactionSpendSaveReq, usersDetails.getUsername());
        return ResponseEntity.ok(transactionSpendSaveRes);
    }

    @GetMapping("/transaction/saving100Check")
    @Operation(summary = "100일적금 납부일 조회")
    public ResponseEntity<TransactionSaving100CheckRes> transactionSaving100Check(@AuthenticationPrincipal UsersDetails usersDetails) {
        TransactionSaving100CheckRes transactionSaving100Check = transactionService.transactionSaving100Check(usersDetails.getUsername());
        return ResponseEntity.ok(transactionSaving100Check);
    }

    @GetMapping("/transaction/waste")
    @Operation(summary = "과소비패턴 조회")
    public ResponseEntity<TransactionWasteGetRes> transactionWasteGet(@AuthenticationPrincipal UsersDetails usersDetails) {
        TransactionWasteGetRes transactionWasteGetRes = transactionService.getWaste(usersDetails.getUsername());
        return ResponseEntity.ok(transactionWasteGetRes);
    }
}
