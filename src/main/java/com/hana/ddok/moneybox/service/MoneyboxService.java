package com.hana.ddok.moneybox.service;

import com.hana.ddok.account.domain.Account;
import com.hana.ddok.account.exception.AccountNotFound;
import com.hana.ddok.moneybox.dto.MoneyboxFindAllRes;
import com.hana.ddok.moneybox.dto.MoneyboxFindBySavingRes;
import com.hana.ddok.moneybox.dto.MoneyboxSaveReq;
import com.hana.ddok.account.repository.AccountRepository;
import com.hana.ddok.moneybox.domain.Moneybox;
import com.hana.ddok.moneybox.dto.MoneyboxSaveRes;
import com.hana.ddok.moneybox.exception.MoneyboxNotFound;
import com.hana.ddok.moneybox.repository.MoneyboxRepository;
import com.hana.ddok.products.domain.Products;
import com.hana.ddok.products.exception.ProductsNotFound;
import com.hana.ddok.products.repository.ProductsRepository;
import com.hana.ddok.users.domain.Users;
import com.hana.ddok.users.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MoneyboxService {
    private final AccountRepository accountRepository;
    private final ProductsRepository productsRepository;
    private final UsersRepository usersRepository;
    private final MoneyboxRepository moneyboxRepository;
    @Transactional
    public MoneyboxSaveRes moneyboxSave(MoneyboxSaveReq moneyboxSaveReq, String phoneNumber) {
        Users users = usersRepository.findByPhoneNumber(phoneNumber);
        Products products = productsRepository.findById(moneyboxSaveReq.productsId())
                .orElseThrow(() -> new ProductsNotFound());
        Account account = accountRepository.save(moneyboxSaveReq.toEntity(users, products));
        Moneybox moneybox = moneyboxRepository.save(Moneybox.builder()
                .parkingBalance(0L)
                .expenseBalance(0L)
                .savingBalance(0L)
                .expenseTotal(0L)
                .isCharged(false)
                .account(account)
                .build()
        );
        return new MoneyboxSaveRes(account, moneybox);
    }

    @Transactional(readOnly = true)
    public MoneyboxFindAllRes moneyboxFindAll(String phoneNumber) {
        Users users = usersRepository.findByPhoneNumber(phoneNumber);
        Account account = accountRepository.findByUsersAndProductsType(users, 4)
                .orElseThrow(() -> new AccountNotFound());
        Moneybox moneybox = moneyboxRepository.findByAccount(account)
                .orElseThrow(() -> new MoneyboxNotFound());
        return new MoneyboxFindAllRes(account, moneybox);
    }

    @Transactional(readOnly = true)
    public MoneyboxFindBySavingRes moneyboxFindBySavingRes(String phoneNumber) {
        Users users = usersRepository.findByPhoneNumber(phoneNumber);
        Account account = accountRepository.findByUsersAndProductsType(users, 4)
                .orElseThrow(() -> new AccountNotFound());
        Moneybox moneybox = moneyboxRepository.findByAccount(account)
                .orElseThrow(() -> new MoneyboxNotFound());
        return new MoneyboxFindBySavingRes(moneybox);
    }
}