package com.hana.ddok.spend.service;

import com.hana.ddok.account.domain.Account;
import com.hana.ddok.account.exception.AccountNotFound;
import com.hana.ddok.account.exception.AccountSpendDenied;
import com.hana.ddok.account.repository.AccountRepository;
import com.hana.ddok.moneybox.domain.Moneybox;
import com.hana.ddok.moneybox.exception.MoneyboxNotFound;
import com.hana.ddok.moneybox.repository.MoneyboxRepository;
import com.hana.ddok.products.domain.ProductsType;
import com.hana.ddok.spend.domain.Spend;
import com.hana.ddok.spend.domain.SpendType;
import com.hana.ddok.spend.dto.SpendFindAllRes;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class SpendService {
    private final TransactionRepository transactionRepository;
    private final UsersRepository usersRepository;
    private final AccountRepository accountRepository;
    private final SpendRepository spendRepository;

    @Transactional(readOnly = true)
    public SpendFindAllRes spendFindAll(Integer year, Integer month, String phoneNumber) {
        Users users = usersRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new UsersNotFound());

        // 입출금계좌, 머니박스만 소비 존재
        List<Account> spendAccountList = new ArrayList<>();
        accountRepository.findByUsersAndProductsType(users, ProductsType.DEPOSITWITHDRAWAL)
                .ifPresent(spendAccountList::add);
        accountRepository.findByUsersAndProductsType(users, ProductsType.MONEYBOX)
                .ifPresent(spendAccountList::add);

        LocalDateTime startDateTime = LocalDateTime.of(year, month, 1, 0, 0);
        LocalDateTime endDateTime = startDateTime.plusMonths(1).minusDays(1).plusHours(23).plusMinutes(59).plusSeconds(59);

        Integer shopping = calculateSpendByCategory(spendAccountList, SpendType.SHOPPING, startDateTime, endDateTime);
        Integer food = calculateSpendByCategory(spendAccountList, SpendType.FOOD, startDateTime, endDateTime);
        Integer traffic = calculateSpendByCategory(spendAccountList, SpendType.TRAFFIC, startDateTime, endDateTime);
        Integer hospital = calculateSpendByCategory(spendAccountList, SpendType.HOSPITAL, startDateTime, endDateTime);
        Integer fee = calculateSpendByCategory(spendAccountList, SpendType.FEE, startDateTime, endDateTime);
        Integer education = calculateSpendByCategory(spendAccountList, SpendType.EDUCATION, startDateTime, endDateTime);
        Integer leisure = calculateSpendByCategory(spendAccountList, SpendType.LEISURE, startDateTime, endDateTime);
        Integer society = calculateSpendByCategory(spendAccountList, SpendType.SOCIETY, startDateTime, endDateTime);
        Integer daily = calculateSpendByCategory(spendAccountList, SpendType.DAILY, startDateTime, endDateTime);
        Integer overseas = calculateSpendByCategory(spendAccountList, SpendType.OVERSEAS, startDateTime, endDateTime);
        Integer sum = shopping + food + traffic + hospital + fee + education + leisure + society + daily + overseas;
        return new SpendFindAllRes(sum, shopping, food, traffic, hospital, fee, education, leisure, society, daily, overseas);
    }

    @Transactional(readOnly = true)
    public Integer calculateSpendByCategory(List<Account> spendAccountList, SpendType type, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        return spendRepository.findByTransactionSenderAccountInAndTypeAndCreatedAtBetween(spendAccountList, type, startDateTime, endDateTime).stream()
                .mapToInt(spend -> spend.getAmount())
                .sum();
    }
}