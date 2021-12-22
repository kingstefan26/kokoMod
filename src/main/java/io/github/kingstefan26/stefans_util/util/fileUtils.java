/*
 * Copyright (c) 2021. All copyright reserved
 */

package io.github.kingstefan26.stefans_util.util;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class fileUtils {
    public static void makeSureDiractoriesExist(String dirpath) throws IOException {
        Path directoriespath = Paths.get(dirpath);
        Files.createDirectories(directoriespath);
    }


    public static File getFileAtPath(String path) throws IOException {
        final File file = new File(path);

//        if(!file.exists()) {
//            Path directoriespath = Paths.get(path);
//
//            //java.nio.file.Files;
//
//            Files.createDirectories(directoriespath);
//
//            System.out.println("Directory is created!");
//        }

        if (file.createNewFile()) {
            System.out.println("File created: " + file.getName());
            return file;
        } else {
            System.out.println("File already exists.");
            return file;
        }
    }

    public static String getFileTextContents(File file) throws IOException {
        FileInputStream inputStream = new FileInputStream(file);
        String everything = IOUtils.toString(inputStream);
        inputStream.close();
        return everything;
    }

    public static void writeTextToFile(File file, String content, boolean append) throws IOException {
        FileWriter myWriter = new FileWriter(file, append);
        myWriter.write(content);
        myWriter.close();
    }

    public static List<String> mapFolder(String path, boolean includeEmptyFolders) {
        List<String> map = new ArrayList<String>();
        List<String> unmappedDirs = new ArrayList<String>();
        File[] items = new File(path).listFiles();

        if (!path.substring(path.length() - 1).equals("/")) {
            path += "/";
        }

        if (items != null) {
            for (File item : items) {
                if (item.isFile())
                    map.add(path + item.getName());
                else
                    unmappedDirs.add(path + item.getName());
            }

            if (!unmappedDirs.isEmpty()) {
                for (String folder : unmappedDirs) {
                    List<String> temp = mapFolder(folder, includeEmptyFolders);
                    if (!temp.isEmpty()) {
                        map.addAll(temp);
                    } else if (includeEmptyFolders)
                        map.add(folder + "/");
                }
            }
        }
        return map;
    }


}
