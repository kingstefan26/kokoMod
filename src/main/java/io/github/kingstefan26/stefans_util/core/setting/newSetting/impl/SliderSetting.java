package io.github.kingstefan26.stefans_util.core.setting.newSetting.impl;

import io.github.kingstefan26.stefans_util.core.config.configObject;
import io.github.kingstefan26.stefans_util.core.module.Module;
import io.github.kingstefan26.stefans_util.core.setting.newSetting.general.AbstractSetting;
import io.github.kingstefan26.stefans_util.core.setting.newSetting.general.SettingsCore;

public class SliderSetting extends AbstractSetting {
    int min;

    int max;

    public SliderSetting(String name, Module parentModule, Double deafultValue, int min, int max) {
        super(name, parentModule, SettingsCore.type.slider);
        this.min = min;
        this.max = max;
        this.ConfigObject = new configObject(name, parentModule.getName(), deafultValue);
    }
    public SliderSetting(String name, Module parentModule, Double deafultValue, int min, int max, String comment) {
        super(name, parentModule, SettingsCore.type.slider);
        this.min = min;
        this.max = max;
        this.comment = comment;
        this.ConfigObject = new configObject(name, parentModule.getName(), deafultValue);
    }
    public Double getValue(){
        return this.ConfigObject.getDoubleValue();
    }

    public void setValue(Double value){
        if(!(value >= max)){
            this.ConfigObject.setDoubleValue(value);
        }
    }

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }
}
