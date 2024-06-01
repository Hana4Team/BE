package com.hana.ddok.account.service;

import com.hana.ddok.account.domain.Account;
import com.hana.ddok.account.domain.Moneybox;
import com.hana.ddok.account.dto.*;
import com.hana.ddok.account.exception.AccountNotFound;
import com.hana.ddok.account.exception.MoneyboxNotFound;
import com.hana.ddok.account.repository.AccountRepository;
import com.hana.ddok.moneybox.repository.MoneyboxRepository;
import com.hana.ddok.products.domain.Products;
import com.hana.ddok.products.exception.ProductsNotFound;
import com.hana.ddok.products.repository.ProductsRepository;
import com.hana.ddok.users.domain.Users;
import com.hana.ddok.users.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final ProductsRepository productsRepository;
    private final UsersRepository usersRepository;
    private final MoneyboxRepository moneyboxRepository;

    @Transactional
    public AccountSaveRes accountSave(AccountSaveReq accountSaveReq, String phoneNumber) {
        Users users = usersRepository.findByPhoneNumber(phoneNumber);
        Products products = productsRepository.findById(accountSaveReq.productsId())
                .orElseThrow(() -> new ProductsNotFound());
        Account account = accountRepository.save(accountSaveReq.toEntity(users, products));
        return new AccountSaveRes(account);
    }

    @Transactional(readOnly = true)
    public List<AccountFindAllRes> accountFindAll(String phoneNumber) {
        Users users = usersRepository.findByPhoneNumber(phoneNumber);

        // 일반 계좌 조회
        List<Account> accountList = accountRepository.findAllByUsers(users);
        List<AccountFindAllRes> accountFindAllResList = accountList.stream()
                .filter(account -> account.getType() == 1)
                .map(AccountFindAllRes::new)
                .collect(Collectors.toList());

        // 머니박스 계좌 조회 : 파킹통장(2)로 balance를 합침
        Optional<Account> parkingAccountOptional = accountList.stream()
                .filter(account -> account.getType() == 2)
                .findFirst();
        if (parkingAccountOptional.isPresent()) {
            Account parkingAccount = parkingAccountOptional.get();
            Long moneyboxTotalBalance = accountList.stream()
                    .filter(account -> account.getType() != 1)
                    .mapToLong(Account::getBalance)
                    .sum();
            accountFindAllResList.add(new AccountFindAllRes(parkingAccount, moneyboxTotalBalance));
        }

        return accountFindAllResList;
    }

    @Transactional
    public MoneyboxSaveRes moneyboxSave(MoneyboxSaveReq moneyboxSaveReq, String phoneNumber) {
        Users users = usersRepository.findByPhoneNumber(phoneNumber);
        Products products = productsRepository.findById(moneyboxSaveReq.productsId())
                .orElseThrow(() -> new ProductsNotFound());
        Account account = accountRepository.save(moneyboxSaveReq.toEntity(users, products));
        return new MoneyboxSaveRes(account);
    }

    @Transactional(readOnly = true)
    public MoneyboxFindAllRes moneyboxFindAll(String phoneNumber) {
        Users users = usersRepository.findByPhoneNumber(phoneNumber);
        Account account = accountRepository.findByUsersAndProductsType(users, 3)
                .orElseThrow(() -> new AccountNotFound());
        Moneybox moneybox = moneyboxRepository.findByAccount(account)
                .orElseThrow(() -> new MoneyboxNotFound());
        return new MoneyboxFindAllRes(account, moneybox);
    }

    @Transactional(readOnly = true)
    public MoneyboxFindBySavingRes moneyboxFindBySavingRes(String phoneNumber) {
        Users users = usersRepository.findByPhoneNumber(phoneNumber);
        List<Account> account = accountRepository.findAllByUsersAndType(users, 4);
        if (account.size() != 1) {
            throw new MoneyboxNotFound();
        }
        return new MoneyboxFindBySavingRes(account.get(0));
    }
}