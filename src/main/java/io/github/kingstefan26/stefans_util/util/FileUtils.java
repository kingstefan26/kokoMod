/*
 * Copyright (c) 2021. All copyright reserved
 */

package io.github.kingstefan26.stefans_util.util;

import io.github.kingstefan26.stefans_util.core.clickGui.components.component;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {
    public static final String configFileName = "stefan_util.txt";
    public static String configDirectoryPath;
    public static String configFullPath;
    public static String stefans_utilPath;

    static {
        FileUtils.configDirectoryPath = System.getProperty("user.dir");
        FileUtils.configFullPath = FileUtils.configDirectoryPath + File.separator + FileUtils.configFileName;
        FileUtils.stefans_utilPath = FileUtils.configDirectoryPath + File.separator + "stefanUtil" + File.separator;
    }

    public static void makeSureDiractoriesExist(String dirpath) throws IOException {
        Path directoriespath = Paths.get(dirpath);
        Files.createDirectories(directoriespath);
    }


    public static File getFileAtPath(String path) throws IOException {

//        if(!file.exists()) {
//            Path directoriespath = Paths.get(path);
//
//            //java.nio.file.Files;
//
//            Files.createDirectories(directoriespath);
//
//            System.out.println("Directory is created!");
//        }

        return new File(path);
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


    public static void writeToFile(String dir, String Text) {
        try {
            FileWriter filewriter = new FileWriter(dir);
            Throwable throwable = null;

            try {
                filewriter.write(Text);
            } catch (Throwable throwable2) {
                throwable = throwable2;
                throw throwable2;
            } finally {
                if (filewriter != null) {
                    if (throwable != null) {
                        try {
                            filewriter.close();
                        } catch (Throwable throwable1) {
                            throwable.addSuppressed(throwable1);
                        }
                    } else {
                        filewriter.close();
                    }
                }
            }
        } catch (IOException ignored) {
        }
    }

    public static String readFile(String Path) {
        try {
            FileReader filereader = new FileReader(Path);
            Throwable throwable = null;
            String s1;

            try {
                int i = filereader.read();
                String s;

                for (s = ""; i != -1; s = s + i) {
                    System.out.print((char) i);
                    i = filereader.read();
                }

                s1 = s;
            } catch (Throwable throwable2) {
                throwable = throwable2;
                throw throwable2;
            } finally {
                if (filereader != null) {
                    if (throwable != null) {
                        try {
                            filereader.close();
                        } catch (Throwable throwable1) {
                            throwable.addSuppressed(throwable1);
                        }
                    } else {
                        filereader.close();
                    }
                }
            }

            return s1;
        } catch (IOException ignored) {
        }
        return "";
    }

    public static InputStream getFileFromResourceAsStream(String fileName) {
// get a file from the resources folder
// works everywhere, IDEA, unit test and JAR file.

        // The class loader that loaded the class
        ClassLoader classLoader = component.class.getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(fileName);

        // the stream holding the file content
        if (inputStream == null) {
            throw new IllegalArgumentException("file not found! " + fileName);
        } else {
            return inputStream;
        }

    }

    public static void emptyFolder(File folder) {
        File[] files = folder.listFiles();
        if (files != null) { //some JVMs return null for empty dirs
            for (File f : files) {
                if (f.isDirectory()) {
                    emptyFolder(f);
                } else {
                    f.delete();
                }
            }
        }
    }
}
