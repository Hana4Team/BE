package com.hana.ddok.account.service;

import com.hana.ddok.account.domain.Account;
import com.hana.ddok.account.dto.*;
import com.hana.ddok.account.exception.MoneyboxNotFound;
import com.hana.ddok.account.repository.AccountRepository;
import com.hana.ddok.products.domain.Products;
import com.hana.ddok.products.exception.ProductsNotFound;
import com.hana.ddok.products.repository.ProductsRepository;
import com.hana.ddok.users.domain.Users;
import com.hana.ddok.users.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.security.auth.login.AccountNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final ProductsRepository productsRepository;
    private final UsersRepository usersRepository;

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

        List<Account> accountList = accountRepository.findAllByUsers(users);
        List<AccountFindAllRes> accountFindAllResList = accountList.stream()
                .filter(account -> account.getType() == 1)
                .map(AccountFindAllRes::new)
                .collect(Collectors.toList());

        Optional<Account> parkingAccountOptional = accountList.stream()
                .filter(account -> account.getType() == 2)
                .findFirst();
        if (parkingAccountOptional.isPresent()) {
            Account parkingAccount = parkingAccountOptional.get();
            Long moneyboxTotalBalance = accountList.stream()
                    .filter(account -> account.getType() == 2 || account.getType() == 3 || account.getType() == 4)
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

        Account parkingAccount = accountRepository.save(moneyboxSaveReq.toEntity(users, products, 1));
        Account expenseAccount = accountRepository.save(moneyboxSaveReq.toEntity(users, products, 2));
        Account savingAccount = accountRepository.save(moneyboxSaveReq.toEntity(users, products, 3));

        return new MoneyboxSaveRes(parkingAccount, expenseAccount, savingAccount);
    }

    @Transactional(readOnly = true)
    public MoneyboxFindAllRes moneyboxFindAll(String phoneNumber) {
        Users users = usersRepository.findByPhoneNumber(phoneNumber);
        List<Account> accountList = accountRepository.findAllByUsers(users);

        Map<Integer, Account> accountMap = accountList.stream()
                .collect(Collectors.toMap(Account::getType, account -> account));
        Account parkingAccount = accountMap.get(1);
        Account expenseAccount = accountMap.get(2);
        Account savingAccount = accountMap.get(3);
        if (parkingAccount == null || expenseAccount == null || savingAccount == null) {
            throw new MoneyboxNotFound();
        }
        return new MoneyboxFindAllRes(parkingAccount, expenseAccount, savingAccount);
    }
}