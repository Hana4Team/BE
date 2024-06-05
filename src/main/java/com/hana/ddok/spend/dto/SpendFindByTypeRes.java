package com.hana.ddok.spend.dto;

import com.hana.ddok.spend.domain.SpendType;

public record SpendFindByTypeRes(
        SpendType type,
        Integer amount
) {
}