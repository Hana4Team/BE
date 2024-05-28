package com.hana.ddok.account.service;

import com.hana.ddok.account.dto.res.AccountFindAllRes;
import com.hana.ddok.account.repository.AccountRepository;
import com.hana.ddok.users.domain.Users;
import com.hana.ddok.users.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final UsersRepository usersRepository;

    @Transactional(readOnly = true)
    public List<AccountFindAllRes> accountFindAll() {
        Users users = usersRepository.findById(1L).get();    // TODO : 시큐리티 적용 시 변경
        List<AccountFindAllRes> accountFindAllResList = accountRepository.findAllByUsers(users).stream()
                .map(AccountFindAllRes::new)
                .toList();
        return accountFindAllResList;
    }
}
