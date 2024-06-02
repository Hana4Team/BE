package com.hana.ddok.account.util;

import java.util.Random;

public class AccountNumberGenerator {
    public static String generateAccountNumber() {
        StringBuilder sb = new StringBuilder("880-");
        Random random = new Random();
        for (int i = 0; i < 11; i++) {
            sb.append(random.nextInt(10)); // 0~9
            if (i == 5) {
                sb.append("-");
            }
        }
        return sb.toString();   // 880-XXXXXX-XXXXX
    }
}