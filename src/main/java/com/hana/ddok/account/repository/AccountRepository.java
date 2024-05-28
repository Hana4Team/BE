package com.hana.ddok.account.repository;

import com.hana.ddok.account.domain.Account;
import com.hana.ddok.users.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Long> {
    List<Account> findAllByUsers(Users users);
}
