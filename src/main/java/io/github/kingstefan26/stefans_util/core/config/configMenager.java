package io.github.kingstefan26.stefans_util.core.config;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Loader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class configMenager {

    public static configMenager configMenager;

    public static configMenager getConfigManager() {
        if(configMenager == null) configMenager = new configMenager();
        return configMenager;
    }

    List<configObject> configObjects;

    public Configuration config;

    public configMenager(){
        File configFile = new File(Loader.instance().getConfigDir(), "kokoMod.cfg");
        config = new Configuration(configFile);
        config.load();

        this.configObjects = new ArrayList<configObject>();
    }

    public void createConfigObject(configObject in){
        configObjects.add(in);
    }


    public configObject getConfigObjectByNameAndType(String name, confgValueType type){
        for(configObject i: configObjects){
            if(i.getName() == name && i.getType() == type){
                int index  = configObjects.indexOf(i);
                return configObjects.get(index);
            }
        }
        System.out.println("failed to find config object :(");
        return null;
    }

    public configObject getConfigObjectByNameAndTypeAndParent(String name,String parentModuleName ,confgValueType type){
        for(configObject i: configObjects){
            if(i.getName() == name && i.getType() == type && i.getParentModuleName() == parentModuleName){
                int index  = configObjects.indexOf(i);
                return configObjects.get(index);
            }
        }
        System.out.println("failed to find config object :(");
        return null;
    }



}
