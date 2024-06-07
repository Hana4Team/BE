package com.hana.ddok.account.service;

import com.hana.ddok.account.domain.Account;
import com.hana.ddok.account.dto.*;
import com.hana.ddok.account.exception.AccountDeleteDenied;
import com.hana.ddok.account.exception.AccountNotFound;
import com.hana.ddok.account.exception.AccountWithdrawalDenied;
import com.hana.ddok.account.repository.AccountRepository;
import com.hana.ddok.depositsaving.domain.Depositsaving;
import com.hana.ddok.account.dto.AccountDepositsavingSaveReq;
import com.hana.ddok.account.dto.AccountDepositsavingSaveRes;
import com.hana.ddok.depositsaving.exception.DepositsavingNotFound;
import com.hana.ddok.depositsaving.repository.DepositsavingRepository;
import com.hana.ddok.moneybox.domain.Moneybox;
import com.hana.ddok.account.dto.AccountMoneyboxSaveReq;
import com.hana.ddok.account.dto.AccountMoneyboxSaveRes;
import com.hana.ddok.moneybox.domain.MoneyboxType;
import com.hana.ddok.moneybox.exception.MoneyboxNotFound;
import com.hana.ddok.moneybox.repository.MoneyboxRepository;
import com.hana.ddok.products.domain.Products;
import com.hana.ddok.products.domain.ProductsType;
import com.hana.ddok.products.exception.ProductsNotFound;
import com.hana.ddok.products.exception.ProductsTypeInvalid;
import com.hana.ddok.products.repository.ProductsRepository;
import com.hana.ddok.transaction.dto.TransactionInterestSaveReq;
import com.hana.ddok.transaction.dto.TransactionMoneyboxSaveReq;
import com.hana.ddok.transaction.dto.TransactionSaveReq;
import com.hana.ddok.transaction.service.TransactionService;
import com.hana.ddok.users.domain.Users;
import com.hana.ddok.users.exception.UsersNotFound;
import com.hana.ddok.users.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final UsersRepository usersRepository;
    private final DepositsavingRepository depositsavingRepository;
    private final ProductsRepository productsRepository;
    private final MoneyboxRepository moneyboxRepository;
    private final TransactionService transactionService;

    @Transactional(readOnly = true)
    public List<AccountFindAllRes> accountFindAll(AccountFindAllReq accountFindAllReq, String phoneNumber) {
        Users users = usersRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new UsersNotFound());
        List<AccountFindAllRes> accountFindAllResList = accountRepository.findAllByUsers(users).stream()
                .filter(account -> (accountFindAllReq.depositWithdrawalAccount() && account.getProducts().getType().equals(ProductsType.DEPOSITWITHDRAWAL)) ||
                        (accountFindAllReq.moneyboxAccount() && account.getProducts().getType().equals(ProductsType.MONEYBOX)) ||
                        (accountFindAllReq.saving100Account() && account.getProducts().getType().equals(ProductsType.SAVING100)) ||
                        (accountFindAllReq.savingAccount() && account.getProducts().getType().equals(ProductsType.SAVING)) ||
                        (accountFindAllReq.depositAccount() && account.getProducts().getType().equals(ProductsType.DEPOSIT))

                )
                .map(AccountFindAllRes::new)
                .collect(Collectors.toList());
        return accountFindAllResList;
    }

    @Transactional
    public AccountMoneyboxSaveRes accountMoneyboxSave(AccountMoneyboxSaveReq accountMoneyboxSaveReq, String phoneNumber) {
        Users users = usersRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new UsersNotFound());
        Products products = productsRepository.findById(accountMoneyboxSaveReq.productsId())
                .orElseThrow(() -> new ProductsNotFound());
        if (products.getType() != ProductsType.MONEYBOX) {
            throw new ProductsTypeInvalid();
        }

        Account account = accountRepository.save(accountMoneyboxSaveReq.toEntity(users, products, generateAccountNumber()));
        Moneybox moneybox = moneyboxRepository.save(accountMoneyboxSaveReq.toMoneybox(account));
        return new AccountMoneyboxSaveRes(account, moneybox);
    }

    @Transactional
    public AccountSaving100SaveRes accountSaving100Save(AccountSaving100SaveReq accountSaving100SaveReq, String phoneNumber) {
        Users users = usersRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new UsersNotFound());
        Products products = productsRepository.findById(accountSaving100SaveReq.productsId())
                .orElseThrow(() -> new ProductsNotFound());
        if (products.getType() != ProductsType.SAVING100) {
            throw new ProductsTypeInvalid();
        }

        // 출금계좌 : 머니박스(저축)만 가능
        Account withdrawalAccount = accountRepository.findById(accountSaving100SaveReq.withdrawalAccountId())
                .orElseThrow(() -> new AccountNotFound());
        if (withdrawalAccount.getProducts().getType() != ProductsType.MONEYBOX) {
            throw new AccountWithdrawalDenied();
        }
        // 비밀번호 동일하게 설정
        String password = withdrawalAccount.getPassword();

        Account account = accountRepository.save(accountSaving100SaveReq.toEntity(users, products, generateAccountNumber(), password));
        Depositsaving depositsaving = depositsavingRepository.save(accountSaving100SaveReq.toDepositsaving(account, withdrawalAccount));

        // 초기자본 : 머니박스(저축)
        Moneybox moneybox = moneyboxRepository.findByAccountUsers(users)
                .orElseThrow(() -> new MoneyboxNotFound());
        Integer initialAmount = moneybox.getSavingBalance().intValue();
        // 머니박스 간 송금 [머니박스(저축) -> 머니박스(파킹)]
        String title = MoneyboxType.SAVING + "->" + MoneyboxType.PARKING;
        transactionService.transactionMoneyboxSave(
                new TransactionMoneyboxSaveReq(
                        initialAmount, title, title, MoneyboxType.SAVING, MoneyboxType.PARKING
                ), phoneNumber
        );
        // 계좌 간 송금 [머니박스 -> 100일적금]
        transactionService.transactionSave(
                new TransactionSaveReq(
                        initialAmount, "100일적금가입", "100일적금가입", withdrawalAccount.getAccountNumber(), account.getAccountNumber()
                )
        );

        return new AccountSaving100SaveRes(depositsaving, account);
    }

    @Transactional
    public AccountDepositsavingSaveRes accountDepositsavingSave(AccountDepositsavingSaveReq accountDepositsavingSaveReq, String phoneNumber) {
        Users users = usersRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new UsersNotFound());
        Products products = productsRepository.findById(accountDepositsavingSaveReq.productsId())
                .orElseThrow(() -> new ProductsNotFound());
        if (products.getType() != ProductsType.DEPOSIT && products.getType() != ProductsType.SAVING) {
            throw new ProductsTypeInvalid();
        }

        // 출금계좌 : 입출금계좌만 가능
        Account withdrawalAccount = accountRepository.findById(accountDepositsavingSaveReq.withdrawalAccountId())
                .orElseThrow(() -> new AccountNotFound());
        if (withdrawalAccount.getProducts().getType() != ProductsType.DEPOSITWITHDRAWAL) {
            throw new AccountWithdrawalDenied();
        }
        // 비밀번호 동일하게 설정
        String password = withdrawalAccount.getPassword();

        Account account = accountRepository.save(accountDepositsavingSaveReq.toEntity(users, products, generateAccountNumber(), password));
        Depositsaving depositsaving = depositsavingRepository.save(accountDepositsavingSaveReq.toDepositsaving(account, withdrawalAccount));

        // 계좌 간 송금 [입출금계좌 -> 예적금]
        transactionService.transactionSave(
                new TransactionSaveReq(
                        accountDepositsavingSaveReq.initialAmount().intValue(), "예적금가입", "예적금가입", withdrawalAccount.getAccountNumber(), account.getAccountNumber()
                )
        );

        return new AccountDepositsavingSaveRes(depositsaving, account);
    }

    @Transactional
    public AccountDepositDeleteRes accountDepositDelete(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFound());
        Products products = account.getProducts();

        // 100일적금, 적금, 예금만 해지 가능
        if (products.getType() == ProductsType.DEPOSITWITHDRAWAL || products.getType() == ProductsType.MONEYBOX) {
            throw new AccountDeleteDenied();
        }
        Depositsaving depositsaving = depositsavingRepository.findByAccount(account)
                .orElseThrow(() -> new DepositsavingNotFound());

        // 이율 수정
        LocalDate currentDate = LocalDate.now();
        LocalDate endDate = depositsaving.getEndDate();
        Float interest = 0f;
        if (currentDate.isBefore(endDate)) {    // 만기일 이전 : 중도해지 최저금리
            interest = products.getInterest1();
        } else if (currentDate.isEqual(endDate) || currentDate.isAfter(endDate)) {  // 만기일 이전 : 만기해지 최고금리
            interest = (products.getInterest2());
        }
        account.updateInterest(interest);

        // 계좌 간 송금 [예적금계좌 -> 출금계좌]
        transactionService.transactionSave(
                new TransactionSaveReq(
                        account.getBalance().intValue(), "예적금해지", "예적금해지", account.getAccountNumber(), depositsaving.getWithdrawalAccount().getAccountNumber()
                )
        );

        // 이자입금 [ -> 출금계좌]
        transactionService.transactionInterestSave(
                new TransactionInterestSaveReq(
                        (int)(account.getBalance() * interest)/100, "예적금이자", depositsaving.getWithdrawalAccount().getAccountNumber()
                )
        );

        // 해지
        account.deleteAccount();

        return new AccountDepositDeleteRes(account);
    }

    @Transactional(readOnly = true)
    public String generateAccountNumber() {
        String accountNumber;
        Optional<Account> existingAccount;
        do {
            StringBuilder sb = new StringBuilder("880-");
            Random random = new Random();
            for (int i = 0; i < 11; i++) {
                sb.append(random.nextInt(10)); // 0~9
                if (i == 5) {
                    sb.append("-");
                }
            }
            accountNumber = sb.toString();   // 880-XXXXXX-XXXXX
            existingAccount = accountRepository.findByAccountNumber(accountNumber);
        } while (existingAccount.isPresent());
        return accountNumber;
    }
}