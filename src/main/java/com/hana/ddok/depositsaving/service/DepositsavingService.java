package com.hana.ddok.depositsaving.service;

import com.hana.ddok.account.domain.Account;
import com.hana.ddok.account.exception.AccountNotFound;
import com.hana.ddok.account.repository.AccountRepository;
import com.hana.ddok.depositsaving.domain.Depositsaving;
import com.hana.ddok.depositsaving.dto.DepositsavingFindbyDepositRes;
import com.hana.ddok.depositsaving.dto.DepositsavingFindbySaving100Res;
import com.hana.ddok.depositsaving.dto.DepositsavingFindbySavingRes;
import com.hana.ddok.depositsaving.exception.DepositsavingNotFound;
import com.hana.ddok.depositsaving.repository.DepositsavingRepository;
import com.hana.ddok.products.domain.ProductsType;
import com.hana.ddok.transaction.domain.Transaction;
import com.hana.ddok.transaction.exception.TransactionNotFound;
import com.hana.ddok.transaction.repository.TransactionRepository;
import com.hana.ddok.users.domain.Users;
import com.hana.ddok.users.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;

@Service
@RequiredArgsConstructor
public class DepositsavingService {
    private final AccountRepository accountRepository;
    private final UsersRepository usersRepository;
    private final TransactionRepository transactionRepository;
    private final DepositsavingRepository depositsavingRepository;


    @Transactional(readOnly = true)
    public DepositsavingFindbySaving100Res depositsavingFindBySaving100(String phoneNumber) {
        Users users = usersRepository.findByPhoneNumber(phoneNumber);
        Account account = accountRepository.findByUsersAndProductsType(users, ProductsType.SAVING100)
                .orElseThrow(() -> new AccountNotFound());
        Depositsaving depositsaving = depositsavingRepository.findByAccount(account)
                .orElseThrow(() -> new DepositsavingNotFound());
        Transaction transaction = transactionRepository.findFirstByRecipientAccountOrderByCreatedAt(account)
                .orElseThrow(() -> new TransactionNotFound());

        return new DepositsavingFindbySaving100Res(account, depositsaving, transaction);
    }

    @Transactional(readOnly = true)
    public DepositsavingFindbySavingRes depositsavingFindBySaving(String phoneNumber) {
        Users users = usersRepository.findByPhoneNumber(phoneNumber);
        Account account = accountRepository.findByUsersAndProductsType(users, ProductsType.SAVING)
                .orElseThrow(() -> new AccountNotFound());
        Depositsaving depositsaving = depositsavingRepository.findByAccount(account)
                .orElseThrow(() -> new DepositsavingNotFound());
        Transaction transaction = transactionRepository.findFirstByRecipientAccountOrderByCreatedAt(account)
                .orElseThrow(() -> new TransactionNotFound());

        // 납입기간(월) 계산
        Period period = Period.between(account.getCreatedAt().toLocalDate(),depositsaving.getEndDate());
        Integer monthPeriod = period.getYears() * 12 + period.getMonths();

        return new DepositsavingFindbySavingRes(account, depositsaving, transaction, monthPeriod);
    }

    @Transactional(readOnly = true)
    public DepositsavingFindbyDepositRes depositsavingFindByDeposit(String phoneNumber) {
        Users users = usersRepository.findByPhoneNumber(phoneNumber);
        Account account = accountRepository.findByUsersAndProductsType(users, ProductsType.DEPOSIT)
                .orElseThrow(() -> new AccountNotFound());
        Depositsaving depositsaving = depositsavingRepository.findByAccount(account)
                .orElseThrow(() -> new DepositsavingNotFound());
        Transaction transaction = transactionRepository.findFirstByRecipientAccountOrderByCreatedAt(account)
                .orElseThrow(() -> new TransactionNotFound());

        return new DepositsavingFindbyDepositRes(account, depositsaving, transaction);
    }
}