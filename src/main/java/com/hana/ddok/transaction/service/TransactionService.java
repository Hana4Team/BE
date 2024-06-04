package com.hana.ddok.transaction.service;

import com.hana.ddok.account.domain.Account;
import com.hana.ddok.account.exception.AccountNotFound;
import com.hana.ddok.account.exception.AccountWithdrawalDenied;
import com.hana.ddok.account.repository.AccountRepository;
import com.hana.ddok.moneybox.domain.Moneybox;
import com.hana.ddok.moneybox.exception.MoneyboxNotFound;
import com.hana.ddok.moneybox.repository.MoneyboxRepository;
import com.hana.ddok.products.domain.ProductsType;
import com.hana.ddok.transaction.domain.Transaction;
import com.hana.ddok.transaction.dto.*;
import com.hana.ddok.transaction.repository.TransactionRepository;
import com.hana.ddok.users.domain.Users;
import com.hana.ddok.users.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
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

        // 계좌가 머니박스일 경우, 파킹 잔액 변경
        if (senderAccount.getProducts().getType() == ProductsType.MONEYBOX) {
            Moneybox moneybox = moneyboxRepository.findByAccount(senderAccount)
                    .orElseThrow(() -> new MoneyboxNotFound());
            moneybox.updateParkingBalance(-transactionSaveReq.amount());
        }
        else if (recipentAccount.getProducts().getType() == ProductsType.MONEYBOX) {
            Moneybox moneybox = moneyboxRepository.findByAccount(recipentAccount)
                    .orElseThrow(() -> new MoneyboxNotFound());
            moneybox.updateParkingBalance(transactionSaveReq.amount());
        }

        Transaction transaction = transactionRepository.save(transactionSaveReq.toEntity(senderAccount, recipentAccount));
        return new TransactionSaveRes(transaction);
    }

    @Transactional
    public TransactionSpendSaveRes transactionSpendSave(TransactionSpendSaveReq transactionSpendSaveReq) {
        Account account = accountRepository.findByAccountNumber(transactionSpendSaveReq.senderAccount())
                .orElseThrow(() -> new AccountNotFound());

        Long amount = transactionSpendSaveReq.amount();
        ProductsType type = account.getProducts().getType();
        switch (type) {
            case DEPOSITWITHDRAWAL:
                account.updateBalance(-amount);
                break;
            case MONEYBOX:
                account.updateBalance(-amount);
                // 소비공간 잔액 업데이트
                Moneybox moneybox = moneyboxRepository.findByAccount(account)
                        .orElseThrow(() -> new MoneyboxNotFound());
                moneybox.updateExpenseBalance(-amount);
                // 소비합계 업데이트
                moneybox.updateExpenseTotal(amount);
                break;
            default:
                throw new AccountWithdrawalDenied();
        }
        Transaction transaction = transactionRepository.save(transactionSpendSaveReq.toEntity(account));
        return new TransactionSpendSaveRes(transaction);
    }

    @Transactional
    public TransactionMoneyboxSaveRes transactionMoneyboxSave(TransactionMoneyboxSaveReq transactionMoneyboxSaveReq, String phoneNumber) {
        Users users = usersRepository.findByPhoneNumber(phoneNumber);
        Account account = accountRepository.findByUsersAndProductsType(users, ProductsType.MONEYBOX)
                .orElseThrow(() -> new AccountNotFound());

        Moneybox moneybox = moneyboxRepository.findByAccount(account)
                .orElseThrow(() -> new MoneyboxNotFound());

        Long amount = transactionMoneyboxSaveReq.amount();
        String senderMoneybox = transactionMoneyboxSaveReq.senderMoneybox();
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
