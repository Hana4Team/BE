package com.hana.ddok.transaction.service;

import com.hana.ddok.account.domain.Account;
import com.hana.ddok.account.exception.AccountNotFound;
import com.hana.ddok.account.exception.AccountSpendDenied;
import com.hana.ddok.account.repository.AccountRepository;
import com.hana.ddok.moneybox.domain.Moneybox;
import com.hana.ddok.moneybox.domain.MoneyboxType;
import com.hana.ddok.moneybox.exception.MoneyboxNotFound;
import com.hana.ddok.moneybox.repository.MoneyboxRepository;
import com.hana.ddok.products.domain.ProductsType;
import com.hana.ddok.spend.domain.Spend;
import com.hana.ddok.spend.domain.SpendType;
import com.hana.ddok.spend.repository.SpendRepository;
import com.hana.ddok.transaction.domain.Transaction;
import com.hana.ddok.transaction.domain.TransactionType;
import com.hana.ddok.transaction.dto.*;
import com.hana.ddok.transaction.exception.TransactionAccessDenied;
import com.hana.ddok.transaction.exception.TransactionAmountInvalid;
import com.hana.ddok.transaction.repository.TransactionRepository;
import com.hana.ddok.transaction.scheduler.TransactionStep2SchedulerService;
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
        Account recipientAccount = accountRepository.findByAccountNumber(transactionSaveReq.recipientAccount())
                .orElseThrow(() -> new AccountNotFound());

        Long amount = transactionSaveReq.amount();
        if (amount <= 0) {
            throw new TransactionAmountInvalid();
        }

        senderAccount.updateBalance(-amount);
        recipientAccount.updateBalance(amount);

        // 계좌가 머니박스일 경우, 파킹 잔액 변경
        updateMoneyboxParkingBalance(senderAccount, -amount);
        updateMoneyboxParkingBalance(recipientAccount, amount);

       Users users = recipientAccount.getUsers();

//        // 머니박스가 첫 충전일 경우, 과소비 지수 계산 및 2단계 스케줄링 시작
//        if (recipientAccount.getProducts().getType() == ProductsType.MONEYBOX) {
//            boolean isFirstCharge = !transactionRepository.existsByRecipientAccount(recipientAccount);
//            if (isFirstCharge) {
//
//                step2SchedulerService.scheduleTaskForUser(users.getUsersId(),
//                        () -> {
//                            Transaction firstTransaction = transactionRepository.findFirstByRecipientAccountOrderByCreatedAt(recipientAccount)
//                                    .orElseThrow(() -> new TransactionNotFound());
//                            Long salary = firstTransaction.getAmount();
//                            Moneybox moneybox = moneyboxRepository.findByAccount(recipientAccount)
//                                    .orElseThrow(() -> new MoneyboxNotFound());
//                            Long savingBalance = moneybox.getSavingBalance();
//                            double wasteIndex = (salary - savingBalance) / (double) salary;
//                            if (wasteIndex >= 1.0) {
//                                // 심각한 과소비
//                                // 2단계 스케줄링 시작
//                            } else if (wasteIndex >= 0.7 && wasteIndex < 1.0) {
//                                // 과소비
//                                // 2단계 스케줄링 시작
//                            } else if (wasteIndex >= 0.6 && wasteIndex < 0.7) {
//                                // 적정 소비
//                                // 2단계 스케줄링 시작
//                                recipientAccount.updateInterest(recipientAccount.getProducts().getInterest2());
//                            } else if (wasteIndex < 0.5) {
//                                // 알뜰 소비
//                                // 2단계 스케줄링 시작
//                                recipientAccount.updateInterest(recipientAccount.getProducts().getInterest2());
//                            }
//                        }, 30 * 24 * 60 * 60 * 1000
//                );
//
//            }

