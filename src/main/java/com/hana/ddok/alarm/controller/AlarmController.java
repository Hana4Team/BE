package com.hana.ddok.alarm.controller;

import com.hana.ddok.alarm.dto.AlarmGetRes;
import com.hana.ddok.alarm.dto.AlarmSaveReq;
import com.hana.ddok.alarm.dto.AlarmSaveRes;
import com.hana.ddok.alarm.service.AlarmService;
import com.hana.ddok.users.domain.UsersDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1")
@Tag(name = "Alarm", description = "알림 API")
public class AlarmController {
    private final AlarmService alarmService;

    @PostMapping("/alarm")
    @Operation(summary = "알림 저장")
    public ResponseEntity<AlarmSaveRes> alarmSave(@AuthenticationPrincipal UsersDetails usersDetails,
                                                  @RequestBody AlarmSaveReq alarmSaveReq) {
        AlarmSaveRes alarmSaveRes = alarmService.alarmSave(usersDetails.getUsername(), alarmSaveReq);
        return ResponseEntity.ok(alarmSaveRes);
    }

    @GetMapping("/alarm")
    @Operation(summary = "알림 전체조회")
    public ResponseEntity<List<AlarmGetRes>> alarmGet(@AuthenticationPrincipal UsersDetails usersDetails) {
        List<AlarmGetRes> alarmGetResList = alarmService.alarmGet(usersDetails.getUsername());
        return ResponseEntity.ok(alarmGetResList);
    }
}
