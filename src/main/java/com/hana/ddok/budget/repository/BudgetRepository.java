package com.hana.ddok.budget.repository;

import com.hana.ddok.budget.domain.Budget;
import org.springframework.data.jpa.repository.JpaRepository;


public interface BudgetRepository extends JpaRepository<Budget, Long> {
}
