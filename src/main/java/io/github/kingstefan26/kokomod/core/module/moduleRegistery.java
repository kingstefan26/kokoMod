package io.github.kingstefan26.kokomod.core.module;

import io.github.kingstefan26.kokomod.core.clickgui.ClickGui;
import io.github.kingstefan26.kokomod.core.module.blueprints.Module;
import io.github.kingstefan26.kokomod.core.module.blueprints.UtilModule;

import java.lang.reflect.Constructor;
import java.util.ArrayList;

public class moduleRegistery {
    public static moduleRegistery moduleRegistery_;
    public static moduleRegistery getModuleRegistery() {
        if(moduleRegistery_ == null) moduleRegistery_ = new moduleRegistery();
        return moduleRegistery_;
    }


    public ArrayList<Module> loadedModules;
    public ArrayList<UtilModule> loadedUtilModules;
    private moduleRegistery() {
        loadedModules = new ArrayList<>();
        moduleIndex.init();
        loadModules();
        loadUtilModules();
    }

    public void loadUtilModules() {
        for(String moduleclassname : moduleIndex.utilModuleIndex){
            try{
                Class<?> clazz = Class.forName(moduleclassname);
                Constructor<?> ctor = clazz.getConstructor();
                Object object = ctor.newInstance();
                loadedUtilModules.add((UtilModule) object);
            }catch(Exception e){
                e.printStackTrace();
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
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    public void unloadAllModules() {
        loadedModules.forEach(m ->{
            if(m.isToggled()) m.setToggled(false);
            m.close();
        });
        loadedModules.clear();
        ClickGui.getClickGui().clearComponents();
    }

    public ArrayList<Module> getModulesInCategory(Category c){
        ArrayList<Module> a = new ArrayList<>();
        for(Module m : loadedModules){
            if(m.getCategory() == c){
                a.add(m);
            }
        }
        return a;
    }
}
