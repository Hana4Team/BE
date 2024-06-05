package com.hana.ddok.spend.dto;

import com.hana.ddok.transaction.domain.Transaction;

import java.time.LocalDate;

public record SpendFindAllRes(
        Integer sum,
        Integer shopping,
        Integer food,
        Integer traffic,
        Integer hospital,
        Integer fee,
        Integer education,
        Integer leisure,
        Integer society,
        Integer daily,
        Integer overseas
) { }