package com.hana.ddok.users.dto;

import com.hana.ddok.users.domain.Users;
import com.hana.ddok.users.domain.UsersStepStatus;

public record UsersMissionRes(
        String phoneNumber,
        Integer step,
        UsersStepStatus stepStatus
) {
    public UsersMissionRes(Users users) {
        this(
                users.getPhoneNumber(),
                users.getStep(),
                users.getStepStatus()
        );
    }
}
