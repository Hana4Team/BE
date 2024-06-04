package com.hana.ddok.depositsaving.service;

import com.hana.ddok.account.domain.Account;
import com.hana.ddok.account.exception.AccountNotFound;
import com.hana.ddok.account.exception.AccountWithdrawalDenied;
import com.hana.ddok.account.repository.AccountRepository;
import com.hana.ddok.account.util.AccountNumberGenerator;
import com.hana.ddok.depositsaving.domain.Depositsaving;
import com.hana.ddok.depositsaving.dto.DepositsavingSaveReq;
import com.hana.ddok.depositsaving.dto.DepositsavingSaveRes;
import com.hana.ddok.depositsaving.repository.DepositsavingRepository;
import com.hana.ddok.products.domain.Products;
import com.hana.ddok.products.domain.ProductsType;
import com.hana.ddok.products.exception.ProductsNotFound;
import com.hana.ddok.products.exception.ProductsTypeInvalid;
import com.hana.ddok.products.repository.ProductsRepository;
import com.hana.ddok.users.domain.Users;
import com.hana.ddok.users.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DepositsavingService {
    private final DepositsavingRepository depositsavingRepository;
    private final AccountRepository accountRepository;
    private final ProductsRepository productsRepository;
    private final UsersRepository usersRepository;

    @Transactional
    public DepositsavingSaveRes depositsavingSave(DepositsavingSaveReq depositsavingSaveReq, String phoneNumber) {
        Users users = usersRepository.findByPhoneNumber(phoneNumber);
        Products products = productsRepository.findById(depositsavingSaveReq.productsId())
                .orElseThrow(() -> new ProductsNotFound());
        if (products.getType() != ProductsType.DEPOSIT && products.getType() != ProductsType.SAVING) {
            throw new ProductsTypeInvalid();
        }

        // 출금계좌 : 입출금계좌만 가능
        Account withdrawalAccount = accountRepository.findById(depositsavingSaveReq.withdrawalAccountId())
                .orElseThrow(() -> new AccountNotFound());
        if (withdrawalAccount.getProducts().getType() != ProductsType.DEPOSITWITHDRAWAL) {
            throw new AccountWithdrawalDenied();
        }
        // 비밀번호 동일하게 설정
        String password = withdrawalAccount.getPassword();

        // 계좌번호 생성
        String accountNumber;
        Optional<Account> existingAccount;
        do {
            accountNumber = AccountNumberGenerator.generateAccountNumber();
            existingAccount = accountRepository.findByAccountNumber(accountNumber);
        } while (existingAccount.isPresent());

        Account account = accountRepository.save(depositsavingSaveReq.toAccount(users, products, accountNumber, password));
        Depositsaving depositsaving = depositsavingRepository.save(depositsavingSaveReq.toEntity(account, withdrawalAccount));
        return new DepositsavingSaveRes(depositsaving, account);
    }
}