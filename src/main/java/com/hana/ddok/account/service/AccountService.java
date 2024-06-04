package com.hana.ddok.account.service;

import com.hana.ddok.account.domain.Account;
import com.hana.ddok.account.dto.*;
import com.hana.ddok.account.exception.AccountNotFound;
import com.hana.ddok.account.exception.AccountWithdrawalDenied;
import com.hana.ddok.account.repository.AccountRepository;
import com.hana.ddok.account.util.AccountNumberGenerator;
import com.hana.ddok.depositsaving.domain.Depositsaving;
import com.hana.ddok.account.dto.AccountFindbyMissionRes;
import com.hana.ddok.depositsaving.exception.DepositsavingNotFound;
import com.hana.ddok.depositsaving.repository.DepositsavingRepository;
import com.hana.ddok.moneybox.domain.Moneybox;
import com.hana.ddok.moneybox.exception.MoneyboxNotFound;
import com.hana.ddok.moneybox.repository.MoneyboxRepository;
import com.hana.ddok.products.domain.Products;
import com.hana.ddok.products.domain.ProductsType;
import com.hana.ddok.products.exception.ProductsNotFound;
import com.hana.ddok.products.exception.ProductsTypeInvalid;
import com.hana.ddok.products.repository.ProductsRepository;
import com.hana.ddok.transaction.domain.Transaction;
import com.hana.ddok.transaction.dto.TransactionMoneyboxSaveReq;
import com.hana.ddok.transaction.dto.TransactionSaveReq;
import com.hana.ddok.transaction.exception.TransactionNotFound;
import com.hana.ddok.transaction.repository.TransactionRepository;
import com.hana.ddok.transaction.service.TransactionService;
import com.hana.ddok.users.domain.Users;
import com.hana.ddok.users.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final UsersRepository usersRepository;
    private final TransactionRepository transactionRepository;
    private final DepositsavingRepository depositsavingRepository;
    private final ProductsRepository productsRepository;
    private final MoneyboxRepository moneyboxRepository;
    private final TransactionService transactionService;

    @Transactional(readOnly = true)
    public List<AccountFindAllRes> accountFindAll(AccountFindAllReq accountFindAllReq, String phoneNumber) {
        Users users = usersRepository.findByPhoneNumber(phoneNumber);
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

    @Transactional(readOnly = true)
    public AccountFindByIdRes accountFindById(Long accountId, Integer year, Integer month) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFound());

        LocalDateTime startDate = LocalDateTime.of(year, month, 1, 0, 0);
        LocalDateTime endDate = startDate.plusMonths(1).minusDays(1).plusHours(23).plusMinutes(59).plusSeconds(59);

        List<Transaction> senderTransactionList =  transactionRepository.findAllBySenderAccountAndCreatedAtBetween(account, startDate, endDate);
        List<Transaction> recipientTransactionList =  transactionRepository.findAllByRecipientAccountAndCreatedAtBetween(account, startDate, endDate);

        List<Transaction> allTransactions = Stream.concat(senderTransactionList.stream(), recipientTransactionList.stream())
                .collect(Collectors.toList());
        Collections.sort(allTransactions, Comparator.comparing(Transaction::getCreatedAt));

        return new AccountFindByIdRes(account, allTransactions);
    }

    @Transactional(readOnly = true)
    public AccountFindbyMissionRes accountFindByMission(String phoneNumber) {
        Users users = usersRepository.findByPhoneNumber(phoneNumber);
        Account account = accountRepository.findByUsersAndIsMissionConnected(users, true)
                .orElseThrow(() -> new AccountNotFound());
        Depositsaving depositsaving = depositsavingRepository.findByAccount(account)
                .orElseThrow(() -> new DepositsavingNotFound());
        Transaction transaction = transactionRepository.findFirstByRecipientAccountOrderByCreatedAt(account)
                .orElseThrow(() -> new TransactionNotFound());

        LocalDate startDate = account.getCreatedAt().toLocalDate();
        LocalDate endDate = depositsaving.getEndDate();
        Period period = Period.between(startDate, endDate);
        Integer monthPeriod = period.getYears() * 12 + period.getMonths();

        return new AccountFindbyMissionRes(account, depositsaving, transaction, monthPeriod);
    }

    @Transactional
    public AccountSaving100SaveRes accountSaving100Save(AccountSaving100SaveReq accountSaving100SaveReq, String phoneNumber) {
        Users users = usersRepository.findByPhoneNumber(phoneNumber);
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

        // 계좌번호 생성
        String accountNumber;
        Optional<Account> existingAccount;
        do {
            accountNumber = AccountNumberGenerator.generateAccountNumber();
            existingAccount = accountRepository.findByAccountNumber(accountNumber);
        } while (existingAccount.isPresent());

        Account account = accountRepository.save(accountSaving100SaveReq.toEntity(users, products, accountNumber, password));
        Depositsaving depositsaving = depositsavingRepository.save(accountSaving100SaveReq.toDepositsaving(account, withdrawalAccount));

        // 초기자본 : 머니박스(저축)
        Moneybox moneybox = moneyboxRepository.findByAccountUsers(users)
                .orElseThrow(() -> new MoneyboxNotFound());
        Long initialAmount = moneybox.getSavingBalance();
        // 머니박스 간 송금 [머니박스(저축) -> 머니박스(파킹)]
        transactionService.transactionMoneyboxSave(
                new TransactionMoneyboxSaveReq(
                        initialAmount, "저축->파킹", "저축->파킹", "saving", "parking"
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
}