package com.sigmadevs.testtask.app.service;

import org.springframework.stereotype.Service;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class TimerService {
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public void scheduleTask(Runnable task, long delay, TimeUnit timeUnit) {
        scheduler.schedule(task, delay, timeUnit);
    }
}