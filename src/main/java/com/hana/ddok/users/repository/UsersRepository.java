package com.hana.ddok.users.repository;

import com.hana.ddok.users.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository<Users, Long> {
    Users findByPhoneNumber(String phoneNumber);
}
