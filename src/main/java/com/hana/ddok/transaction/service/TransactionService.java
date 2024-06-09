package com.hana.ddok.transaction.service;

import com.hana.ddok.account.domain.Account;
import com.hana.ddok.account.exception.AccountNotFound;
import com.hana.ddok.account.exception.AccountSpendDenied;
import com.hana.ddok.account.repository.AccountRepository;
import com.hana.ddok.depositsaving.domain.Depositsaving;
import com.hana.ddok.depositsaving.exception.DepositsavingNotFound;
import com.hana.ddok.depositsaving.repository.DepositsavingRepository;
import com.hana.ddok.moneybox.domain.Moneybox;
import com.hana.ddok.moneybox.domain.MoneyboxType;
import com.hana.ddok.moneybox.exception.MoneyboxNotFound;
import com.hana.ddok.moneybox.repository.MoneyboxRepository;
import com.hana.ddok.products.domain.ProductsType;
import com.hana.ddok.spend.domain.Spend;
import com.hana.ddok.spend.repository.SpendRepository;
import com.hana.ddok.transaction.domain.Transaction;
import com.hana.ddok.transaction.domain.TransactionType;
import com.hana.ddok.transaction.dto.*;
import com.hana.ddok.transaction.repository.TransactionRepository;
import com.hana.ddok.users.domain.Users;
import com.hana.ddok.users.exception.UsersNotFound;
import com.hana.ddok.users.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.*;
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

            // 머니박스가 첫 충전일 경우, 2단계 스케줄링 시작
            Boolean isCharged = transactionRepository.existsByRecipientAccount(recipentAccount);
            if (isCharged == false) {
                // TODO : 2단계 스케줄링
            }
        }

        Transaction transaction = transactionRepository.save(transactionSaveReq.toEntity(senderAccount, recipentAccount));
        return new TransactionSaveRes(transaction);
    }

    @Transactional(readOnly = true)
    public TransactionFindAllRes transactionFindAll(Long accountId, Integer year, Integer month) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFound());

        List<TransactionType> typeList = Arrays.asList(TransactionType.REMITTANCE, TransactionType.SPEND, TransactionType.INTEREST);
        LocalDateTime startDateTime = LocalDateTime.of(year, month, 1, 0, 0);
        LocalDateTime endDateTime = startDateTime.plusMonths(1).minusDays(1).plusHours(23).plusMinutes(59).plusSeconds(59);

        List<Transaction> senderTransactionList =  transactionRepository.findAllByTypeInAndSenderAccountAndCreatedAtBetween(typeList, account, startDateTime, endDateTime);
        List<Transaction> recipientTransactionList =  transactionRepository.findAllByTypeInAndRecipientAccountAndCreatedAtBetween(typeList, account, startDateTime, endDateTime);

        // 시간순 정렬
        List<TransactionFindByIdRes> transactionFindByIdResList = Stream.concat(
                        senderTransactionList.stream().map(transaction -> new TransactionFindByIdRes(transaction, true)),
                        recipientTransactionList.stream().map(transaction -> new TransactionFindByIdRes(transaction, false)))
                .sorted(Comparator.comparing(TransactionFindByIdRes::dateTime))
                .collect(Collectors.toList());

        return new TransactionFindAllRes(account, transactionFindByIdResList);
    }

    @Transactional
    public TransactionMoneyboxSaveRes transactionMoneyboxSave(TransactionMoneyboxSaveReq transactionMoneyboxSaveReq, String phoneNumber) {
        Users users = usersRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new UsersNotFound());
        Account account = accountRepository.findByUsersAndProductsTypeAndIsDeletedFalse(users, ProductsType.MONEYBOX)
                .orElseThrow(() -> new AccountNotFound());

        Moneybox moneybox = moneyboxRepository.findByAccount(account)
                .orElseThrow(() -> new MoneyboxNotFound());

        Long amount = transactionMoneyboxSaveReq.amount().longValue();
        MoneyboxType senderMoneyboxType = transactionMoneyboxSaveReq.senderMoneybox();
        switch (senderMoneyboxType) {
            case PARKING:
                moneybox.updateParkingBalance(-amount);
                break;
            case EXPENSE:
                moneybox.updateExpenseBalance(-amount);
                break;
            case SAVING:
                moneybox.updateSavingBalance(-amount);
                break;
        }
        MoneyboxType recipientMoneyboxType = transactionMoneyboxSaveReq.recipientMoneybox();
        switch (recipientMoneyboxType) {
            case PARKING:
                moneybox.updateParkingBalance(amount);
                break;
            case EXPENSE:
                moneybox.updateExpenseBalance(amount);
                break;
            case SAVING:
                moneybox.updateSavingBalance(amount);
                break;
        }

        Transaction transaction = transactionRepository.save(transactionMoneyboxSaveReq.toEntity(account));
        return new TransactionMoneyboxSaveRes(transaction);
    }

    @Transactional(readOnly = true)
    public TransactionMoneyboxFindAllRes transactionMoneyboxFindAll(MoneyboxType type, Integer year, Integer month, String phoneNumber) {
        Users users = usersRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new UsersNotFound());
        Account account = accountRepository.findByUsersAndProductsTypeAndIsDeletedFalse(users, ProductsType.MONEYBOX)
                .orElseThrow(() -> new AccountNotFound());

        List<TransactionType> typeList = Collections.singletonList(TransactionType.MONEYBOX);
        LocalDateTime startDateTime = LocalDateTime.of(year, month, 1, 0, 0);
        LocalDateTime endDateTime = startDateTime.plusMonths(1).minusDays(1).plusHours(23).plusMinutes(59).plusSeconds(59);

        List<Transaction> senderTransactionList =  transactionRepository.findAllByTypeInAndSenderAccountAndCreatedAtBetweenAndSenderTitleContaining(typeList, account, startDateTime, endDateTime, type.getKorean()+"->");
        List<Transaction> recipientTransactionList =  transactionRepository.findAllByTypeInAndRecipientAccountAndCreatedAtBetweenAndRecipientTitleContaining(typeList, account, startDateTime, endDateTime, "->"+type.getKorean());

        // 파킹 :  계좌 간 송금 내역 포함
        if (type == MoneyboxType.PARKING) {
            List<TransactionType> parkingTypeList = Arrays.asList(TransactionType.REMITTANCE, TransactionType.SPEND, TransactionType.INTEREST);
            senderTransactionList.addAll(
                    transactionRepository.findAllByTypeInAndSenderAccountAndCreatedAtBetween(parkingTypeList, account, startDateTime, endDateTime)
            );
            recipientTransactionList.addAll(
                    transactionRepository.findAllByTypeInAndRecipientAccountAndCreatedAtBetween(parkingTypeList, account, startDateTime, endDateTime)
            );
        }

        // 시간순 정렬
        List<TransactionMoneyboxFindByIdRes> transactionMoneyboxFindByIdResList = Stream.concat(
                        senderTransactionList.stream().map(transaction -> new TransactionMoneyboxFindByIdRes(transaction, true)),
                        recipientTransactionList.stream().map(transaction -> new TransactionMoneyboxFindByIdRes(transaction, false)))
                .sorted(Comparator.comparing(TransactionMoneyboxFindByIdRes::dateTime))
                .collect(Collectors.toList());

        return new TransactionMoneyboxFindAllRes(account, transactionMoneyboxFindByIdResList);
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

    @Transactional(readOnly = true)
    public TransactionSaving100CheckRes transactionSaving100Check(String phoneNumber) {
        Users users = usersRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new UsersNotFound());
        Account account = accountRepository.findByUsersAndProductsTypeAndIsDeletedFalse(users, ProductsType.SAVING100)
                .orElseThrow(() -> new AccountNotFound());

        LocalDateTime startDateTime = account.getCreatedAt();
        // 성공일수 : 개설일자 ~ 현재 의 송금 개수 확인
        Integer successCount = transactionRepository.countByRecipientAccountAndCreatedAtBetween(account, startDateTime, LocalDateTime.now());
        // 실패일수 : 개설일자 ~ 현재 의 송금 없는 개수 확인
        Period period = Period.between(startDateTime.toLocalDate(), LocalDate.now());
        Integer days = period.getYears() * 365 + period.getMonths() * 30 + period.getDays();
        Integer failCount = days + 1 - successCount;    // 시작일이 1일차

        return new TransactionSaving100CheckRes(successCount, failCount);
    }
}
