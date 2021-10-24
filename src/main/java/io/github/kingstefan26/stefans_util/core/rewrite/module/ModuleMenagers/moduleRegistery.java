package io.github.kingstefan26.stefans_util.core.rewrite.module.ModuleMenagers;

import com.google.common.reflect.ClassPath;
import io.github.kingstefan26.stefans_util.core.rewrite.module.moduleFrames.basicModule;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Constructor;
import java.util.ArrayList;

public class moduleRegistery {
    public static moduleRegistery moduleRegistery_;
    public static moduleRegistery getModuleRegistery() {
        if(moduleRegistery_ == null) moduleRegistery_ = new moduleRegistery();
        return moduleRegistery_;
    }

    private final Logger logger;
    public ArrayList<String> productionModuleIndex;
    public ArrayList<basicModule> loadedModules;

    {
        logger = LogManager.getLogger("kokomod-moduleRegistry");
    }

    public void initRegistry(){
        productionModuleIndex = new ArrayList<>();
        loadedModules = new ArrayList<>();
        loadModuleNames();
        loadModules();
    }


    public void loadModules() {
        productionModuleIndex = removeDuplicates(productionModuleIndex);
        try{
            productionModuleIndex = removeDuplicates(productionModuleIndex);
            for(String moduleclassname : productionModuleIndex){
                try{
                    Class<?> clazz = Class.forName(moduleclassname);
                    Constructor<?> ctor = clazz.getConstructor();
                    Object object = ctor.newInstance();
                    loadedModules.add((basicModule) object);
                    ((basicModule) object).onLoad();
                    logger.info("loaded module: "+ ((basicModule) object).getName());
                }catch(Exception e){
                    logger.warn("Failed to load debug module " + moduleclassname);
                    e.printStackTrace();
                }
            }
        }catch (Exception ignored){}
    }

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
     * IM NOT DOCUMENTIGN THIS SHIT HAHA LLL
     */
    private void loadModuleNames(){
        ArrayList<String> clazzlist = new ArrayList<>();
        try{
            final ClassLoader loader = Thread.currentThread().getContextClassLoader();

            for (final ClassPath.ClassInfo info : ClassPath.from(loader).getTopLevelClasses()) {
                if (info.getName().startsWith("io.github.kingstefan26.stefans_util.module")) {
                    final Class<?> clazz = info.load();
                    if(basicModule.class.isAssignableFrom(clazz)) {
                        if(!clazzlist.contains(clazz.getName())){
                            logger.info("found module: "+ clazz.getName());
                            clazzlist.add(clazz.getName());
                        }
                    }
                }
            }

        }catch(Exception ignored){}

        clazzlist = removeDuplicates(clazzlist);
        productionModuleIndex.addAll(clazzlist);
    }

    public ArrayList<basicModule> getModulesInCategory(moduleManager.Category c){
        ArrayList<basicModule> a = new ArrayList<>();
        for(basicModule m : loadedModules){
            if(m.getCategory() == c){
                a.add(m);
            }
        }
        return a;
    }
}