package com.hana.ddok.moneybox.repository;

import com.hana.ddok.account.domain.Account;
import com.hana.ddok.moneybox.domain.Moneybox;
import com.hana.ddok.users.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MoneyboxRepository extends JpaRepository<Moneybox, Long> {
    Optional<Moneybox> findByAccount(Account account);
    Optional<Moneybox> findByAccountUsers(Users users);
}
