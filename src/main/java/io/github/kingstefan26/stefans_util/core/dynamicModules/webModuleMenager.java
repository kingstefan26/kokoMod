/*
 * Copyright (c) 2021. All copyright reserved
 */

package io.github.kingstefan26.stefans_util.core.dynamicModules;

import io.github.kingstefan26.stefans_util.core.rewrite.module.moduleFrames.basicModule;
import io.github.kingstefan26.stefans_util.main;
import io.github.kingstefan26.stefans_util.service.impl.chatService;
import io.github.kingstefan26.stefans_util.util.file;
import io.github.kingstefan26.stefans_util.util.fileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class webModuleMenager {
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

}
