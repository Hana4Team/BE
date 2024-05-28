package com.hana.ddok.account.service;

import com.hana.ddok.account.domain.Account;
import com.hana.ddok.account.dto.AccountFindAllRes;
import com.hana.ddok.account.dto.AccountSaveReq;
import com.hana.ddok.account.dto.AccountSaveRes;
import com.hana.ddok.account.repository.AccountRepository;
import com.hana.ddok.common.exception.EntityNotFoundException;
import com.hana.ddok.products.domain.Products;
import com.hana.ddok.products.exception.ProductsNotFound;
import com.hana.ddok.products.repository.ProductsRepository;
import com.hana.ddok.users.domain.Users;
import com.hana.ddok.users.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
        List<AccountFindAllRes> accountFindAllResList = accountRepository.findAllByUsers(users).stream()
                .map(AccountFindAllRes::new)
                .toList();
        return accountFindAllResList;
    }
}
