package com.hana.ddok.alarm.dto;

import com.hana.ddok.alarm.domain.Alarm;
import com.hana.ddok.users.domain.Users;

public record AlarmSaveReq(String contents) {
    public static Alarm toEntity(AlarmSaveReq req, Users user) {
        return Alarm.builder().
                contents(req.contents).
                users(user).
                build();
    }
}
