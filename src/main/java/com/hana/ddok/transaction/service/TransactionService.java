package com.hana.ddok.transaction.service;

import com.hana.ddok.account.domain.Account;
import com.hana.ddok.account.exception.AccountChargeDenied;
import com.hana.ddok.account.exception.AccountNotFound;
import com.hana.ddok.account.repository.AccountRepository;
import com.hana.ddok.moneybox.domain.Moneybox;
import com.hana.ddok.moneybox.exception.MoneyboxNotFound;
import com.hana.ddok.moneybox.repository.MoneyboxRepository;
import com.hana.ddok.transaction.domain.Transaction;
import com.hana.ddok.transaction.dto.AccountChargeReq;
import com.hana.ddok.transaction.dto.AccountChargeRes;
import com.hana.ddok.transaction.dto.TransactionSaveReq;
import com.hana.ddok.transaction.dto.TransactionSaveRes;
import com.hana.ddok.transaction.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final MoneyboxRepository moneyboxRepository;

    @Transactional
    public AccountChargeRes accountCharge(AccountChargeReq accountChargeReq) {
        Account recipentAccount = accountRepository.findByAccountNumber(accountChargeReq.recipientAccount())
                .orElseThrow(() -> new AccountNotFound());

        // 입출금 계좌만 충전 가능
        if (recipentAccount.getProducts().getType() != 1) {
            throw new AccountChargeDenied();
        }

        recipentAccount.updateBalance(accountChargeReq.amount());
        Transaction transaction = transactionRepository.save(accountChargeReq.toEntity(recipentAccount));
        return new AccountChargeRes(transaction);
    }

    @Transactional
    public TransactionSaveRes transactionSave(TransactionSaveReq transactionSaveReq) {
        Account senderAccount = accountRepository.findByAccountNumber(transactionSaveReq.senderAccount())
                .orElseThrow(() -> new AccountNotFound());
        Account recipentAccount = accountRepository.findByAccountNumber(transactionSaveReq.recipientAccount())
                .orElseThrow(() -> new AccountNotFound());

        senderAccount.updateBalance(-transactionSaveReq.amount());
        recipentAccount.updateBalance(transactionSaveReq.amount());

        // 받는 분이 머니박스일 경우, 파킹 잔액 변경
        if (recipentAccount.getProducts().getType() == 4) {
            Moneybox moneybox = moneyboxRepository.findByAccount(recipentAccount)
                    .orElseThrow(() -> new MoneyboxNotFound());
            moneybox.updateParkingBalance(transactionSaveReq.amount());
        }

        Transaction transaction = transactionRepository.save(transactionSaveReq.toEntity(senderAccount, recipentAccount));
        return new TransactionSaveRes(transaction);
    }
}
