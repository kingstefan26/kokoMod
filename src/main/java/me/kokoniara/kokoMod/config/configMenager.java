package me.kokoniara.kokoMod.config;

import com.typesafe.config.ConfigObject;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;


import java.util.ArrayList;

import static me.kokoniara.kokoMod.main.config;

public class configMenager {

    ArrayList<configObject> configObjects;
    ArrayList<Property> propertyObjects;

    public configMenager(){
        this.configObjects = new ArrayList<configObject>();
        this.propertyObjects = new ArrayList<Property>();
    }

    public void createConfigObject(String name, confgValueType type, boolean bool){
        configObject temp = new configObject(name, type, bool);
        this.configObjects.add(temp);
        this.sync();
    }
    public void createConfigObject(String name, confgValueType type, double val){
        configObject temp = new configObject(name, type, val);
        this.configObjects.add(temp);
        this.sync();
    }
    public void createConfigObject(String name, confgValueType type, int keycode){
        configObject temp = new configObject(name, type, keycode);
        this.configObjects.add(temp);
        this.sync();
    }

    public void replaceConfigObjectWithCopy(configObject o){
        for(configObject p : this.configObjects){
            if(p.name == o.name){
                this.configObjects.remove(p);
                this.configObjects.add(o);
                return;
            }
        }
    }

    public configObject getConfigByName(String name){
        for(configObject p : this.configObjects){
            if(p.name == name){
                return p;
            }
        }
        return null;
    }

    public void sync(){
        System.out.println("called sync config");
        try{
            // Load config
            config.load();

            Property dontCrash = config.get(Configuration.CATEGORY_GENERAL,
                    "dontCrash",
                    "dontCrash");

            //for every config object we create a temporary prop that has the config object value,
            //and we sync the internal memory objects to props aka save them
            for(configObject p : this.configObjects) {
                switch (p.type){
                    case KEYBIND:
                        Property temp = config.get(Configuration.CATEGORY_GENERAL,
                                p.name,
                                p.keybindKeycode);
                        this.propertyObjects.add(temp);
                        break;

                    case DOUBLE:
                        Property temp1 = config.get(Configuration.CATEGORY_GENERAL,
                                p.name,
                                p.doublevalue);
                        this.propertyObjects.add(temp1);
                        break;

                    case BOOLEAN:
                        Property temp2 = config.get(Configuration.CATEGORY_GENERAL,
                                p.name,
                                p.bolvalue);
                        this.propertyObjects.add(temp2);
                        break;
                }

            }

        } catch (Exception e){
            e.printStackTrace();
        } finally {
            for(configObject p : this.configObjects){
                for(Property b : this.propertyObjects){
                    if(p.name == b.getName()){
                        switch (p.type){
                            case KEYBIND:
                                p.keybindKeycode = b.getInt();
                                break;

                            case DOUBLE:
                                p.doublevalue = b.getDouble();
                                break;

                            case BOOLEAN:
                                p.bolvalue = b.getBoolean();
                                break;
                        }
                    }
                }
            }


            // Save props to config IF config changed
            if (config.hasChanged()) config.save();
        }

    }


}
