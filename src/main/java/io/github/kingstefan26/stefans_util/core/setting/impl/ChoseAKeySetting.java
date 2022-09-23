/*
 * Copyright (c) 2021. All copyright reserved
 */

package io.github.kingstefan26.stefans_util.core.setting.impl;

import io.github.kingstefan26.stefans_util.core.config.ConfigManager;
import io.github.kingstefan26.stefans_util.core.module.moduleframes.BasicModule;
import io.github.kingstefan26.stefans_util.core.setting.AbstractSetting;
import io.github.kingstefan26.stefans_util.core.setting.SettingsCore;

import java.util.function.Consumer;

public class ChoseAKeySetting extends AbstractSetting<Integer> {

    public ChoseAKeySetting(String name, BasicModule parentModule, int deafultValue, Consumer<Integer> callback) {
        super(name, parentModule, SettingsCore.type.KEY, callback);
        this.prop = ConfigManager.getInstance().getConfigObject(parentModule.getName() + "." + name, deafultValue);
        setValue(getValue());
    }

    public ChoseAKeySetting(String name, BasicModule parentModule, int deafultValue, Consumer<Integer> callback, String comment) {
        super(name, parentModule, SettingsCore.type.KEY, callback);
        this.comment = comment;
        this.prop = ConfigManager.getInstance().getConfigObject(parentModule.getName() + "." + name, deafultValue);
    }

    public Integer getValue() {
        return this.prop.getProperty();
    }

    public void setValue(int value) {
        this.prop.setProperty(value);
        this.callback.accept(value);
    }
}
