package com.hana.ddok.depositsaving.service;

import com.hana.ddok.account.domain.Account;
import com.hana.ddok.account.exception.AccountNotFound;
import com.hana.ddok.account.repository.AccountRepository;
import com.hana.ddok.depositsaving.domain.Depositsaving;
import com.hana.ddok.depositsaving.dto.DepositsavingFindbyTypeRes;
import com.hana.ddok.depositsaving.exception.DepositsavingNotFound;
import com.hana.ddok.depositsaving.repository.DepositsavingRepository;
import com.hana.ddok.products.domain.ProductsType;
import com.hana.ddok.products.exception.ProductsTypeInvalid;
import com.hana.ddok.transaction.domain.Transaction;
import com.hana.ddok.transaction.exception.TransactionNotFound;
import com.hana.ddok.transaction.repository.TransactionRepository;
import com.hana.ddok.users.domain.Users;
import com.hana.ddok.users.exception.UsersNotFound;
import com.hana.ddok.users.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class DepositsavingService {
    private final AccountRepository accountRepository;
    private final UsersRepository usersRepository;
    private final TransactionRepository transactionRepository;
    private final DepositsavingRepository depositsavingRepository;


    @Transactional(readOnly = true)
    public DepositsavingFindbyTypeRes depositsavingFindByType(ProductsType type, String phoneNumber) {
        Users users = usersRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new UsersNotFound());
        Account account = accountRepository.findByUsersAndProductsTypeAndIsDeletedFalse(users, type)
                        .orElseThrow(() -> new AccountNotFound());
        Depositsaving depositsaving = depositsavingRepository.findByAccount(account)
                .orElseThrow(() -> new DepositsavingNotFound());
        Transaction transaction = transactionRepository.findFirstByRecipientAccountOrderByCreatedAt(account)
                .orElseThrow(() -> new TransactionNotFound());

        Long initialAmount = transaction.getAmount();
        Long payment = depositsaving.getPayment();
        Long targetAmount = 0L;
        switch (type) {
            case SAVING100:
                targetAmount = initialAmount + payment * 99;
                break;
            case SAVING:
                Integer monthPeriod = (int) ChronoUnit.MONTHS.between(account.getCreatedAt().toLocalDate(), LocalDate.now());
                targetAmount = payment * (monthPeriod - 1) + initialAmount;
                break;
            case DEPOSIT:
                targetAmount = initialAmount;
                break;
            default:
                throw new ProductsTypeInvalid();
        }

        return new DepositsavingFindbyTypeRes(account, depositsaving, initialAmount, payment, targetAmount);
    }
}