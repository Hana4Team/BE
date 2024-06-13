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
    private final UsersRepository usersRepository;
    private final AccountRepository accountRepository;
    private final SpendRepository spendRepository;

    @Transactional(readOnly = true)
    public SpendFindAllRes spendFindAll(Integer year, Integer month, String phoneNumber) {
        Users users = usersRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new UsersNotFound());

        // 입출금계좌, 머니박스만 소비 존재
        // 만약 해지된 계좌여도, 소비는 확인해야함 : isDeleted=true/false 모두 포함
        List<Account> spendAccountList = accountRepository.findAllByUsersAndProductsTypeIn(users,
                List.of(ProductsType.DEPOSITWITHDRAWAL, ProductsType.MONEYBOX)
        );

        LocalDateTime startDateTime = LocalDateTime.of(year, month, 1, 0, 0);
        LocalDateTime endDateTime = startDateTime.plusMonths(1).minusDays(1).plusHours(23).plusMinutes(59).plusSeconds(59);

        Long shopping = calculateSpendByType(spendAccountList, SpendType.SHOPPING, startDateTime, endDateTime);
        Long food = calculateSpendByType(spendAccountList, SpendType.FOOD, startDateTime, endDateTime);
        Long traffic = calculateSpendByType(spendAccountList, SpendType.TRAFFIC, startDateTime, endDateTime);
        Long hospital = calculateSpendByType(spendAccountList, SpendType.HOSPITAL, startDateTime, endDateTime);
        Long fee = calculateSpendByType(spendAccountList, SpendType.FEE, startDateTime, endDateTime);
        Long education = calculateSpendByType(spendAccountList, SpendType.EDUCATION, startDateTime, endDateTime);
        Long leisure = calculateSpendByType(spendAccountList, SpendType.LEISURE, startDateTime, endDateTime);
        Long society = calculateSpendByType(spendAccountList, SpendType.SOCIETY, startDateTime, endDateTime);
        Long daily = calculateSpendByType(spendAccountList, SpendType.DAILY, startDateTime, endDateTime);
        Long overseas = calculateSpendByType(spendAccountList, SpendType.OVERSEAS, startDateTime, endDateTime);
        Long sum = shopping + food + traffic + hospital + fee + education + leisure + society + daily + overseas;

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
        spendFindByTypeResList.sort(Comparator.comparingLong(s -> ((SpendFindByTypeRes)s).amount()).reversed());

        return new SpendFindAllRes(sum, spendFindByTypeResList);
    }

    @Transactional(readOnly = true)
    public Long calculateSpendByType(List<Account> spendAccountList, SpendType type, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        return spendRepository.findByTransactionSenderAccountInAndTypeAndCreatedAtBetween(spendAccountList, type, startDateTime, endDateTime).stream()
                .mapToLong(spend -> spend.getAmount())
                .sum();
    }
}