package io.github.kingstefan26.stefans_util.core.module.modulemenagers;

import com.google.common.reflect.ClassPath;
import io.github.kingstefan26.stefans_util.core.module.moduleframes.BasicModule;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Iterator;

public class moduleRegistery {
    public static moduleRegistery moduleRegistery_;

    public static moduleRegistery getModuleRegistery() {
        if (moduleRegistery_ == null) moduleRegistery_ = new moduleRegistery();
        return moduleRegistery_;
    }

    private final Logger logger;
    public ArrayList<String> productionModuleIndex;
    public ArrayList<BasicModule> loadedModules = new ArrayList<>();

    {
        logger = LogManager.getLogger("kokomod-moduleRegistry");
    }

    public void unloadAllModules() {
        try {
            Iterator<BasicModule> iter = loadedModules.iterator();

            while (iter.hasNext()) {
                BasicModule m = iter.next();
                m.onUnload();
                m = null;
                iter.remove();
            }
        } catch (Exception ignored) {}
    }

    public void findAndLoadModuleRegistry() {
        productionModuleIndex = new ArrayList<>();
        findModuleNames();
        loadModules();
    }

    public BasicModule getModuleByClassName(String name) {
        try {
            for (BasicModule module : loadedModules) {
                if (module.getClass().getName().equals(name)) return module;
            }
        } catch (Exception ignored) {
        }
        return null;
    }

    public BasicModule getModuleByName(String name) {
        try {
            for (BasicModule module : loadedModules) {
                if (module.getName().equals(name)) return module;
            }
        } catch (Exception ignored) {
        }
        return null;
    }


    public void loadModules() {
        productionModuleIndex = removeDuplicates(productionModuleIndex);
        try {
            productionModuleIndex = removeDuplicates(productionModuleIndex);
            for (String moduleclassname : productionModuleIndex) {
                try {
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

                } catch (Exception e) {
                    logger.warn("Failed to load debug module " + moduleclassname);
                    e.printStackTrace();
                }
            }
        } catch (Exception ignored) {
        }
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
     * IM NOT DOCUMENTIGN THIS SHIT HAHA LLL
     */
    private void findModuleNames() {
        ArrayList<String> clazzlist = new ArrayList<>();
        try {
            final ClassLoader loader = Thread.currentThread().getContextClassLoader();

            for (final ClassPath.ClassInfo info : ClassPath.from(loader).getTopLevelClasses()) {
                if (info.getName().startsWith("io.github.kingstefan26.stefans_util.module")) {
                    final Class<?> clazz = info.load();
                    if (BasicModule.class.isAssignableFrom(clazz)) {
                        if (!clazzlist.contains(clazz.getName())) {
                            logger.info("found module: " + clazz.getName());
                            clazzlist.add(clazz.getName());
                        }
                    }
                }
            }

        } catch (Exception ignored) {
        }

        clazzlist = removeDuplicates(clazzlist);
        productionModuleIndex.addAll(clazzlist);
    }

    public ArrayList<BasicModule> getModulesInCategory(ModuleManager.Category c) {
        ArrayList<BasicModule> a = new ArrayList<>();
        for (BasicModule m : loadedModules) {
            if (m.getCategory() == c) {
                a.add(m);
            }
        }
        return a;
    }
}