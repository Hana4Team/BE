package com.hana.ddok.account.repository;

import com.hana.ddok.account.domain.Account;
import com.hana.ddok.products.domain.ProductsType;
import com.hana.ddok.users.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    List<Account> findAllByUsers(Users users);
    Optional<Account> findByUsersAndProductsType(Users users, ProductsType type);
    Optional<Account> findByAccountNumber(String accountNumber);
    Optional<Account> findByUsersAndIsMissionConnected(Users users, Boolean missionConnected);
}
