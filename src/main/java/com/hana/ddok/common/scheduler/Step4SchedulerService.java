package com.hana.ddok.common.scheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

@Service
@RequiredArgsConstructor
public class Step4SchedulerService {
    private final Map<Long, ScheduledFuture<?>> scheduledTasks = new HashMap<>();
    private final TaskScheduler taskScheduler;

    public void scheduleTaskForUser(Long userId, Runnable task, long delayInMilliseconds) {
        ScheduledFuture<?> scheduledTask = taskScheduler.schedule(task, new Date(System.currentTimeMillis() + delayInMilliseconds));
        scheduledTasks.put(userId, scheduledTask);
    }
}