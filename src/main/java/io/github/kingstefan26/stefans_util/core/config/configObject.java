package io.github.kingstefan26.stefans_util.core.config;

import net.minecraftforge.common.config.Property;

public class configObject {
    private String name;
    private String parentModuleName;
    private boolean isIndependent = false;
    private confgValueType type;

    private Property configPropertyObject;



    public configObject(String name,String parentModuleName, int dval){
        if(configMenager.getConfigManager() == null) return;
        this.name = name;
        this.parentModuleName = parentModuleName;
        this.type = confgValueType.INT;
        configPropertyObject = configMenager.getConfigManager().config.get(parentModuleName, name, dval);
    }

    public configObject(String name,String parentModuleName, double dval){
        if(configMenager.getConfigManager() == null) return;
        this.name = name;
        this.parentModuleName = parentModuleName;
        this.type = confgValueType.DOUBLE;
        configPropertyObject = configMenager.getConfigManager().config.get(parentModuleName, name, dval);
    }
    public configObject(String name,String parentModuleName, String dval){
        if(configMenager.getConfigManager() == null) return;
        this.name = name;
        this.parentModuleName = parentModuleName;
        this.type = confgValueType.STRING;
        configPropertyObject = configMenager.getConfigManager().config.get(parentModuleName, name, dval);
    }

    public configObject(String name,String parentModuleName, boolean dval){
        if(configMenager.getConfigManager() == null) return;
        this.name = name;
        this.parentModuleName = parentModuleName;
        this.type = confgValueType.BOOLEAN;
        configPropertyObject = configMenager.getConfigManager().config.get(parentModuleName, name, dval);
    }

    public configObject(String name,String parentModuleName, confgValueType type){
        if(configMenager.getConfigManager() == null) return;
        this.name = name;
        this.parentModuleName = parentModuleName;
        this.type = type;
        configPropertyObject = configMenager.getConfigManager().config.get(parentModuleName, "presistanceToggle", false);
    }


//    public configObject(String name,String parentModuleName, confgValueType type){
//        if(kokoMod.instance._configMenager == null) return;
//        this.name = name;
//        this.parentModuleName = parentModuleName;
//        this.type = type;
//        this.isIndependent = false;
//        switch(type){
//            case INT:
//                configPropertyObject = configMenager.getConfigManager().config.get(parentModuleName, name, 0);
//                break;
//            case DOUBLE:
//                configPropertyObject = configMenager.getConfigManager().config.get(parentModuleName, name, 0.0);
//                break;
//            case STRING:
//                configPropertyObject = configMenager.getConfigManager().config.get(parentModuleName, name, "default");
//                break;
//            case BOOLEAN:
//                configPropertyObject = configMenager.getConfigManager().config.get(parentModuleName, name, false);
//                break;
//            case PERSISTENT:
//                configPropertyObject = configMenager.getConfigManager().config.get(parentModuleName, "presistanceToggle", false);
//                break;
//        }
//    }

    public configObject(String name, confgValueType type){
        if(configMenager.getConfigManager() == null) return;
        this.name = name;
        this.type = type;
        this.isIndependent = true;
        switch(type){
            case INT:
                configPropertyObject = configMenager.getConfigManager().config.get("independent int value", name, 0);
                break;
            case DOUBLE:
                configPropertyObject = configMenager.getConfigManager().config.get("independent Double value", name, 0.0);
                break;
            case STRING:
                configPropertyObject = configMenager.getConfigManager().config.get("independent String value", name, "default");
                break;
            case BOOLEAN:
                configPropertyObject = configMenager.getConfigManager().config.get("independent Bool value", name, false);
                break;
        }
    }

    public String getName() {return this.name;}
    public confgValueType getType() {return this.type;}
    public boolean isIndependent() {return isIndependent;}
    public String getParentModuleName() {return this.parentModuleName;}


    public int getIntValue(){
        return configPropertyObject.getInt();
    }

    public void setIntValue(int value){
        configPropertyObject.setValue(value);
        if(configMenager.getConfigManager().config.hasChanged()) configMenager.getConfigManager().config.save();
    }

    public double getDoubleValue(){
        return configPropertyObject.getDouble();
    }
    public void setDoubleValue(double value){
        configPropertyObject.setValue(value);
        if(configMenager.getConfigManager().config.hasChanged()) configMenager.getConfigManager().config.save();
    }

    public String getStringValue(){
        return configPropertyObject.getString();
    }
    public void setStringValue(String value){
        configPropertyObject.setValue(value);
        if(configMenager.getConfigManager().config.hasChanged()) configMenager.getConfigManager().config.save();
    }

    public boolean getBooleanValue(){
        return configPropertyObject.getBoolean();
    }
    public void setBooleanValue(boolean value){
        configPropertyObject.setValue(value);
        if(configMenager.getConfigManager().config.hasChanged()) configMenager.getConfigManager().config.save();
    }



}
