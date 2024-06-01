package com.hana.ddok.moneybox.repository;

import com.hana.ddok.account.domain.Account;
import com.hana.ddok.account.domain.Moneybox;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MoneyboxRepository extends JpaRepository<Moneybox, Long> {
    Optional<Moneybox> findByAccount(Account account);
}
