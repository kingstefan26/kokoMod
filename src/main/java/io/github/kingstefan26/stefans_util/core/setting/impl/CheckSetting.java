package io.github.kingstefan26.stefans_util.core.setting.impl;

import io.github.kingstefan26.stefans_util.core.config.configObject;
import io.github.kingstefan26.stefans_util.core.module.moduleFrames.basicModule;
import io.github.kingstefan26.stefans_util.core.setting.general.AbstractSetting;
import io.github.kingstefan26.stefans_util.core.setting.general.SettingsCore;

import java.util.function.Consumer;

public class CheckSetting extends AbstractSetting {


    public CheckSetting(String name, basicModule parentModule, boolean deafultValue, Consumer<Object> callback) {
        super(name, parentModule, SettingsCore.type.check, callback);
        this.ConfigObject = new configObject(name, parentModule.getName(), deafultValue);
        setValue(getValue());
    }
    public CheckSetting(String name, basicModule parentModule, boolean deafultValue,Consumer<Object> callback, String comment) {
        super(name, parentModule, SettingsCore.type.check, callback);
        this.comment = comment;
        this.ConfigObject = new configObject(name, parentModule.getName(), deafultValue);
    }

    public boolean getValue(){
        return this.ConfigObject.getBooleanValue();
    }

    public void setValue(boolean value){
        this.ConfigObject.setBooleanValue(value);
        this.callback.accept(value);
    }
}
