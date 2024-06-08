package com.hana.ddok.moneybox.domain;

public enum MoneyboxType {
    PARKING("파킹"),
    EXPENSE("소비"),
    SAVING("저축");

    private final String korean;

    MoneyboxType(String korean) {
        this.korean = korean;
    }

    public String getKorean() {
        return korean;
    }
}
