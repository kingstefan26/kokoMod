/*
 * Copyright (c) 2021. All copyright reserved
 */

package io.github.kingstefan26.stefans_util.core.setting.impl;

import io.github.kingstefan26.stefans_util.core.config.configObject;
import io.github.kingstefan26.stefans_util.core.module.moduleFrames.basicModule;
import io.github.kingstefan26.stefans_util.core.setting.general.AbstractSetting;
import io.github.kingstefan26.stefans_util.core.setting.general.SettingsCore;

import java.util.function.Consumer;

public class ChoseAKeySetting extends AbstractSetting {

    public ChoseAKeySetting(String name, basicModule parentModule, int deafultValue, Consumer<Object> callback) {
        super(name, parentModule, SettingsCore.type.key, callback);
        this.ConfigObject = new configObject(name, parentModule.getName(), deafultValue);
        setValue(getValue());
    }
    public ChoseAKeySetting(String name, basicModule parentModule, int deafultValue,Consumer<Object> callback, String comment) {
        super(name, parentModule, SettingsCore.type.key, callback);
        this.comment = comment;
        this.ConfigObject = new configObject(name, parentModule.getName(), deafultValue);
    }

    public int getValue(){
        return this.ConfigObject.getIntValue();
    }

    public void setValue(int value){
        this.ConfigObject.setIntValue(value);
        this.callback.accept(value);
    }
}
