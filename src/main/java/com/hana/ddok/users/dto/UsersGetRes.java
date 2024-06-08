package com.hana.ddok.users.dto;

import com.hana.ddok.users.domain.Users;

public record UsersGetRes(
        String name,
        String phoneNumber,
        Integer step,
        Integer stepStatus
) {
    public UsersGetRes(Users users) {
        this(
                users.getName(),
                users.getPhoneNumber(),
                users.getStep(),
                users.getStepStatus().ordinal()
        );
    }

}
