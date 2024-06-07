package com.hana.ddok.transaction.service;

import com.hana.ddok.account.domain.Account;
import com.hana.ddok.account.exception.AccountNotFound;
import com.hana.ddok.account.exception.AccountSpendDenied;
import com.hana.ddok.account.exception.AccountWithdrawalDenied;
import com.hana.ddok.account.repository.AccountRepository;
import com.hana.ddok.moneybox.domain.Moneybox;
import com.hana.ddok.moneybox.exception.MoneyboxNotFound;
import com.hana.ddok.moneybox.repository.MoneyboxRepository;
import com.hana.ddok.products.domain.ProductsType;
import com.hana.ddok.spend.domain.Spend;
import com.hana.ddok.spend.repository.SpendRepository;
import com.hana.ddok.transaction.domain.Transaction;
import com.hana.ddok.transaction.dto.*;
import com.hana.ddok.transaction.repository.TransactionRepository;
import com.hana.ddok.users.domain.Users;
import com.hana.ddok.users.exception.UsersNotFound;
import com.hana.ddok.users.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final MoneyboxRepository moneyboxRepository;
    private final UsersRepository usersRepository;
    private final SpendRepository spendRepository;

    @Transactional
    public TransactionSaveRes transactionSave(TransactionSaveReq transactionSaveReq) {
        Account senderAccount = accountRepository.findByAccountNumber(transactionSaveReq.senderAccount())
                .orElseThrow(() -> new AccountNotFound());
        Account recipentAccount = accountRepository.findByAccountNumber(transactionSaveReq.recipientAccount())
                .orElseThrow(() -> new AccountNotFound());

        Long amount = transactionSaveReq.amount().longValue();

        senderAccount.updateBalance(-amount);
        recipentAccount.updateBalance(amount);

        // 계좌가 머니박스일 경우, 파킹 잔액 변경
        if (senderAccount.getProducts().getType() == ProductsType.MONEYBOX) {
            Moneybox moneybox = moneyboxRepository.findByAccount(senderAccount)
                    .orElseThrow(() -> new MoneyboxNotFound());
            moneybox.updateParkingBalance(-amount);
        }
        else if (recipentAccount.getProducts().getType() == ProductsType.MONEYBOX) {
            Moneybox moneybox = moneyboxRepository.findByAccount(recipentAccount)
                    .orElseThrow(() -> new MoneyboxNotFound());
            moneybox.updateParkingBalance(amount);
        }

        Transaction transaction = transactionRepository.save(transactionSaveReq.toEntity(senderAccount, recipentAccount));
        return new TransactionSaveRes(transaction);
    }

    @Transactional(readOnly = true)
    public List<TransactionFindAllRes> transactionFindAll(Long accountId, Integer year, Integer month) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFound());

        LocalDateTime startDateTime = LocalDateTime.of(year, month, 1, 0, 0);
        LocalDateTime endDateTime = startDateTime.plusMonths(1).minusDays(1).plusHours(23).plusMinutes(59).plusSeconds(59);

        List<Transaction> senderTransactionList =  transactionRepository.findAllBySenderAccountAndCreatedAtBetween(account, startDateTime, endDateTime);
        List<Transaction> recipientTransactionList =  transactionRepository.findAllByRecipientAccountAndCreatedAtBetween(account, startDateTime, endDateTime);

        // 시간순 정렬
        List<TransactionFindAllRes> transactionFindAllResList = Stream.concat(
                        senderTransactionList.stream().map(transaction -> new TransactionFindAllRes(transaction, true)),
                        recipientTransactionList.stream().map(transaction -> new TransactionFindAllRes(transaction, false)))
                .sorted(Comparator.comparing(TransactionFindAllRes::dateTime))
                .collect(Collectors.toList());

        return transactionFindAllResList;
    }

    @Transactional
    public TransactionMoneyboxSaveRes transactionMoneyboxSave(TransactionMoneyboxSaveReq transactionMoneyboxSaveReq, String phoneNumber) {
        Users users = usersRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new UsersNotFound());
        Account account = accountRepository.findByUsersAndProductsType(users, ProductsType.MONEYBOX)
                .orElseThrow(() -> new AccountNotFound());

        Moneybox moneybox = moneyboxRepository.findByAccount(account)
                .orElseThrow(() -> new MoneyboxNotFound());

        Long amount = transactionMoneyboxSaveReq.amount().longValue();
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

    @Transactional(readOnly = true)
    public List<TransactionMoneyboxFindAllRes> transactionMoneyboxFindAll(Integer year, Integer month, String phoneNumber) {
        Users users = usersRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new UsersNotFound());
        Account account = accountRepository.findByUsersAndProductsType(users, ProductsType.MONEYBOX)
                .orElseThrow(() -> new AccountNotFound());

        LocalDateTime startDateTime = LocalDateTime.of(year, month, 1, 0, 0);
        LocalDateTime endDateTime = startDateTime.plusMonths(1).minusDays(1).plusHours(23).plusMinutes(59).plusSeconds(59);

        List<Transaction> senderTransactionList =  transactionRepository.findAllBySenderAccountAndCreatedAtBetween(account, startDateTime, endDateTime);
        List<Transaction> recipientTransactionList =  transactionRepository.findAllByRecipientAccountAndCreatedAtBetween(account, startDateTime, endDateTime);

        // 시간순 정렬
        List<TransactionMoneyboxFindAllRes> transactionMoneyboxFindAllResList = Stream.concat(
                        senderTransactionList.stream().map(transaction -> new TransactionMoneyboxFindAllRes(transaction, true)),
                        recipientTransactionList.stream().map(transaction -> new TransactionMoneyboxFindAllRes(transaction, false)))
                .sorted(Comparator.comparing(TransactionMoneyboxFindAllRes::dateTime))
                .collect(Collectors.toList());

        return transactionMoneyboxFindAllResList;
    }

    @Transactional
    public TransactionSpendSaveRes transactionSpendSave(TransactionSpendSaveReq transactionSpendSaveReq) {
        Account account = accountRepository.findByAccountNumber(transactionSpendSaveReq.senderAccount())
                .orElseThrow(() -> new AccountNotFound());

        Long amount = transactionSpendSaveReq.amount().longValue();
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
                throw new AccountSpendDenied();
        }
        Transaction transaction = transactionRepository.save(transactionSpendSaveReq.toEntity(account));
        Spend spend = spendRepository.save(transactionSpendSaveReq.toSpend(transaction));

        return new TransactionSpendSaveRes(transaction, spend);
    }

    @Transactional
    public TransactionInterestSaveRes transactionInterestSave(TransactionInterestSaveReq transactionInterestSaveReq) {
        Account recipentAccount = accountRepository.findByAccountNumber(transactionInterestSaveReq.recipientAccount())
                .orElseThrow(() -> new AccountNotFound());

        recipentAccount.updateBalance(transactionInterestSaveReq.amount().longValue());
        Transaction transaction = transactionRepository.save(transactionInterestSaveReq.toEntity(recipentAccount));
        return new TransactionInterestSaveRes(transaction);
    }
}
