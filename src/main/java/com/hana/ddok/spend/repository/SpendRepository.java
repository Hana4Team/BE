package com.hana.ddok.spend.repository;

import com.hana.ddok.account.domain.Account;
import com.hana.ddok.spend.domain.Spend;
import com.hana.ddok.spend.domain.SpendType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface SpendRepository extends JpaRepository<Spend, Long> {
    List<Spend> findByTransactionSenderAccountInAndTypeAndCreatedAtBetween(List<Account> accountList, SpendType type, LocalDateTime startDate, LocalDateTime endDate);
}