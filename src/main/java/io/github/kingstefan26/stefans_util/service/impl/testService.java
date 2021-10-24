package io.github.kingstefan26.stefans_util.service.impl;

import io.github.kingstefan26.stefans_util.service.Service;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class testService extends Service {

    public testService() {
        super("testService");
    }

    ScheduledExecutorService workerExecutorService;

    Logger logger = LogManager.getLogger("testService");

    Runnable executeRunnable = (() -> {
        logger.info("Test Service Executed");
    });

    @Override
    public void start() {
        workerExecutorService = Executors.newSingleThreadScheduledExecutor();
        workerExecutorService.scheduleAtFixedRate(executeRunnable, 5, 1, TimeUnit.SECONDS);
    }

    @Override
    public void stop() {
        workerExecutorService.shutdown();
    }
}
