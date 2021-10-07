package io.github.kingstefan26.stefans_util.core.setting.newSetting.impl;

import io.github.kingstefan26.stefans_util.core.config.configObject;
import io.github.kingstefan26.stefans_util.core.module.Module;
import io.github.kingstefan26.stefans_util.core.setting.newSetting.general.AbstractSetting;
import io.github.kingstefan26.stefans_util.core.setting.newSetting.general.SettingsCore;

public class CheckSetting extends AbstractSetting {

    public CheckSetting(String name, Module parentModule, boolean deafultValue) {
        super(name, parentModule, SettingsCore.type.check);
        this.ConfigObject = new configObject(name, parentModule.getName(), deafultValue);
    }
    public CheckSetting(String name, Module parentModule, boolean deafultValue, String comment) {
        super(name, parentModule, SettingsCore.type.check);
        this.comment = comment;
        this.ConfigObject = new configObject(name, parentModule.getName(), deafultValue);
    }

    public boolean getValue(){
        return this.ConfigObject.getBooleanValue();
    }

    public void setValue(boolean value){
        this.ConfigObject.setBooleanValue(value);
    }
}
