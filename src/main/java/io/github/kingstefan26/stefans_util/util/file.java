package io.github.kingstefan26.stefans_util.util;

import io.github.kingstefan26.stefans_util.core.clickGui.components.component;

import java.io.*;

public class file {
    public static final String configFileName = "stefan_util.txt";
    public static final String configDirectoryPath;
    public static final String configFullPath;
    public static final String stefans_utilPath;

    static {
        configDirectoryPath = System.getProperty("user.dir");
        configFullPath = configDirectoryPath + File.separator + configFileName;
        stefans_utilPath = configDirectoryPath + File.separator + "stefanUtil" + File.separator;
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

    public void startClient() {
        System.out.println(configDirectoryPath);
        String s3 = "";

        try {
            FileReader filereader = new FileReader(configFullPath);
            Throwable throwable = null;

            try {
                char[] achar = new char[100];
                filereader.read(achar);

                for (char c0 : achar) {
                    s3 = s3 + c0;
                }
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
        } catch (FileNotFoundException exception) {
            this.writeToFile(configFullPath, "");
        } catch (Exception ignored) {
        }

        if (!s3.equals("")) {
            try {
                //TODO: default values
//                currentDelay = Integer.parseInt(s3.trim());
            } catch (Exception ignored) {
            }
        }
    }

    public void stopClient() {
        System.out.println(configDirectoryPath);
        String s2 = configDirectoryPath + File.separator + configFileName;
        //TODO: write config json here
//        this.writeto(s2, String.valueOf(currentDelay));
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
