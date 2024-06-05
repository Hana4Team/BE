package com.hana.ddok.depositsaving.repository;

import com.hana.ddok.account.domain.Account;
import com.hana.ddok.depositsaving.domain.Depositsaving;
import com.hana.ddok.users.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DepositsavingRepository extends JpaRepository<Depositsaving, Long> {
    Optional<Depositsaving> findByAccount(Account account);
}
