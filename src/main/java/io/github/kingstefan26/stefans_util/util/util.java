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
}
