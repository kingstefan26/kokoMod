package io.github.kingstefan26.stefans_util.core.rewrite.setting.impl;

import io.github.kingstefan26.stefans_util.core.config.configObject;
import io.github.kingstefan26.stefans_util.core.rewrite.module.moduleFrames.basicModule;
import io.github.kingstefan26.stefans_util.core.rewrite.setting.general.AbstractSetting;
import io.github.kingstefan26.stefans_util.core.rewrite.setting.general.SettingsCore;

public class SliderNoDecimalSetting extends AbstractSetting {
    int min;

    int max;

    public SliderNoDecimalSetting(String name, basicModule parentModule, int deafultValue, int min, int max) {
        super(name, parentModule, SettingsCore.type.sliderNoDecimal);
        this.min = min;
        this.max = max;
        this.ConfigObject = new configObject(name, parentModule.getName(), deafultValue);
    }
    public SliderNoDecimalSetting(String name, basicModule parentModule, int deafultValue, int min, int max, String comment) {
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