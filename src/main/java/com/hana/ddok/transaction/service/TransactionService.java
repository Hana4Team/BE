package com.hana.ddok.transaction.service;

import com.hana.ddok.account.domain.Account;
import com.hana.ddok.account.exception.AccountChargeDenied;
import com.hana.ddok.account.exception.AccountNotFound;
import com.hana.ddok.account.repository.AccountRepository;
import com.hana.ddok.moneybox.domain.Moneybox;
import com.hana.ddok.moneybox.exception.MoneyboxNotFound;
import com.hana.ddok.moneybox.repository.MoneyboxRepository;
import com.hana.ddok.transaction.domain.Transaction;
import com.hana.ddok.transaction.dto.*;
import com.hana.ddok.transaction.repository.TransactionRepository;
import com.hana.ddok.users.domain.Users;
import com.hana.ddok.users.domain.UsersDetails;
import com.hana.ddok.users.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final MoneyboxRepository moneyboxRepository;
    private final UsersRepository usersRepository;

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

    @Transactional
    public TransactionDepositSaveRes transactionDepositSave(TransactionDepositSaveReq transactionDepositSaveReq) {
        Account recipentAccount = accountRepository.findByAccountNumber(transactionDepositSaveReq.recipientAccount())
                .orElseThrow(() -> new AccountNotFound());

        // 입출금 계좌만 충전 가능
        if (recipentAccount.getProducts().getType() != 1) {
            throw new AccountChargeDenied();
        }

        recipentAccount.updateBalance(transactionDepositSaveReq.amount());
        Transaction transaction = transactionRepository.save(transactionDepositSaveReq.toEntity(recipentAccount));
        return new TransactionDepositSaveRes(transaction);
    }

    @Transactional
    public TransactionMoneyboxSaveRes transactionMoneyboxSave(TransactionMoneyboxSaveReq transactionMoneyboxSaveReq, String phoneNumber) {
        Users users = usersRepository.findByPhoneNumber(phoneNumber);
        Account account = accountRepository.findByUsersAndProductsType(users, 4)
                .orElseThrow(() -> new AccountNotFound());

        Moneybox moneybox = moneyboxRepository.findByAccount(account)
                .orElseThrow(() -> new MoneyboxNotFound());

        String senderMoneybox = transactionMoneyboxSaveReq.senderMoneybox();
        Long amount = transactionMoneyboxSaveReq.amount();
        switch (senderMoneybox) {
            case "parking":
                moneybox.updateParkingBalance(-amount);
                break;
            case "expense":
                moneybox.updateExpenseBalance(-amount);
                break;
            case "saving":
                moneybox.updateSavingBalance(-amount);
                break;
        }
        String recipientMoneybox = transactionMoneyboxSaveReq.recipientMoneybox();
        switch (recipientMoneybox) {
            case "parking":
                moneybox.updateParkingBalance(amount);
                break;
            case "expense":
                moneybox.updateExpenseBalance(amount);
                break;
            case "saving":
                moneybox.updateSavingBalance(amount);
                break;
        }

        Transaction transaction = transactionRepository.save(transactionMoneyboxSaveReq.toEntity(account));
        return new TransactionMoneyboxSaveRes(transaction);
    }
}
