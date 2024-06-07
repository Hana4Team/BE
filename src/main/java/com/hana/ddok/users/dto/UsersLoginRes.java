package com.hana.ddok.users.dto;

import com.hana.ddok.users.domain.UsersStepStatus;

public record UsersLoginRes(Boolean success, String name, String phoneNumber, Integer step, UsersStepStatus stepStatus, String token) {
}
