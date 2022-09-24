/*
 * Copyright (c) 2022. All copyright reserved
 */

package io.github.kingstefan26.stefans_util.core.module;

import com.google.common.reflect.ClassPath;
import io.github.kingstefan26.stefans_util.core.module.moduleframes.BasicModule;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class ModuleUtil {

    private ModuleUtil() {
        throw new SecurityException("This cant be called");
    }

    private static final Logger logger = LogManager.getLogger("kokomod-ModuleUtil");


    public static List<BasicModule> loadModules(List<String> moduleindex) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        ArrayList<BasicModule> loadedModules = new ArrayList<>();
        moduleindex = removeDuplicates(moduleindex);
        moduleindex = removeDuplicates(moduleindex);
        for (String moduleclassname : moduleindex) {
            Class<?> clazz = Class.forName(moduleclassname);
            boolean exists = false;

            for (BasicModule bm : loadedModules) {
                if (bm.getClass().getName().equals(clazz.getName())) {
                    exists = true;
                    logger.info("module " + bm.getName() + " was already loaded (probably web module)");
                    break;
                }
            }

            if (!exists) {
                Constructor<?> ctor = clazz.getConstructor();
                Object object = ctor.newInstance();
                loadedModules.add((BasicModule) object);
                ((BasicModule) object).onLoad();
                logger.info("loaded module: " + ((BasicModule) object).getName());
            }


        }

        return loadedModules;
    }

    /**
     * Function to remove duplicates from an ArrayList
     */
    public static <T> List<T> removeDuplicates(List<T> list) {
        List<T> newList = new ArrayList<>();
        for (T element : list) {
            if (!newList.contains(element)) {
                newList.add(element);
            }
        }
        return newList;
    }

    /**
     * IM NOT DOCUMENTIGN THIS SHIT HAHA LLL
     */
    public static List<String> findModuleNames() throws IOException {
        List<String> clazzlist = new ArrayList<>();
        final ClassLoader loader = Thread.currentThread().getContextClassLoader();

        for (final ClassPath.ClassInfo info : ClassPath.from(loader).getTopLevelClasses()) {
            if (info.getName().startsWith("io.github.kingstefan26.stefans_util.module")) {
                final Class<?> clazz = info.load();
                if (BasicModule.class.isAssignableFrom(clazz) && !clazzlist.contains(clazz.getName())) {
                    logger.info("found module: " + clazz.getName());
                    clazzlist.add(clazz.getName());
                }
            }

        }

        clazzlist = removeDuplicates(clazzlist);

        return new ArrayList<>(clazzlist);
    }


}