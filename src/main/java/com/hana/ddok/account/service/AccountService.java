package com.hana.ddok.account.service;

import com.hana.ddok.account.domain.Account;
import com.hana.ddok.account.dto.*;
import com.hana.ddok.account.repository.AccountRepository;
import com.hana.ddok.products.domain.Products;
import com.hana.ddok.products.exception.ProductsNotFound;
import com.hana.ddok.products.repository.ProductsRepository;
import com.hana.ddok.users.domain.Users;
import com.hana.ddok.users.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public AccountSaveRes accountSave(AccountSaveReq accountSaveReq) {
        Users users = usersRepository.findById(1L).get();    // TODO : 시큐리티 적용 시 변경
        Products products = productsRepository.findById(accountSaveReq.productsId())
                .orElseThrow(() -> new ProductsNotFound());
        Account account = accountRepository.save(accountSaveReq.toEntity(users, products));
        return new AccountSaveRes(account);
    }

    @Transactional(readOnly = true)
    public List<AccountFindAllRes> accountFindAll() {
        Users users = usersRepository.findById(1L).get();    // TODO : 시큐리티 적용 시 변경

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

    @Transactional(readOnly = true)
    public MoneyboxFindAllRes moneyboxFindAll() {
        Users users = usersRepository.findById(1L).get();    // TODO : 시큐리티 적용 시 변경
        List<Account> accountList = accountRepository.findAllByUsers(users);

        Map<Integer, Account> accountMap = accountList.stream()
                .collect(Collectors.toMap(Account::getType, account -> account));
        Account parkingAccount = accountMap.get(1);
        Account expenseAccount = accountMap.get(2);
        Account savingAccount = accountMap.get(3);

        MoneyboxFindAllRes moneyboxFindAllRes = new MoneyboxFindAllRes(parkingAccount, expenseAccount, savingAccount);
        return moneyboxFindAllRes;
    }
}
