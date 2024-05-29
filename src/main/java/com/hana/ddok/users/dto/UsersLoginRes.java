package com.hana.ddok.users.dto;

public record UsersLoginRes(String name, String phoneNumber, Integer step, Integer stepStatus, String token) {
}
