package io.github.kingstefan26.stefans_util.util;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

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
     * @param runnable the runable that will execute after delay Milliseconds
     * @param delay the delay in Milliseconds before executing a task
     */
    public static void setTimeout(Runnable runnable, int delay){
        new Thread(() -> {
            try {
                Thread.sleep(delay);
                runnable.run();
            }
            catch (Exception e){
                System.err.println(e);
            }
        }).start();
    }

}
