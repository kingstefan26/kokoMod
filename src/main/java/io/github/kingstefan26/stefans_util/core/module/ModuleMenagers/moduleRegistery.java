package io.github.kingstefan26.stefans_util.core.module.ModuleMenagers;

import com.google.common.reflect.ClassPath;
import io.github.kingstefan26.stefans_util.core.module.moduleFrames.basicModule;
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
    public ArrayList<basicModule> loadedModules = new ArrayList<>();

    {
        logger = LogManager.getLogger("kokomod-moduleRegistry");
    }

    public void unloadAllModules() {
        try {
            Iterator<basicModule> iter = loadedModules.iterator();

            while (iter.hasNext()) {
                basicModule m = iter.next();
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

    public basicModule getModuleByClassName(String name) {
        try {
            for (basicModule module : loadedModules) {
                if (module.getClass().getName().equals(name)) return module;
            }
        } catch (Exception ignored) {
        }
        return null;
    }

    public basicModule getModuleByName(String name) {
        try {
            for (basicModule module : loadedModules) {
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

                    for (basicModule bm : loadedModules) {
                        if (bm.getClass().getName().equals(clazz.getName())) {
                            exists = true;
                            logger.info("module " + bm.getName() + " was already loaded (probably web module)");
                            break;
                        }
                    }

                    if (!exists) {
                        Constructor<?> ctor = clazz.getConstructor();
                        Object object = ctor.newInstance();
                        ((basicModule) object).onLoad();
                        loadedModules.add((basicModule) object);
                        logger.info("loaded module: " + ((basicModule) object).getName());
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
                    if (basicModule.class.isAssignableFrom(clazz)) {
                        if (!clazzlist.contains(clazz.getName())) {
                            logger.info("found module: " + clazz.getName());
                            clazzlist.add(clazz.getName());
                        }
                    }
                }
            }

        } catch (Exception ignored) {}

        clazzlist = removeDuplicates(clazzlist);
        productionModuleIndex.addAll(clazzlist);
    }

    public ArrayList<basicModule> getModulesInCategory(moduleManager.Category c) {
        ArrayList<basicModule> a = new ArrayList<>();
        for (basicModule m : loadedModules) {
            if (m.getCategory() == c) {
                a.add(m);
            }
        }
        return a;
    }
}