package com.hana.ddok.account.scheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

@Service
@RequiredArgsConstructor
public class AccountSavingSchedulerService {
    private final Map<Long, ScheduledFuture<?>> scheduledTasks = new HashMap<>();
    private final TaskScheduler taskScheduler;

    public void scheduleTaskForUser(Long userId, Runnable task, long delayInMilliseconds) {
        ScheduledFuture<?> scheduledTask = taskScheduler.schedule(task, new Date(System.currentTimeMillis() + delayInMilliseconds));
        scheduledTasks.put(userId, scheduledTask);
    }

    public void cancelScheduledTaskForUser(Long userId) {
        ScheduledFuture<?> scheduledTask = scheduledTasks.get(userId);
        if (scheduledTask != null) {
            scheduledTask.cancel(true);
        }
    }
}