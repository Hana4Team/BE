package com.hana.ddok.users.dto;

import com.hana.ddok.users.domain.UsersStepStatus;

public record UsersGetRes(String name, String phoneNumber, Integer step, UsersStepStatus stepStatus) {
}
