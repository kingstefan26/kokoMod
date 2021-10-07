package io.github.kingstefan26.stefans_util.core.setting.newSetting.general;

import io.github.kingstefan26.stefans_util.core.module.Module;

import java.util.ArrayList;

public class SettingsCore {
    private static SettingsCore SettingsCoreInstance;
    public static SettingsCore getSettingsCore() {
        if (SettingsCoreInstance == null) SettingsCoreInstance = new SettingsCore();
        return SettingsCoreInstance;
    }

    public enum type{
        check, multiChoise, sliderNoDecimal, slider
    }

    private final ArrayList<AbstractSetting> settings = new ArrayList<>();

    public void addSetting(AbstractSetting a){
        settings.add(a);
    }
    public void removeSetting(AbstractSetting a){
        settings.remove(a);
    }

    public ArrayList<AbstractSetting> getSettingsByMod(Module mod) {
        ArrayList<AbstractSetting> a = new ArrayList<>();
        for(AbstractSetting s : settings){
            if(s.getParent() == mod){
                a.add(s);
            }
        }
        return a;
    }

    public ArrayList<AbstractSetting> getSettingsInType(SettingsCore.type type) {
        ArrayList<AbstractSetting> a = new ArrayList<>();
        for(AbstractSetting s : settings){
            if(s.getType() == type){
                a.add(s);
            }
        }
        return a;
    }

    public ArrayList<AbstractSetting> getAllSettings() {
        return this.settings;
    }

}
