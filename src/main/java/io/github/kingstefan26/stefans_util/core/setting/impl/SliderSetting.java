package io.github.kingstefan26.stefans_util.core.setting.impl;

import io.github.kingstefan26.stefans_util.core.config.ConfigManager;
import io.github.kingstefan26.stefans_util.core.module.moduleframes.BasicModule;
import io.github.kingstefan26.stefans_util.core.setting.general.AbstractSetting;
import io.github.kingstefan26.stefans_util.core.setting.general.SettingsCore;

import java.util.function.Consumer;

public class SliderSetting extends AbstractSetting<Double> {
    double min;

    double max;

    public SliderSetting(String name, BasicModule parentModule, double deafultValue, double min, double max, Consumer<Double> callback) {
        super(name, parentModule, SettingsCore.type.slider, callback);
        this.min = min;
        this.max = max;
//        this.ConfigObject = new configObject(name, parentModule.getName(), deafultValue);

        this.prop = ConfigManager.getInstance().getConfigObject(parentModule.getName() + "." + name, deafultValue);

        setValue(getValue());
    }

    public SliderSetting(String name, BasicModule parentModule, double deafultValue, double min, double max, Consumer<Double> callback, String comment) {
        super(name, parentModule, SettingsCore.type.slider, callback);
        this.min = min;
        this.max = max;
        this.comment = comment;
//        this.ConfigObject = new configObject(name, parentModule.getName(), deafultValue);
        this.prop = ConfigManager.getInstance().getConfigObject(parentModule.getName() + "." + name, deafultValue);
    }

//    public double getValue() {
//        return this.ConfigObject.getDoubleValue();
//    }


    public void setValue(double value) {
        if (!(value >= max)) {
//            this.ConfigObject.setDoubleValue(value);
            this.prop.setProperty(value);
            this.callback.accept(value);
        }
    }

    public double getMin() {
        return min;
    }

    public double getMax() {
        return max;
    }
}
