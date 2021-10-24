package io.github.kingstefan26.stefans_util.service.impl;

import io.github.kingstefan26.stefans_util.service.Service;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class keyMovementService extends Service {
    public keyMovementService() {
        super("keyMovementEngine");
    }

    static int forwardKeyCode;
    static int backKeyCode;
    static int rightKeyCode;
    static int leftKeyCode;
    static int attackKeyCode;
    static int delayms;
    static Queue<String> taskQueue = new LinkedList<>();


    ScheduledExecutorService workerExecutorService;

    Runnable executeRunnable = (() -> {
        if (taskQueue.peek() != null) {
        }
    });

    @Override
    public void start() {
        attackKeyCode = mc.gameSettings.keyBindAttack.getKeyCode();
        leftKeyCode = mc.gameSettings.keyBindLeft.getKeyCode();
        rightKeyCode = mc.gameSettings.keyBindRight.getKeyCode();
        backKeyCode = mc.gameSettings.keyBindBack.getKeyCode();
        forwardKeyCode = mc.gameSettings.keyBindForward.getKeyCode();

        workerExecutorService = Executors.newSingleThreadScheduledExecutor();
        workerExecutorService.scheduleAtFixedRate(executeRunnable, 0, 50, TimeUnit.MILLISECONDS);
    }

    @Override
    public void stop() {

    }
}
