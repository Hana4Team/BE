package com.hana.ddok.budget.service;

import com.hana.ddok.budget.domain.Budget;
import com.hana.ddok.budget.dto.BudgetUpdateReq;
import com.hana.ddok.budget.dto.BudgetUpdateRes;
import com.hana.ddok.budget.repository.BudgetRepository;
import com.hana.ddok.users.domain.Users;
import com.hana.ddok.users.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BudgetService {
    private final BudgetRepository budgetRepository;
    private final UsersRepository usersRepository;

    @Transactional
    public BudgetUpdateRes budgetUpdate(BudgetUpdateReq budgetUpdateReq, String phoneNumber) {
        Users users = usersRepository.findByPhoneNumber(phoneNumber);

        Budget budget = budgetRepository.findByUsers(users)
                .orElseGet(() -> budgetRepository.save(Budget.builder()
                                .sum(0)
                                .shopping(0)
                                .food(0)
                                .traffic(0)
                                .hospital(0)
                                .fee(0)
                                .education(0)
                                .leisure(0)
                                .society(0)
                                .daily(0)
                                .overseas(0)
                                .users(users)
                                .build()
                        )
                );

        budget.setSum(budgetUpdateReq.sum());
        return new BudgetUpdateRes(budget);
    }
}