//        }

        Transaction transaction = transactionRepository.save(transactionSaveReq.toEntity(senderAccount, recipientAccount));
        return new TransactionSaveRes(transaction);
    }


    private void updateMoneyboxParkingBalance(Account account, Long amount) {
        if (account.getProducts().getType() == ProductsType.MONEYBOX) {
            Moneybox moneybox = moneyboxRepository.findByAccount(account)
                    .orElseThrow(() -> new MoneyboxNotFound());
            moneybox.updateParkingBalance(amount);
        }
    }
    @Transactional(readOnly = true)
    public TransactionFindAllRes transactionFindAll(Long accountId, Integer year, Integer month, String phoneNumber) {
        Users users = usersRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new UsersNotFound());
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFound());
        if (!Objects.equals(users, account.getUsers())) {
            throw new TransactionAccessDenied();
        }

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

        Long amount = transactionMoneyboxSaveReq.amount();
        if (amount <= 0) {
            throw new TransactionAmountInvalid();
        }

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

        LocalDateTime startDateTime = LocalDateTime.of(year, month, 1, 0, 0);
        LocalDateTime endDateTime = startDateTime.plusMonths(1).minusDays(1).plusHours(23).plusMinutes(59).plusSeconds(59);

        // 머니박스 간 송금 : title로 확인
        List<TransactionType> typeList = Collections.singletonList(TransactionType.MONEYBOX);
        List<Transaction> senderTransactionList =  transactionRepository.findAllByTypeInAndSenderAccountAndCreatedAtBetweenAndSenderTitleContaining(typeList, account, startDateTime, endDateTime, type.getKorean()+"->");
        List<Transaction> recipientTransactionList =  transactionRepository.findAllByTypeInAndRecipientAccountAndCreatedAtBetweenAndRecipientTitleContaining(typeList, account, startDateTime, endDateTime, "->"+type.getKorean());

        // 소비 : 지출 포함
        if (type == MoneyboxType.EXPENSE) {
            typeList = Collections.singletonList(TransactionType.SPEND);
            senderTransactionList.addAll(
                    transactionRepository.findAllByTypeInAndSenderAccountAndCreatedAtBetween(typeList, account, startDateTime, endDateTime)
            );
        }
        // 파킹 :  계좌 간 송금 내역 포함
        else if (type == MoneyboxType.PARKING) {
            typeList = Arrays.asList(TransactionType.REMITTANCE, TransactionType.INTEREST);
            senderTransactionList.addAll(
                    transactionRepository.findAllByTypeInAndSenderAccountAndCreatedAtBetween(typeList, account, startDateTime, endDateTime)
            );
            recipientTransactionList.addAll(
                    transactionRepository.findAllByTypeInAndRecipientAccountAndCreatedAtBetween(typeList, account, startDateTime, endDateTime)
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
    public TransactionSpendSaveRes transactionSpendSave(TransactionSpendSaveReq transactionSpendSaveReq, String phoneNumber) {
        Users users = usersRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new UsersNotFound());
        ProductsType type = transactionSpendSaveReq.senderAccountType();
        Account account = accountRepository.findByUsersAndProductsTypeAndIsDeletedFalse(users, transactionSpendSaveReq.senderAccountType())
                .orElseThrow(() -> new AccountNotFound());

        Long amount = transactionSpendSaveReq.amount();
        if (amount <= 0) {
            throw new TransactionAmountInvalid();
        }

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

        Long amount = transactionInterestSaveReq.amount();
        if (amount <= 0) {
            throw new TransactionAmountInvalid();
        }

        recipentAccount.updateBalance(amount);
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

    @Transactional
    public void generateDummyTransaction(Users users) {
        String phoneNumber = users.getPhoneNumber();
        transactionSpendSave(new TransactionSpendSaveReq(50000L, "무신사", ProductsType.DEPOSITWITHDRAWAL, SpendType.SHOPPING), phoneNumber);
        transactionSpendSave(new TransactionSpendSaveReq(300000L, "신세계백화점", ProductsType.DEPOSITWITHDRAWAL, SpendType.SHOPPING), phoneNumber);
        transactionSpendSave(new TransactionSpendSaveReq(4500L, "스타벅스", ProductsType.DEPOSITWITHDRAWAL, SpendType.FOOD), phoneNumber);
        transactionSpendSave(new TransactionSpendSaveReq(8000L, "맥도날드", ProductsType.DEPOSITWITHDRAWAL, SpendType.FOOD), phoneNumber);
        transactionSpendSave(new TransactionSpendSaveReq(1200L, "시내버스", ProductsType.DEPOSITWITHDRAWAL, SpendType.TRAFFIC), phoneNumber);
        transactionSpendSave(new TransactionSpendSaveReq(1300L, "지하철", ProductsType.DEPOSITWITHDRAWAL, SpendType.TRAFFIC), phoneNumber);
        transactionSpendSave(new TransactionSpendSaveReq(23000L, "김안과의원", ProductsType.DEPOSITWITHDRAWAL, SpendType.HOSPITAL), phoneNumber);
        transactionSpendSave(new TransactionSpendSaveReq(23000L, "가스비", ProductsType.DEPOSITWITHDRAWAL, SpendType.FEE), phoneNumber);
        transactionSpendSave(new TransactionSpendSaveReq(300000L, "파고다어학원", ProductsType.DEPOSITWITHDRAWAL, SpendType.EDUCATION), phoneNumber);
        transactionSpendSave(new TransactionSpendSaveReq(20000L, "CGV", ProductsType.DEPOSITWITHDRAWAL, SpendType.LEISURE), phoneNumber);
        transactionSpendSave(new TransactionSpendSaveReq(190000L, "뮤지컬레미제라블", ProductsType.DEPOSITWITHDRAWAL, SpendType.LEISURE), phoneNumber);
        transactionSpendSave(new TransactionSpendSaveReq(4000L, "인생네컷", ProductsType.DEPOSITWITHDRAWAL, SpendType.LEISURE), phoneNumber);
        transactionSpendSave(new TransactionSpendSaveReq(10000L, "유니세프", ProductsType.DEPOSITWITHDRAWAL, SpendType.SOCIETY), phoneNumber);
        transactionSpendSave(new TransactionSpendSaveReq(20000L, "다이소", ProductsType.DEPOSITWITHDRAWAL, SpendType.DAILY), phoneNumber);
        transactionSpendSave(new TransactionSpendSaveReq(2000L, "GS25", ProductsType.DEPOSITWITHDRAWAL, SpendType.DAILY), phoneNumber);
        transactionSpendSave(new TransactionSpendSaveReq(70000L, "올리브영", ProductsType.DEPOSITWITHDRAWAL, SpendType.DAILY), phoneNumber);
        transactionSpendSave(new TransactionSpendSaveReq(80000L, "돈키호테시부야점", ProductsType.DEPOSITWITHDRAWAL, SpendType.OVERSEAS), phoneNumber);
    }
}
