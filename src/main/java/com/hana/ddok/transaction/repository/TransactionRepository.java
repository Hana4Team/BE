package com.hana.ddok.transaction.repository;

import com.hana.ddok.account.domain.Account;
import com.hana.ddok.transaction.domain.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findAllBySenderAccountAndCreatedAtBetween(Account account, LocalDateTime startDateTime, LocalDateTime endDateTime);
    List<Transaction> findAllByRecipientAccountAndCreatedAtBetween(Account account, LocalDateTime startDateTime, LocalDateTime endDateTime);
    Optional<Transaction> findFirstByRecipientAccountOrderByCreatedAt(Account account);

}
