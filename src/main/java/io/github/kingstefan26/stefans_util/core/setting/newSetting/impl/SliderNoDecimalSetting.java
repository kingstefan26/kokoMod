package io.github.kingstefan26.stefans_util.core.setting.newSetting.impl;

import io.github.kingstefan26.stefans_util.core.config.configObject;
import io.github.kingstefan26.stefans_util.core.module.Module;
import io.github.kingstefan26.stefans_util.core.setting.newSetting.general.AbstractSetting;
import io.github.kingstefan26.stefans_util.core.setting.newSetting.general.SettingsCore;

public class SliderNoDecimalSetting extends AbstractSetting {
    int min;

    int max;

    public SliderNoDecimalSetting(String name, Module parentModule, int deafultValue, int min, int max) {
        super(name, parentModule, SettingsCore.type.sliderNoDecimal);
        this.min = min;
        this.max = max;
        this.ConfigObject = new configObject(name, parentModule.getName(), deafultValue);
    }
    public SliderNoDecimalSetting(String name, Module parentModule, int deafultValue, int min, int max, String comment) {
        super(name, parentModule, SettingsCore.type.sliderNoDecimal);
        this.min = min;
        this.max = max;
        this.comment = comment;
        this.ConfigObject = new configObject(name, parentModule.getName(), deafultValue);
    }
    public int getValue(){
        return this.ConfigObject.getIntValue();
    }

    public void setValue(int value){
        if (!(value >= max)) {
            this.ConfigObject.setIntValue(value);
        }
    }

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }
}
