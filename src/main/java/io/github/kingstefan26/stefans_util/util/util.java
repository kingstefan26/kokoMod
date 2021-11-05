package io.github.kingstefan26.stefans_util.util;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

public class util {
    /**
     * Function to remove duplicates from an ArrayList
     */
    public static <T> ArrayList<T> removeDuplicates(@NotNull ArrayList<T> list) {
        ArrayList<T> newList = new ArrayList<>();
        for (T element : list) {
            if (!newList.contains(element)) {
                newList.add(element);
            }
        }
        return newList;
    }

    /**
     * Equivalent to JS set timeout
     *
     * @param runnable the runable that will execute after delay Milliseconds
     * @param delay    the delay in Milliseconds before executing a task
     */
    public static void setTimeout(Runnable runnable, int delay) {
        new Thread(() -> {
            try {
                Thread.sleep(delay);
                runnable.run();
            } catch (Exception e) {
                System.err.println(e);
            }
        }).start();
    }

    /**
     * do thing
     *
     * @param service  the thing that gives threads
     * @param callable the thging that does stuff eg download pic
     * @param callback the thing that executes with stuff
     * @param <T>      bs
     */
    public static <T> void submit(ExecutorService service,
                                  Callable<T> callable,
                                  Consumer<T> callback) {
        service.submit(() -> {
            try {
                callback.accept(callable.call());
            } catch (Throwable t) {
                // log the Throwable
            }
        });
    }



    /**
     * do thing
     *
     * @param service  the thing that gives threads
     * @param callable the thging that does stuff eg download pic
     * @param callback the thing that executes with stuff
     * @param recheckDelay how often does it check for results in ms
     * @param <T>      bs
     */
    public static <T> void submitA(ExecutorService service,
                                  Callable<T> callable,
                                  Consumer<T> callback,
                                   int recheckDelay) {

        Timer timer = new Timer();
        AtomicReference<T> t = new AtomicReference<>();
        AtomicBoolean finished = new AtomicBoolean(false);

        service.submit(() -> {
            try {
                t.set(callable.call());
                finished.set(true);
            } catch (Throwable th) {
                // log the Throwable
            }
        });

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if(finished.get()){
                    callback.accept(t.get());
                    timer.cancel();
                }
            }
        }, recheckDelay, 1);


    }


    /**
     * do thing
     * @param callable the thging that does stuff eg download pic
     * @param callback the thing that executes with stuff
     * @param <T>      bs
     */
    public static <T> void submitSync(Callable<T> callable, Consumer<T> callback) {
        try {
            callback.accept(callable.call());
        } catch (Throwable t) {
            // log the Throwable
        }
    }
}
