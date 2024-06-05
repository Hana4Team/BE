package com.hana.ddok.users.repository;

import com.hana.ddok.users.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface UsersRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByPhoneNumber(String phoneNumber);

    @Modifying
    @Transactional
    @Query("UPDATE Users u SET u.readNews = false WHERE u.readNews = true")
    void resetReadNews();
}
