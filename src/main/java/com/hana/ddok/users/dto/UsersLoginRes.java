package com.hana.ddok.users.dto;

import com.hana.ddok.users.domain.Users;

public record UsersLoginRes(
        Boolean success,
        String name,
        String phoneNumber,
        Integer step,
        Integer stepStatus,
        String token
) {
    public UsersLoginRes(Boolean success, Users users, String token) {
        this(
                success,
                users.getName(),
                users.getPhoneNumber(),
                users.getStep(),
                users.getStepStatus().ordinal(),
                token
        );
    }
}
