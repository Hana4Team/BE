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
import com.hana.ddok.spend.dto.SpendFindByTypeRes;
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
import java.util.*;
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
        List<Account> spendAccountList = accountRepository.findAllByUsersAndProductsTypeIn(users,
                List.of(ProductsType.DEPOSITWITHDRAWAL, ProductsType.MONEYBOX)
        );

        LocalDateTime startDateTime = LocalDateTime.of(year, month, 1, 0, 0);
        LocalDateTime endDateTime = startDateTime.plusMonths(1).minusDays(1).plusHours(23).plusMinutes(59).plusSeconds(59);

        Integer shopping = calculateSpendByType(spendAccountList, SpendType.SHOPPING, startDateTime, endDateTime);
        Integer food = calculateSpendByType(spendAccountList, SpendType.FOOD, startDateTime, endDateTime);
        Integer traffic = calculateSpendByType(spendAccountList, SpendType.TRAFFIC, startDateTime, endDateTime);
        Integer hospital = calculateSpendByType(spendAccountList, SpendType.HOSPITAL, startDateTime, endDateTime);
        Integer fee = calculateSpendByType(spendAccountList, SpendType.FEE, startDateTime, endDateTime);
        Integer education = calculateSpendByType(spendAccountList, SpendType.EDUCATION, startDateTime, endDateTime);
        Integer leisure = calculateSpendByType(spendAccountList, SpendType.LEISURE, startDateTime, endDateTime);
        Integer society = calculateSpendByType(spendAccountList, SpendType.SOCIETY, startDateTime, endDateTime);
        Integer daily = calculateSpendByType(spendAccountList, SpendType.DAILY, startDateTime, endDateTime);
        Integer overseas = calculateSpendByType(spendAccountList, SpendType.OVERSEAS, startDateTime, endDateTime);
        Integer sum = shopping + food + traffic + hospital + fee + education + leisure + society + daily + overseas;

        List<SpendFindByTypeRes> spendFindByTypeResList = new ArrayList<>(
                List.of(
                        new SpendFindByTypeRes(SpendType.SHOPPING, shopping),
                        new SpendFindByTypeRes(SpendType.FOOD, food),
                        new SpendFindByTypeRes(SpendType.TRAFFIC, traffic),
                        new SpendFindByTypeRes(SpendType.HOSPITAL, hospital),
                        new SpendFindByTypeRes(SpendType.FEE, fee),
                        new SpendFindByTypeRes(SpendType.EDUCATION, education),
                        new SpendFindByTypeRes(SpendType.LEISURE, leisure),
                        new SpendFindByTypeRes(SpendType.SOCIETY, society),
                        new SpendFindByTypeRes(SpendType.DAILY, daily),
                        new SpendFindByTypeRes(SpendType.OVERSEAS, overseas)
                )
        );
        // 내림차순 정렬
        spendFindByTypeResList.sort(Comparator.comparingInt(s -> ((SpendFindByTypeRes)s).amount()).reversed());

        return new SpendFindAllRes(sum, spendFindByTypeResList);
    }

    @Transactional(readOnly = true)
    public Integer calculateSpendByType(List<Account> spendAccountList, SpendType type, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        return spendRepository.findByTransactionSenderAccountInAndTypeAndCreatedAtBetween(spendAccountList, type, startDateTime, endDateTime).stream()
                .mapToInt(spend -> spend.getAmount())
                .sum();
    }
}