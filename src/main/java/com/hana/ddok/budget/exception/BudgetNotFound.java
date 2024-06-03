package com.hana.ddok.budget.exception;

import com.hana.ddok.common.exception.EntityNotFoundException;

public class BudgetNotFound extends EntityNotFoundException {
    public BudgetNotFound() {
        super("예산을 찾을 수 없습니다.");
    }
}
