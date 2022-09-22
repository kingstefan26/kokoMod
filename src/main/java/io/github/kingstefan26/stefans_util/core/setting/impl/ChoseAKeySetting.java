/*
 * Copyright (c) 2021. All copyright reserved
 */

package io.github.kingstefan26.stefans_util.core.setting.impl;

import io.github.kingstefan26.stefans_util.core.module.moduleframes.BasicModule;
import io.github.kingstefan26.stefans_util.core.newconfig.ConfigManagerz;
import io.github.kingstefan26.stefans_util.core.setting.general.AbstractSetting;
import io.github.kingstefan26.stefans_util.core.setting.general.SettingsCore;

import java.util.function.Consumer;

public class ChoseAKeySetting extends AbstractSetting<Integer> {

    public ChoseAKeySetting(String name, BasicModule parentModule, int deafultValue, Consumer<Integer> callback) {
        super(name, parentModule, SettingsCore.type.key, callback);
//        this.ConfigObject = new configObject(name, parentModule.getName(), deafultValue);
        this.prop = ConfigManagerz.getInstance().getConfigObject(parentModule.getName() + "." + name, deafultValue);
        setValue(getValue());
    }

    public ChoseAKeySetting(String name, BasicModule parentModule, int deafultValue, Consumer<Integer> callback, String comment) {
        super(name, parentModule, SettingsCore.type.key, callback);
        this.comment = comment;
//        this.ConfigObject = new configObject(name, parentModule.getName(), deafultValue);
        this.prop = ConfigManagerz.getInstance().getConfigObject(parentModule.getName() + "." + name, deafultValue);
    }

//    public int getValue(){
//        return this.ConfigObject.getIntValue();
//    }
//
//    public void setValue(int value){
//        this.ConfigObject.setIntValue(value);
//        this.callback.accept(value);
//    }
}
