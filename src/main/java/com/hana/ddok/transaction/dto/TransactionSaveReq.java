package com.hana.ddok.transaction.dto;

public record TransactionReq(
        String senderTitle,
        String senderAccount,
        String recipientTitle,
        String recipientAccount,
        Long Balance
) {
}
