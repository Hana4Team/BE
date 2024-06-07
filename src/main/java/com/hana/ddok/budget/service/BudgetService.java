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

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class BudgetService {
    private final BudgetRepository budgetRepository;
    private final UsersRepository usersRepository;
    private final UsersService usersService;

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

        Optional<Budget> optionalBudget = budgetRepository.findByUsers(users);
        Budget budget = null;
        Boolean isInitialUpdate = false;
        if (optionalBudget.isPresent()) {   // 1단계 성공 이후
            budget = optionalBudget.get();
            budget.setSum(budgetUpdateReq.sum());
        } else {    // 1단계 성공 이전 => 1단계 성공
            isInitialUpdate = true;
            budget = budgetRepository.save(Budget.builder()
                    .sum(budgetUpdateReq.sum())
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
            );
            usersService.usersMove(users.getPhoneNumber());
        }

        return new BudgetUpdateRes(isInitialUpdate);
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