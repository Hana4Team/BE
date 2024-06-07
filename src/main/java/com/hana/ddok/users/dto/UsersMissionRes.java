package com.hana.ddok.users.dto;

import com.hana.ddok.users.domain.UsersStepStatus;

public record UsersMissionRes(String phoneNumber, Integer step, UsersStepStatus stepStatus) {
}
