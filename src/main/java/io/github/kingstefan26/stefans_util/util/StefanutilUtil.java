package io.github.kingstefan26.stefans_util.util;

import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

public class StefanutilUtil {
    public static String dateFormat = "dd-MM-yyyy hh:mm";
    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);

    private StefanutilUtil() {
    }

    /**
     * Function to remove duplicates from an ArrayList
     */
    public static <T> ArrayList<T> removeDuplicates(ArrayList<T> list) {
        ArrayList<T> newList = new ArrayList<>();
        for (T element : list) {
            if (!newList.contains(element)) {
                newList.add(element);
            }
        }
        return newList;
    }


    /**
     * Generic rotate array clockwise by 90 degres
     * @param clas the class of the array
     * @param array the array itself
     * @param <T> the rotated array
     * @return the rotated array
     */
    public static <T> T[][] rotateArray90clockwise(Class<T> clas, T[][] array) {
        T[][] target = (T[][]) java.lang.reflect.Array.newInstance(
                clas, array[0].length, array.length);

        for (int i = 0; i < target.length; i++) {
            for (int j = 0; j < target[i].length; j++) {
                target[i][j] = array[(target[i].length - 1) - j][i];
            }
        }

        return target;
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

    public static int everyXTicks(int ticks, TickEvent.ClientTickEvent e, int counter, Runnable callbalback) {
        if (e.phase != TickEvent.Phase.START)
            return counter;
        counter++;
        if (counter % ticks == 0) {
            callbalback.run();
            return 0;
        }
        return counter;
    }

    public static int every20Ticks(TickEvent.ClientTickEvent e, int counter, Runnable callbalback) {
        if (e.phase != TickEvent.Phase.START)
            return counter;
        counter++;
        if (counter % 20 == 0) {
            callbalback.run();
            return 0;
        }
        return counter;
    }

    public static String ConvertMilliSecondsToFormattedDate(String milliSeconds) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Long.parseLong(milliSeconds));
        return simpleDateFormat.format(calendar.getTime());
    }

    public static String ConvertMilliSecondsToFormattedDate(Long milliSeconds) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return simpleDateFormat.format(calendar.getTime());
    }

    public static String getMD5Hash(String str) throws NoSuchAlgorithmException {
        // hash string into byte array
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] hashbytes = md.digest(str.getBytes());

        // convert byte array into hex string and return
        StringBuilder stringBuffer = new StringBuilder();
        for (byte hashbyte : hashbytes) {
            stringBuffer.append(Integer.toString((hashbyte & 0xff) + 0x100, 16)
                    .substring(1));
        }
        return stringBuffer.toString();
    }

    public static String getStringHash(String str, String hashType) throws NoSuchAlgorithmException {
        // hash string into byte array
        MessageDigest md = MessageDigest.getInstance(hashType);
        byte[] hashbytes = md.digest(str.getBytes());

        // convert byte array into hex string and return
        StringBuilder stringBuffer = new StringBuilder();
        for (byte hashbyte : hashbytes) {
            stringBuffer.append(Integer.toString((hashbyte & 0xff) + 0x100, 16)
                    .substring(1));
        }
        return stringBuffer.toString();
    }
}
