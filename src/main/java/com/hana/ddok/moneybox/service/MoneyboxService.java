package com.hana.ddok.moneybox.service;

import com.hana.ddok.account.domain.Account;
import com.hana.ddok.account.exception.AccountNotFound;
import com.hana.ddok.moneybox.dto.MoneyboxFindAllRes;
import com.hana.ddok.moneybox.dto.MoneyboxFindBySavingRes;
import com.hana.ddok.account.repository.AccountRepository;
import com.hana.ddok.moneybox.domain.Moneybox;
import com.hana.ddok.moneybox.exception.MoneyboxNotFound;
import com.hana.ddok.moneybox.repository.MoneyboxRepository;
import com.hana.ddok.products.domain.ProductsType;
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

    @Transactional(readOnly = true)
    public MoneyboxFindAllRes moneyboxFindAll(String phoneNumber) {
        Users users = usersRepository.findByPhoneNumber(phoneNumber);
        Account account = accountRepository.findByUsersAndProductsType(users, ProductsType.MONEYBOX)
                .orElseThrow(() -> new AccountNotFound());
        Moneybox moneybox = moneyboxRepository.findByAccount(account)
                .orElseThrow(() -> new MoneyboxNotFound());
        return new MoneyboxFindAllRes(account, moneybox);
    }

    @Transactional(readOnly = true)
    public MoneyboxFindBySavingRes moneyboxFindBySavingRes(String phoneNumber) {
        Users users = usersRepository.findByPhoneNumber(phoneNumber);
        Account account = accountRepository.findByUsersAndProductsType(users, ProductsType.MONEYBOX)
                .orElseThrow(() -> new AccountNotFound());
        Moneybox moneybox = moneyboxRepository.findByAccount(account)
                .orElseThrow(() -> new MoneyboxNotFound());
        return new MoneyboxFindBySavingRes(moneybox);
    }
}