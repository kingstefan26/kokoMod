package io.github.kingstefan26.stefans_util.core.config;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Loader;

import java.io.File;


public class configMenager {

    private static configMenager configMenager;

    public static configMenager getConfigManager() {
        if(configMenager == null) configMenager = new configMenager();
        return configMenager;
    }

    public Configuration config;

    public configMenager(){
        File configFile = new File(Loader.instance().getConfigDir(), "kokoMod.cfg");
        config = new Configuration(configFile);
        config.load();

    }



}
