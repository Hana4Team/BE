package com.hana.ddok.alarm.dto;

import java.time.LocalDateTime;

public record AlarmGetRes(String contents, LocalDateTime createdAt) {
}
