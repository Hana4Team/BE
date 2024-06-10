package com.hana.ddok.account.service;

import com.hana.ddok.account.domain.Account;
import com.hana.ddok.account.dto.*;
import com.hana.ddok.account.exception.AccountDeleteDenied;
import com.hana.ddok.account.exception.AccountNotFound;
import com.hana.ddok.account.exception.AccountSaveDenied;
import com.hana.ddok.account.exception.AccountWithdrawalDenied;
import com.hana.ddok.account.repository.AccountRepository;
import com.hana.ddok.account.scheduler.AccountSaving100SchedulerService;
import com.hana.ddok.account.scheduler.AccountStep3SchedulerService;
import com.hana.ddok.depositsaving.domain.Depositsaving;
import com.hana.ddok.account.dto.AccountDepositSaveReq;
import com.hana.ddok.account.dto.AccountDepositSaveRes;
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
import com.hana.ddok.users.domain.UsersStepStatus;
import com.hana.ddok.users.exception.UsersNotFound;
import com.hana.ddok.users.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
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
    private final AccountStep3SchedulerService accountStep3SchedulerService;
    private final AccountSaving100SchedulerService accountSaving100SchedulerService;

    @Transactional(readOnly = true)
    public List<AccountFindAllRes> accountFindAll(AccountFindAllReq accountFindAllReq, String phoneNumber) {
        Users users = usersRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new UsersNotFound());
        // TODO
        List<AccountFindAllRes> accountFindAllResList = accountRepository.findAllByUsersAndIsDeletedFalse(users).stream()
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
        // 머니박스 : 유저 당 1개 가능
        Optional<Account> accountOptional = accountRepository.findByUsersAndProductsTypeAndIsDeletedFalse(users, ProductsType.MONEYBOX);
        if (accountOptional.isPresent()) {
            throw new AccountSaveDenied();
        }

        Account account = accountRepository.save(accountMoneyboxSaveReq.toEntity(users, products, generateAccountNumber()));
        Moneybox moneybox = moneyboxRepository.save(accountMoneyboxSaveReq.toMoneybox(account));

        users.updateStepStatus(UsersStepStatus.PROCEEDING);
        usersRepository.save(users);

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
        // 100일적금 : 유저 당 1개 가능
        Optional<Account> accountOptional = accountRepository.findByUsersAndProductsTypeAndIsDeletedFalse(users, ProductsType.SAVING100);
        if (accountOptional.isPresent()) {
            throw new AccountSaveDenied();
        }

        // 출금계좌 : 입출금계좌만 가능
        Account withdrawalAccount = accountRepository.findById(accountSaving100SaveReq.withdrawalAccountId())
                .orElseThrow(() -> new AccountNotFound());
        if (withdrawalAccount.getProducts().getType() != ProductsType.DEPOSITWITHDRAWAL) {
            throw new AccountWithdrawalDenied();
        }
        // 비밀번호 동일하게 설정
        String password = withdrawalAccount.getPassword();

        Account account = accountRepository.save(accountSaving100SaveReq.toEntity(users, products, generateAccountNumber(), password));
        Depositsaving depositsaving = depositsavingRepository.save(accountSaving100SaveReq.toDepositsaving(account, withdrawalAccount));

        // 초기자본 : 머니박스(저축)
        Moneybox moneybox = moneyboxRepository.findByAccountUsers(users)
                .orElseThrow(() -> new MoneyboxNotFound());
        Long initialAmount = moneybox.getSavingBalance();

        // 머니박스 간 송금 [머니박스(저축) -> 머니박스(파킹)]
        String title = MoneyboxType.SAVING + "->" + MoneyboxType.PARKING;
        transactionService.transactionMoneyboxSave(
                new TransactionMoneyboxSaveReq(
                        initialAmount, title, title, MoneyboxType.SAVING, MoneyboxType.PARKING
                ), phoneNumber
        );

        // 계좌 간 송금 [머니박스 -> 100일적금]
        Account moneyboxAccount = accountRepository.findByUsersAndProductsTypeAndIsDeletedFalse(users, ProductsType.MONEYBOX)
                .orElseThrow(() -> new AccountNotFound());
        transactionService.transactionSave(
                new TransactionSaveReq(
                        initialAmount, "백일적금가입", "백일적금가입", moneyboxAccount.getAccountNumber(), account.getAccountNumber()
                )
        );

        // 1일1회 적금 납입
        AtomicInteger executionCount = new AtomicInteger(1);
        accountSaving100SchedulerService.scheduleTaskForUser(users.getUsersId(),
                () -> executeTask(executionCount, users.getUsersId(), accountSaving100SaveReq.payment(), account, withdrawalAccount)
                , 24 * 60 * 60 * 1000
        );

        // 3단계 스케줄러 시작 -> 100일 뒤 종료
        accountStep3SchedulerService.scheduleTaskForUser(users.getUsersId(),
                () -> {
                    // 50일 이상 성공 시 : 최고금리
                    if (transactionService.transactionSaving100Check(users.getPhoneNumber()).successCount() >= 50) {
                        account.updateInterest(products.getInterest2());
                    }
                    accountStep3SchedulerService.cancelScheduledTaskForUser(users.getUsersId());
                }, 100 * 24 * 60 * 60 * 1000
        );

        return new AccountSaving100SaveRes(depositsaving, account);
    }

    private void executeTask(AtomicInteger executionCount, Long userId, Long payment, Account account, Account withdrawalAccount) {
        if (executionCount.getAndIncrement() < 100) {    // 2~100일차
            // 작업 수행 내용
            transactionService.transactionSave(
                    new TransactionSaveReq(payment, executionCount.get() + "일차납입", executionCount.get() + "일차", withdrawalAccount.getAccountNumber(), account.getAccountNumber())
            );
            accountSaving100SchedulerService.scheduleTaskForUser(userId, () -> executeTask(executionCount, userId, payment, account, withdrawalAccount)
                    , 24 * 60 * 60 * 1000
            );
        }
    }

    @Transactional
    public AccountSavingSaveRes accountSavingSave(AccountSavingSaveReq accountSavingSaveReq, String phoneNumber) {
        Users users = usersRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new UsersNotFound());
        Products products = productsRepository.findById(accountSavingSaveReq.productsId())
                .orElseThrow(() -> new ProductsNotFound());
        if (products.getType() != ProductsType.SAVING) {
            throw new ProductsTypeInvalid();
        }
        // 같은 상품에 여러 번 가입 불가
        Optional<Account> accountOptional = accountRepository.findByUsersAndProductsAndIsDeletedFalse(users, products);
        if (accountOptional.isPresent()) {
            throw new AccountSaveDenied();
        }

        // 출금계좌 : 입출금계좌만 가능
        Account withdrawalAccount = accountRepository.findById(accountSavingSaveReq.withdrawalAccountId())
                .orElseThrow(() -> new AccountNotFound());
        if (withdrawalAccount.getProducts().getType() != ProductsType.DEPOSITWITHDRAWAL) {
            throw new AccountWithdrawalDenied();
        }
        // 비밀번호 동일하게 설정
        String password = withdrawalAccount.getPassword();

        Account account = accountRepository.save(accountSavingSaveReq.toEntity(users, products, generateAccountNumber(), password));
        Depositsaving depositsaving = depositsavingRepository.save(accountSavingSaveReq.toDepositsaving(account, withdrawalAccount));

        // 계좌 간 송금 [출금계좌 -> 적금]
        transactionService.transactionSave(
                new TransactionSaveReq(
                        accountSavingSaveReq.initialAmount(), "적금가입", "적금가입", withdrawalAccount.getAccountNumber(), account.getAccountNumber()
                )
        );

        if (users.getStep() == 4 && users.getStepStatus() == UsersStepStatus.NOTSTARTED) {
            // TODO : 4단계 스케줄링 시작
        }

        return new AccountSavingSaveRes(depositsaving, account);
    }

    @Transactional
    public AccountDepositSaveRes accountDepositSave(AccountDepositSaveReq accountDepositSaveReq, String phoneNumber) {
        Users users = usersRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new UsersNotFound());
        Products products = productsRepository.findById(accountDepositSaveReq.productsId())
                .orElseThrow(() -> new ProductsNotFound());
        if (products.getType() != ProductsType.DEPOSIT) {
            throw new ProductsTypeInvalid();
        }
        // 같은 상품에 여러 번 가입 불가
        Optional<Account> accountOptional = accountRepository.findByUsersAndProductsAndIsDeletedFalse(users, products);
        if (accountOptional.isPresent()) {
            throw new AccountSaveDenied();
        }

        // 출금계좌 : 입출금계좌만 가능
        Account withdrawalAccount = accountRepository.findById(accountDepositSaveReq.withdrawalAccountId())
                .orElseThrow(() -> new AccountNotFound());
        if (withdrawalAccount.getProducts().getType() != ProductsType.DEPOSITWITHDRAWAL) {
            throw new AccountWithdrawalDenied();
        }
        // 비밀번호 동일하게 설정
        String password = withdrawalAccount.getPassword();

        Account account = accountRepository.save(accountDepositSaveReq.toEntity(users, products, generateAccountNumber(), password));
        Depositsaving depositsaving = depositsavingRepository.save(accountDepositSaveReq.toDepositsaving(account, withdrawalAccount));

        // 계좌 간 송금 [입출금계좌 -> 예금]
        transactionService.transactionSave(
                new TransactionSaveReq(
                        accountDepositSaveReq.initialAmount(), "예금가입", "예금가입", withdrawalAccount.getAccountNumber(), account.getAccountNumber()
                )
        );

        if (users.getStep() == 5 && users.getStepStatus() == UsersStepStatus.NOTSTARTED) {
            // TODO : 5단계 스케줄링 시작
        }

        return new AccountDepositSaveRes(depositsaving, account);
    }

    @Transactional
    public AccountDeleteRes accountDelete(AccountDeleteReq accountDeleteReq) {
        Account withdrawalAccount = accountRepository.findById(accountDeleteReq.deleteAccountId())
                .orElseThrow(() -> new AccountNotFound());
        Account depositAccount = accountRepository.findById(accountDeleteReq.depositAccountId())
                .orElseThrow(() -> new AccountNotFound());

        // 100일적금, 적금, 예금만 해지 가능
        Products products = withdrawalAccount.getProducts();
        if (products.getType() == ProductsType.DEPOSITWITHDRAWAL || products.getType() == ProductsType.MONEYBOX) {
            throw new AccountDeleteDenied();
        }
        Depositsaving depositsaving = depositsavingRepository.findByAccount(withdrawalAccount)
                .orElseThrow(() -> new DepositsavingNotFound());

        // 이율 수정
        LocalDate currentDate = LocalDate.now();
        LocalDate endDate = depositsaving.getEndDate();
        Float interest = 0f;
        if (currentDate.isBefore(endDate)) {    // 만기일 이전 : 중도해지 최저금리
            interest = products.getInterest1();
        } else if (currentDate.isEqual(endDate) || currentDate.isAfter(endDate)) {  // 만기일 이후 : 만기해지 최고금리
            interest = (products.getInterest1());
        }
        withdrawalAccount.updateInterest(interest);

        // 계좌 간 송금 [출금계좌 -> 입금계좌]
        transactionService.transactionSave(
                new TransactionSaveReq(
                        withdrawalAccount.getBalance(), "예적금해지", "예적금해지", withdrawalAccount.getAccountNumber(), depositAccount.getAccountNumber()
                )
        );

        // 이자입금 [ -> 입금계좌]
        transactionService.transactionInterestSave(
                new TransactionInterestSaveReq(
                        (long)(withdrawalAccount.getBalance() * interest)/100, "예적금이자", depositAccount.getAccountNumber()
                )
        );

        // 해지
        withdrawalAccount.deleteAccount();
        return new AccountDeleteRes("success");
    }

    @Transactional(readOnly = true)
    public AccountPasswordCheckRes accountPasswordCheck(AccountPasswordCheckReq accountPasswordCheckReq) {
        Account account = accountRepository.findByAccountNumber(accountPasswordCheckReq.accountNumber())
                .orElseThrow(() -> new AccountNotFound());
        String message = Objects.equals(account.getPassword(), accountPasswordCheckReq.password()) ? "match" : "mismatch";
        return new AccountPasswordCheckRes(message);
    }

    private String generateAccountNumber() {
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

    @Transactional
    public void generateDummyDepositWithdrawalAccount(Users users) {
        // 해당 종류의 상품 중 랜덤으로 생성
        List<Products> productsList = productsRepository.findAllByType(ProductsType.DEPOSITWITHDRAWAL);
        if (productsList.isEmpty()) {
            throw new ProductsNotFound();
        }
        Random random = new Random();
        Integer randomIndex = random.nextInt(productsList.size());
        Products products = productsList.get(randomIndex);
        String password = String.format("%04d", random.nextInt(10000));   // 0000~9999

        Account account = Account.builder()
                .accountNumber(generateAccountNumber())
                .balance(10000000L)
                .interest(products.getInterest1())
                .password(password)
                .isDeleted(false)
                .users(users)
                .products(products)
                .build();
        accountRepository.save(account);
    }
}