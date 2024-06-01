package com.hana.ddok.users.dto;

public record UsersLoginRes(Boolean success, String name, String phoneNumber, Integer step, Integer stepStatus, String token) {
}
