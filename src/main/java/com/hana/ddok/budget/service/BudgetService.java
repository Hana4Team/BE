package com.hana.ddok.budget.service;

import com.hana.ddok.budget.domain.Budget;
import com.hana.ddok.budget.dto.*;
import com.hana.ddok.budget.exception.BudgetNotFound;
import com.hana.ddok.budget.repository.BudgetRepository;
import com.hana.ddok.users.domain.Users;
import com.hana.ddok.users.exception.UsersNotFound;
import com.hana.ddok.users.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class BudgetService {
    private final BudgetRepository budgetRepository;
    private final UsersRepository usersRepository;

    @Transactional(readOnly = true)
    public BudgetFindRes budgetFind(String phoneNumber) {
        Users users = usersRepository.findByPhoneNumber(phoneNumber)

                .orElseThrow(() -> new UsersNotFound());
        Budget budget = budgetRepository.findByUsers(users)
                .orElseThrow(() -> new BudgetNotFound());

        return new BudgetFindRes(budget);
    }

    @Transactional
    public BudgetUpdateRes budgetUpdate(BudgetUpdateReq budgetUpdateReq, String phoneNumber) {
        Users users = usersRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new UsersNotFound());

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

    @Transactional(readOnly = true)
    public BudgetFindByCategoryRes budgetFindByCategory(String phoneNumber) {
        Users users = usersRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new UsersNotFound());
        Budget budget = budgetRepository.findByUsers(users)
                .orElseThrow(() -> new BudgetNotFound());

        return new BudgetFindByCategoryRes(budget);
    }

    @Transactional
    public BudgetByCategoryUpdateRes budgetByCategoryUpdate(BudgetByCategoryUpdateReq budgetByCategoryUpdateReq, String phoneNumber) {
        Users users = usersRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new UsersNotFound());
        Budget budget = budgetRepository.findByUsers(users)
                .orElseThrow(() -> new BudgetNotFound());

        budget = Budget.builder()
                .budgetId(budget.getBudgetId())
                .sum(budget.getSum())
                .shopping(budgetByCategoryUpdateReq.shopping())
                .food(budgetByCategoryUpdateReq.food())
                .traffic(budgetByCategoryUpdateReq.traffic())
                .hospital(budgetByCategoryUpdateReq.hospital())
                .fee(budgetByCategoryUpdateReq.fee())
                .education(budgetByCategoryUpdateReq.education())
                .leisure(budgetByCategoryUpdateReq.leisure())
                .society(budgetByCategoryUpdateReq.society())
                .daily(budgetByCategoryUpdateReq.daily())
                .overseas(budgetByCategoryUpdateReq.overseas())
                .users(users)
                .build();
        budgetRepository.save(budget);

        return new BudgetByCategoryUpdateRes(budget);
    }
}