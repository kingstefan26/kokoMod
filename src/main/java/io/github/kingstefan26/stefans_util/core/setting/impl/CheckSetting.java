package io.github.kingstefan26.stefans_util.core.setting.impl;

import io.github.kingstefan26.stefans_util.core.config.ConfigManager;
import io.github.kingstefan26.stefans_util.core.module.moduleframes.BasicModule;
import io.github.kingstefan26.stefans_util.core.setting.AbstractSetting;
import io.github.kingstefan26.stefans_util.core.setting.SettingsCore;

import java.util.function.Consumer;

public class CheckSetting extends AbstractSetting<Boolean> {


    public CheckSetting(String name, BasicModule parentModule, boolean deafultValue, Consumer<Boolean> callback) {
        super(name, parentModule, SettingsCore.type.CHECK, callback);

        this.prop = ConfigManager.getInstance().getConfigObject(parentModule.getName() + "." + name, deafultValue);
        setValue(getValue());
    }

    public CheckSetting(String name, BasicModule parentModule, boolean deafultValue, Consumer<Boolean> callback, String comment) {
        super(name, parentModule, SettingsCore.type.CHECK, callback);
        this.comment = comment;
        this.prop = ConfigManager.getInstance().getConfigObject(parentModule.getName() + "." + name, deafultValue);
    }

    public Boolean getValue() {
        return this.prop.getProperty();
    }

    public void setValue(boolean value) {
        this.prop.setProperty(value);
        if (this.callback != null) this.callback.accept(value);
    }
}
