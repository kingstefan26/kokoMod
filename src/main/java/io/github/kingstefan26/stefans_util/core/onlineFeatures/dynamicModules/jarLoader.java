/*
 * Copyright (c) 2021. All copyright reserved
 */

package io.github.kingstefan26.stefans_util.core.onlineFeatures.dynamicModules;

import io.github.kingstefan26.stefans_util.core.module.moduleFrames.basicModule;
import io.github.kingstefan26.stefans_util.main;
import io.github.kingstefan26.stefans_util.service.impl.chatService;
import io.github.kingstefan26.stefans_util.util.file;
import io.github.kingstefan26.stefans_util.util.fileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.jar.Attributes;
import java.util.jar.JarInputStream;
import java.util.jar.Manifest;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class jarLoader {
    static Logger logger = LogManager.getLogger("jarModuleLoader");

    static boolean didroutine = false;

    public static void loadJar(String jarPath) {
        if(!didroutine) {
            try {
                fileUtils.makeSureDiractoriesExist(file.configDirectoryPath + File.separator + "assets");
            } catch (IOException e) {
                e.printStackTrace();
            }
            didroutine = true;
        }


        if(!(new File(jarPath)).exists()){
            logger.info("cannot find " + jarPath);
            chatService.queueClientChatMessage("cannot find " + jarPath);
            return;
        }

        try{

            File initialFile = new File(jarPath);
            InputStream targetStream = new FileInputStream(initialFile);

            JarInputStream jarStream = new JarInputStream(targetStream);
            Manifest mf = jarStream.getManifest();
            Attributes mainAttributes = mf.getMainAttributes();

            for (Map.Entry<Object, Object> objectObjectEntry : mainAttributes.entrySet()) {
                System.out.println(objectObjectEntry.getKey());
                System.out.println(objectObjectEntry.getValue());
            }

        } catch (Exception ignored){}


        List<String> classNames = new ArrayList<>();
        try {
            ZipInputStream zip = new ZipInputStream(new FileInputStream(jarPath));
            for (ZipEntry entry = zip.getNextEntry(); entry != null; entry = zip.getNextEntry()) {
                if (!entry.isDirectory() && entry.getName().endsWith(".class")) {
                    // This ZipEntry represents a class. Now, what class does it represent?
                    String className = entry.getName().replace('/', '.'); // including ".class"
                    classNames.add(className.substring(0, className.length() - ".class".length()));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        logger.info("found " + classNames.size() + " classes in jar file listing");
        classNames.forEach(x -> logger.info(x));


        try {
            //the part that loades the jar
            File file = new File(jarPath);
            URL url = file.toURI().toURL();

            URLClassLoader classLoader = (URLClassLoader) main.class.getClassLoader();

            Method method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
            method.setAccessible(true);
            method.invoke(classLoader, url);

            ArrayList<Class<?>> classes = new ArrayList<>();

            for(String className : classNames){
                classes.add(main.class.getClassLoader().loadClass(className));
            }

            // now we try to make a object out of that class

            for(Class<?> clazz : classes){
                if(basicModule.class.isAssignableFrom(clazz)){
                    final basicModule module = (basicModule) clazz.getConstructor().newInstance();

                    logger.info(module.getName());

                    // try to load the stpuid thing
                    chatService.queueCleanChatMessage("loading " + module.getClass().getName());
                    module.getClass().getMethod("onLoad").invoke(module);
                }
            }


        } catch (MalformedURLException | NoSuchMethodException | InvocationTargetException | IllegalAccessException | ClassNotFoundException | InstantiationException e) {
            e.printStackTrace();
        }
    }


    public static String getJarbuildNumber(String jarPath) {
        String result = null;

        if ((new File(jarPath)).exists()) {
            try {
                JarInputStream jarStream = new JarInputStream(new FileInputStream(jarPath));
                Attributes mainAttributes = jarStream.getManifest().getMainAttributes();
                String value = mainAttributes.getValue("relasedate");

                if (value != null) {
                    result = value.replaceAll("[^\\x00-\\x7F]", "").replace("\n", "").replace("\r", "").replace(" ", "");
                } else {
                    System.out.println("an error has ripped ass");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return result;
    }


    public static String getOwnReleaseDate() {
        String result = null;
        boolean finished = false;
        Class<?> clazz = jarLoader.class;
        String className = clazz.getSimpleName() + ".class";
        String classPath = null;
        try {
            classPath = clazz.getResource(className).toString();
        } catch (NullPointerException e) {
            e.printStackTrace();
            finished = true;
        }
        if (!finished) {// Class not from JAR
            if (classPath.startsWith("jar")) {
                String manifestPath = classPath.substring(0, classPath.lastIndexOf("!") + 1) +
                        "/META-INF/MANIFEST.MF";
                Manifest manifest = null;
                try {
                    manifest = new Manifest(new URL(manifestPath).openStream());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Attributes attr = null;
                if (manifest != null) {
                    attr = manifest.getMainAttributes();
                }
                if (attr != null) {
                    result = attr.getValue("relasedate");
                }
            }

        }


        return result;
    }


}
