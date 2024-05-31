package com.hana.ddok.alarm.service;

import com.hana.ddok.alarm.domain.Alarm;
import com.hana.ddok.alarm.dto.AlarmGetRes;
import com.hana.ddok.alarm.dto.AlarmSaveReq;
import com.hana.ddok.alarm.dto.AlarmSaveRes;
import com.hana.ddok.alarm.repository.AlarmRepository;
import com.hana.ddok.users.domain.Users;
import com.hana.ddok.users.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AlarmService {
    private final AlarmRepository alarmRepository;
    private final UsersRepository usersRepository;

    public AlarmSaveRes alarmSave(String phoneNumber, AlarmSaveReq req) {
        Users user = usersRepository.findByPhoneNumber(phoneNumber);
        alarmRepository.save(AlarmSaveReq.toEntity(req, user));
        return new AlarmSaveRes(true);
    }

    public List<AlarmGetRes> alarmGet(String phoneNumber) {
        Users user = usersRepository.findByPhoneNumber(phoneNumber);
        List<Alarm> alarms = alarmRepository.findTop10ByUsersOrderByCreatedAtDesc(user);
        return alarms.stream()
                .map(alarm -> new AlarmGetRes(alarm.getContents(), alarm.getCreatedAt()))
                .collect(Collectors.toList());
    }
}
