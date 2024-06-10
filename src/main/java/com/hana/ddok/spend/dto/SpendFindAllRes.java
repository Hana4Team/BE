package com.hana.ddok.spend.dto;

import java.util.List;

public record SpendFindAllRes(
        Long sum,
        List<SpendFindByTypeRes> spendFindByTypeResList
) { }