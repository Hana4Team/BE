package com.hana.ddok.budget.service;

import com.hana.ddok.budget.domain.Budget;
import com.hana.ddok.budget.dto.*;
import com.hana.ddok.budget.exception.BudgetNotFound;
import com.hana.ddok.budget.repository.BudgetRepository;
import com.hana.ddok.users.domain.Users;
import com.hana.ddok.users.exception.UsersNotFound;
import com.hana.ddok.users.repository.UsersRepository;
import com.hana.ddok.users.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@Service
@RequiredArgsConstructor
public class BudgetService {
    private final BudgetRepository budgetRepository;
    private final UsersRepository usersRepository;
    private final UsersService usersService;

    @Transactional(readOnly = true)
    public BudgetSumFindRes budgetSumFind(String phoneNumber) {
        Users users = usersRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new UsersNotFound());

        Budget budget = users.getBudget();
        if (budget == null) {
            throw new BudgetNotFound();
        }

        return new BudgetSumFindRes(budget);
    }

    @Transactional
    public BudgetSumUpdateRes budgetSumUpdate(BudgetSumUpdateReq budgetSumUpdateReq, String phoneNumber) {
        Users users = usersRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new UsersNotFound());

        Budget budget = users.getBudget();
        boolean isInitialUpdate = false;

        if (budget == null) {
            isInitialUpdate = true;
            budget = Budget.builder()
                    .sum(budgetSumUpdateReq.sum())
                    .shopping(0L)
                    .food(0L)
                    .traffic(0L)
                    .hospital(0L)
                    .fee(0L)
                    .education(0L)
                    .leisure(0L)
                    .society(0L)
                    .daily(0L)
                    .overseas(0L)
                    .build();
            budgetRepository.save(budget);
            users.updateBudget(budget);
            usersService.usersMove(users.getPhoneNumber());
        } else {
            budget.updateSum(budgetSumUpdateReq.sum());
        }

        return new BudgetSumUpdateRes(isInitialUpdate);
    }

    @Transactional(readOnly = true)
    public BudgetFindByCategoryRes budgetFindByCategory(String phoneNumber) {
        Users users = usersRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new UsersNotFound());
        Budget budget = users.getBudget();
        if (budget == null) {
            throw new BudgetNotFound();
        }

        return new BudgetFindByCategoryRes(budget);
    }

    @Transactional
    public BudgetByCategoryUpdateRes budgetByCategoryUpdate(BudgetByCategoryUpdateReq budgetByCategoryUpdateReq, String phoneNumber) {
        Users users = usersRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new UsersNotFound());
        Budget budget = users.getBudget();
        if (budget == null) {
            throw new BudgetNotFound();
        }

        budget = Budget.builder()
                .budgetId(budget.getBudgetId())
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
                .build();
        budgetRepository.save(budget);
        users.updateBudget(budget);

        return new BudgetByCategoryUpdateRes(budget);
    }
}