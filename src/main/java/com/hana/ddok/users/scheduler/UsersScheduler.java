package com.hana.ddok.users.scheduler;

import com.hana.ddok.users.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UsersScheduler {
    private final UsersRepository usersRepository;

    // 매일 자정에 실행
    // @Scheduled(cron = "0 0 0 * * ?")
    @Scheduled(cron = "0 * * * * ?") // test용 1분
    public void updateNewsRead() {
        usersRepository.updateReadNews();
    }
}
