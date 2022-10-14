/*
 * Copyright (c) 2022. All copyright reserved
 */

package io.github.kingstefan26.stefans_util.core.setting;

import io.github.kingstefan26.stefans_util.core.module.moduleframes.BasicModule;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SettingsCore {
    private static SettingsCore settingsCoreInstance;
    private final List<AbstractSetting<?>> settings = new ArrayList<>();

    public static SettingsCore getSettingsCore() {
        if (settingsCoreInstance == null) settingsCoreInstance = new SettingsCore();
        return settingsCoreInstance;
    }

    public void addSetting(AbstractSetting<?> a) {
        settings.add(a);
    }

    public void removeSetting(AbstractSetting<?> a) {
        settings.remove(a);
    }

    public List<AbstractSetting<?>> getSettingsByMod(BasicModule mod) {
        ArrayList<AbstractSetting<?>> a = new ArrayList<>();
        for (AbstractSetting<?> s : settings) {
            if (s.getParent() == mod) {
                a.add(s);
            }
        }
        return a;
    }

    public boolean doesSettingExist(String name) {
        for (AbstractSetting<?> s : settings) {
            if (Objects.equals(s.name, name)) {
                return true;
            }
        }
        return false;
    }

    public List<AbstractSetting<?>> getSettingsInType(SettingsCore.type type) {
        ArrayList<AbstractSetting<?>> a = new ArrayList<>();
        for (AbstractSetting<?> s : settings) {
            if (s.getType() == type) {
                a.add(s);
            }
        }
        return a;
    }

    public List<AbstractSetting<?>> getAllSettings() {
        return this.settings;
    }

    public enum type {
        CHECK, MULTI_CHOICE, SLIDER_NO_DECIMAL, SLIDER, ENUM, KEY
    }

}
