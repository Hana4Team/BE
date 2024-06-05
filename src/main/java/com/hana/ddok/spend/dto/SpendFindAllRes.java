package com.hana.ddok.spend.dto;

import java.util.List;

public record SpendFindAllRes(
        Integer sum,
        List<SpendFindByTypeRes> spendFindByTypeResList
) { }