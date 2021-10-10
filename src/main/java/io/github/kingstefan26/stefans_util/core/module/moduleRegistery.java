package io.github.kingstefan26.stefans_util.core.module;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Constructor;
import java.util.ArrayList;

public class moduleRegistery {
    public static moduleRegistery moduleRegistery_;
    public static moduleRegistery getModuleRegistery() {
        if(moduleRegistery_ == null) moduleRegistery_ = new moduleRegistery();
        return moduleRegistery_;
    }
    private static final Logger logger = LogManager.getLogger("kokomod-moduleRegistry");

    public ArrayList<Module> loadedModules;
    public ArrayList<UtilModule> loadedUtilModules;
    private moduleRegistery() {
        loadedModules = new ArrayList<>();
        loadedUtilModules = new ArrayList<>();
        moduleIndex.init();
        loadModules();
        loadDebugModules();
        loadUtilModules();
    }

    public void loadUtilModules() {
        for(String moduleclassname : moduleIndex.utilModuleIndex){
            Object object;
            try{
                Class<?> clazz = Class.forName(moduleclassname);
                Constructor<?> ctor = clazz.getConstructor();
                object = ctor.newInstance();
                loadedUtilModules.add((UtilModule) object);
                if(((UtilModule)object).getName() == null) {
                    logger.warn("failed to load " + moduleclassname);
                    continue;
                }
                //logger.info("loaded module: "+ ((UtilModule) object).getName());
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    public void loadDebugModules() {
        for(String moduleclassname : moduleIndex.debugModuleIndex){
            try{
                Class<?> clazz = Class.forName(moduleclassname);
                Constructor<?> ctor = clazz.getConstructor();
                Object object = ctor.newInstance();
                loadedModules.add((Module) object);
                ((Module) object).onLoad();
                //logger.info("loaded module: "+ ((Module) object).getName());
            }catch(Exception e){
                logger.warn("Failed to load debug module " + moduleclassname);
            }
        }
    }

    public void loadModules() {
        for(String moduleclassname : moduleIndex.productionModuleIndex){
            try{
                Class<?> clazz = Class.forName(moduleclassname);
                Constructor<?> ctor = clazz.getConstructor();
                Object object = ctor.newInstance();
                loadedModules.add((Module) object);
                ((Module) object).onLoad();
                //logger.info("loaded module: "+ ((Module) object).getName());
            }catch(Exception e){
                logger.warn("Failed to load debug module " + moduleclassname);
            }
        }
    }

//    public void unloadAllModules() {
//        loadedModules.forEach(m ->{
//            if(m.isToggled()) m.setToggled(false);
//            m.close();
//        });
//        loadedModules.clear();
//        ClickGui.getClickGui().clearComponents();
//    }

    public ArrayList<Module> getModulesInCategory(ModuleManager.Category c){
        ArrayList<Module> a = new ArrayList<>();
        for(Module m : loadedModules){
            if(m.getCategory() == c){
                a.add(m);
            }
        }
        return a;
    }
}
