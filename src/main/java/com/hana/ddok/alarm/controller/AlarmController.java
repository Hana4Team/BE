package com.hana.ddok.alarm.controller;

import com.hana.ddok.alarm.dto.AlarmSaveReq;
import com.hana.ddok.alarm.dto.AlarmSaveRes;
import com.hana.ddok.alarm.service.AlarmService;
import com.hana.ddok.users.domain.UsersDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/alarm")
public class AlarmController {
    private final AlarmService alarmService;

    @PostMapping("")
    public ResponseEntity<AlarmSaveRes> alarmSave(@AuthenticationPrincipal UsersDetails usersDetails,
                                                  @RequestBody AlarmSaveReq req) {
        return ResponseEntity.ok(alarmService.alarmSave(usersDetails.getUsername(), req));
    }
}
