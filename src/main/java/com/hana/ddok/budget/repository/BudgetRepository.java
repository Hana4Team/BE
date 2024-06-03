package com.hana.ddok.budget.repository;

import com.hana.ddok.account.domain.Account;
import com.hana.ddok.budget.domain.Budget;
import com.hana.ddok.users.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface BudgetRepository extends JpaRepository<Budget, Long> {
    Optional<Budget> findByUsers(Users users);
}
