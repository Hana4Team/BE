package com.hana.ddok.account.service;

import com.hana.ddok.account.domain.Account;
import com.hana.ddok.account.dto.*;
import com.hana.ddok.account.exception.AccountDeleteDenied;
import com.hana.ddok.account.exception.AccountNotFound;
import com.hana.ddok.account.exception.AccountSaveDenied;
import com.hana.ddok.account.exception.AccountWithdrawalDenied;
import com.hana.ddok.account.repository.AccountRepository;
import com.hana.ddok.common.scheduler.Step3SchedulerService;
import com.hana.ddok.depositsaving.domain.Depositsaving;
import com.hana.ddok.depositsaving.exception.DepositsavingNotFound;
import com.hana.ddok.depositsaving.repository.DepositsavingRepository;
import com.hana.ddok.home.domain.Home;
import com.hana.ddok.home.exception.HomeNotFound;
import com.hana.ddok.home.repository.HomeRepository;
import com.hana.ddok.moneybox.domain.Moneybox;
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
import com.hana.ddok.users.exception.UsersStepDenied;
import com.hana.ddok.users.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
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
    private final HomeRepository homeRepository;
    private final Step3SchedulerService step3SchedulerService;

    @Transactional(readOnly = true)
    public List<AccountFindAllRes> accountFindAll(AccountFindAllReq accountFindAllReq, String phoneNumber) {
        Users users = usersRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new UsersNotFound());

        List<AccountFindAllRes> accountFindAllResList = accountRepository.findAllByUsersAndIsDeletedFalse(users).stream()
                .filter(account -> (accountFindAllReq.depositWithdrawalAccount() && Objects.equals(account.getProducts().getType(), ProductsType.DEPOSITWITHDRAWAL)) ||
                        (accountFindAllReq.moneyboxAccount() && Objects.equals(account.getProducts().getType(), ProductsType.MONEYBOX)) ||
                        (accountFindAllReq.saving100Account() && Objects.equals(account.getProducts().getType(), ProductsType.SAVING100)) ||
                        (accountFindAllReq.savingAccount() && Objects.equals(account.getProducts().getType(), ProductsType.SAVING)) ||
                        (accountFindAllReq.depositAccount() && Objects.equals(account.getProducts().getType(), ProductsType.DEPOSIT))

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

        // 2단계 시작
        if (users.getStep() != 2 || (users.getStepStatus() != UsersStepStatus.NOTSTARTED && users.getStepStatus() != UsersStepStatus.FAIL) ) {
            throw new UsersStepDenied();
        }
        users.updateStepStatus(UsersStepStatus.PROCEEDING);

        // 머니박스 : 유저 당 1개 가능
        Optional<Account> accountOptional = accountRepository.findByUsersAndProductsTypeAndIsDeletedFalse(users, ProductsType.MONEYBOX);
        if (accountOptional.isPresent()) {
            throw new AccountSaveDenied();
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
        // 100일적금 : 유저 당 1개 가능
        Optional<Account> accountOptional = accountRepository.findByUsersAndProductsTypeAndIsDeletedFalse(users, ProductsType.SAVING100);
        if (accountOptional.isPresent()) {
            throw new AccountSaveDenied();
        }

        // 3단계 시작
        if (users.getStep() != 3 || (users.getStepStatus() != UsersStepStatus.NOTSTARTED && users.getStepStatus() != UsersStepStatus.FAIL) ) {
            throw new UsersStepDenied();
        }
        users.updateStepStatus(UsersStepStatus.PROCEEDING);

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

        if (initialAmount > 0) {
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
        }

        // 스케줄링 : 1일1회 적금 납입
        AtomicInteger executionCount = new AtomicInteger(1);
        step3SchedulerService.scheduleTaskForUser(users.getUsersId(),
                () -> executeSaving100Task(executionCount, users, accountSaving100SaveReq.payment(), account, withdrawalAccount)
                , 24 * 60 * 60 * 1000
        );

        return new AccountSaving100SaveRes(depositsaving, account);
    }

    @Transactional
    public void executeSaving100Task(AtomicInteger executionCount, Users users, Long payment, Account account, Account withdrawalAccount) {
        if (executionCount.getAndIncrement() < 10) {    // 2~100일차
            transactionService.transactionSave(
                    new TransactionSaveReq(payment, executionCount.get() + "일차납입", executionCount.get() + "일차납입", withdrawalAccount.getAccountNumber(), account.getAccountNumber())
            );
            step3SchedulerService.scheduleTaskForUser(users.getUsersId(), () -> executeSaving100Task(executionCount, users, payment, account, withdrawalAccount)
                    , 24 * 60 * 60 * 1000
            );
        }
        else {  // 만기 시 : 101일차
            // 성공일자 50일 이상부터 : 최고금리
            if (transactionService.transactionSaving100Check(users.getPhoneNumber()).successCount() >= 50) {
                account.updateInterest(account.getProducts().getInterest2());
            }

            // 이사가기
            Home home = homeRepository.findById(users.getHome().getHomeId() + 1)
                    .orElseThrow(() -> new HomeNotFound());
            users.updateHome(home);
            users.updateStepStatus(UsersStepStatus.SUCCESS);
            usersRepository.save(users);
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

        // 4단계 시작
        if (users.getStep() != 4 || (users.getStepStatus() != UsersStepStatus.NOTSTARTED && users.getStepStatus() != UsersStepStatus.FAIL) ) {
            throw new UsersStepDenied();
        }
        users.updateStepStatus(UsersStepStatus.PROCEEDING);

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

        // 5단계 시작 => 성공
        if (users.getStep() != 5 || users.getStepStatus() != UsersStepStatus.NOTSTARTED) {
            throw new UsersStepDenied();
        }
        users.updateStepStatus(UsersStepStatus.SUCCESS);

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

        return new AccountDepositSaveRes(depositsaving, account);
    }

    @Transactional
    public AccountDeleteRes accountDelete(AccountDeleteReq accountDeleteReq, String phoneNumber) {
        Users users = usersRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new UsersNotFound());

        Account deleteAccount = accountRepository.findById(accountDeleteReq.deleteAccountId())
                .orElseThrow(() -> new AccountNotFound());
        Account depositAccount = accountRepository.findById(accountDeleteReq.depositAccountId())
                .orElseThrow(() -> new AccountNotFound());

        // 100일적금, 적금, 예금만 해지 가능
        Products products = deleteAccount.getProducts();
        if (products.getType() == ProductsType.DEPOSITWITHDRAWAL || products.getType() == ProductsType.MONEYBOX) {
            throw new AccountDeleteDenied();
        }
        Depositsaving depositsaving = depositsavingRepository.findByAccount(deleteAccount)
                .orElseThrow(() -> new DepositsavingNotFound());

        // 이율 수정
        LocalDate currentDate = LocalDate.now();
        LocalDate endDate = depositsaving.getEndDate();
        Float interest = 0f;
        if (currentDate.isBefore(endDate)) {    // 만기일 이전 : 중도해지 최저금리
            interest = products.getInterest1();
            // 단계 진행 중이면, status 변경
            if (users.getStepStatus() == UsersStepStatus.PROCEEDING) {
                users.updateStepStatus(UsersStepStatus.FAIL);
            }
        } else if (currentDate.isEqual(endDate) || currentDate.isAfter(endDate)) {  // 만기일 이후 : 만기해지 최고금리
            interest = (products.getInterest1());
            users.updateStepStatus(UsersStepStatus.SUCCESS);
        }
        deleteAccount.updateInterest(interest);

        Long deleteAmount = deleteAccount.getBalance();
        if (deleteAmount > 0) {
            // 계좌 간 송금 [출금계좌 -> 입금계좌]
            transactionService.transactionSave(
                    new TransactionSaveReq(
                            deleteAmount, "예적금해지", "예적금해지", deleteAccount.getAccountNumber(), depositAccount.getAccountNumber()
                    )
            );

            // 이자입금 [ -> 입금계좌]
            transactionService.transactionInterestSave(
                    new TransactionInterestSaveReq(
                            (long) (deleteAmount * interest / 100), "예적금이자", depositAccount.getAccountNumber()
                    )
            );
        }

        // 해지
        deleteAccount.deleteAccount();
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