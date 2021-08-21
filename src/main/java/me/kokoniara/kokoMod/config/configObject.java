package me.kokoniara.kokoMod.config;

public class configObject {
    public String name;
    public boolean bolvalue;
    public double doublevalue;
    public double minvalue;
    public double maxvalue;
    public int keybindKeycode;
    public confgValueType type;


    public configObject(String name, confgValueType type, double doublevalue){
        this.name = name;
        this.doublevalue = doublevalue;
        this.type = type;
    }


    public configObject(String name,confgValueType type, boolean bolvalue){
        this.name = name;
        this.bolvalue = bolvalue;
        this.type = type;
    }

    public configObject(String name,confgValueType type, int keybindKeycode){
        this.name = name;
        this.keybindKeycode = keybindKeycode;
        this.type = type;
    }

}
