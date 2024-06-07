package com.hana.ddok.transaction.repository;

import com.hana.ddok.account.domain.Account;
import com.hana.ddok.transaction.domain.Transaction;
import com.hana.ddok.transaction.domain.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findAllByTypeInAndSenderAccountAndCreatedAtBetween(List<TransactionType> typeList, Account account, LocalDateTime startDateTime, LocalDateTime endDateTime);
    List<Transaction> findAllByTypeInAndRecipientAccountAndCreatedAtBetween(List<TransactionType> typeList, Account account, LocalDateTime startDateTime, LocalDateTime endDateTime);
    Optional<Transaction> findFirstByRecipientAccountOrderByCreatedAt(Account account);

}
