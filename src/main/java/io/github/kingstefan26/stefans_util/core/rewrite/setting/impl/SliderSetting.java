package io.github.kingstefan26.stefans_util.core.rewrite.setting.impl;

import io.github.kingstefan26.stefans_util.core.config.configObject;
import io.github.kingstefan26.stefans_util.core.rewrite.module.moduleFrames.basicModule;
import io.github.kingstefan26.stefans_util.core.rewrite.setting.general.AbstractSetting;
import io.github.kingstefan26.stefans_util.core.rewrite.setting.general.SettingsCore;

import java.util.function.Consumer;

public class SliderSetting extends AbstractSetting {
    int min;

    int max;

    public SliderSetting(String name, basicModule parentModule, Double deafultValue, int min, int max, Consumer<Object> callback) {
        super(name, parentModule, SettingsCore.type.slider, callback);
        this.min = min;
        this.max = max;
        this.ConfigObject = new configObject(name, parentModule.getName(), deafultValue);
    }
    public SliderSetting(String name, basicModule parentModule, Double deafultValue, int min, int max, Consumer<Object> callback, String comment) {
        super(name, parentModule, SettingsCore.type.slider, callback);
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
            this.callback.accept(value);
        }
    }

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }
}
