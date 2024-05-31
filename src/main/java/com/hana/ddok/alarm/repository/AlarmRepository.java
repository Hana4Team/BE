package com.hana.ddok.alarm.repository;

import com.hana.ddok.alarm.domain.Alarm;
import com.hana.ddok.users.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlarmRepository extends JpaRepository<Alarm, Long> {
    List<Alarm> findTop10ByUsersOrderByCreatedAtDesc(Users user);
}
