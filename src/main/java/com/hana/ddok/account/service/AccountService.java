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
    public List<AccountFindAllRes> accountFindAll(AccountFindAllReq accountFindAllReq, String phoneNumber) {
        Users users = usersRepository.findByPhoneNumber(phoneNumber);
        List<AccountFindAllRes> accountFindAllResList = accountRepository.findAllByUsers(users).stream()
                .filter(account -> (accountFindAllReq.depositAccount() && account.getProducts().getType().equals(1)) ||
                                (accountFindAllReq.savingsAccount() && account.getProducts().getType().equals(2)) ||
                                (accountFindAllReq.depositWithdrawalAccount() && account.getProducts().getType().equals(3)) ||
                                (accountFindAllReq.moneyboxAccount() && account.getProducts().getType().equals(4))
                )
                .map(AccountFindAllRes::new)
                .collect(Collectors.toList());
        return accountFindAllResList;
    }
}