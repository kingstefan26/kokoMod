package io.github.kingstefan26.stefans_util.core.setting.impl;

import io.github.kingstefan26.stefans_util.core.module.moduleFrames.basicModule;
import io.github.kingstefan26.stefans_util.core.newconfig.ConfigManagerz;
import io.github.kingstefan26.stefans_util.core.setting.general.AbstractSetting;
import io.github.kingstefan26.stefans_util.core.setting.general.SettingsCore;

import java.util.function.Consumer;

public class SliderNoDecimalSetting extends AbstractSetting<Double> {
    double min;

    double max;

    public SliderNoDecimalSetting(String name, basicModule parentModule, double deafultValue, double min, double max, Consumer<Double> callback) {
        super(name, parentModule, SettingsCore.type.sliderNoDecimal, callback);
        this.min = min;
        this.max = max;

        this.prop = ConfigManagerz.getInstance().getConfigObjectSpetial(parentModule.getName() + "." + name, deafultValue);

        this.callback.accept(getValue());
    }

    public SliderNoDecimalSetting(String name, basicModule parentModule, double deafultValue, double min, double max, Consumer<Double> callback, String comment) {
        super(name, parentModule, SettingsCore.type.sliderNoDecimal, callback);
        this.min = min;
        this.max = max;
        this.comment = comment;


        this.prop = ConfigManagerz.getInstance().getConfigObjectSpetial(parentModule.getName() + "." + name, deafultValue);
    }

    @Override
    public Double getValue() {
        Object a = this.prop.getProperty();


        if (a instanceof Double) {
            int t = Math.toIntExact(Math.round((Double) a));

            return Double.valueOf(t);

        } else if (a instanceof Integer) {
            int t = Math.toIntExact(Math.round((Integer) a));

            return Double.valueOf(t);
        }

        throw new IllegalArgumentException("FUCK THIS SHIT IM OUT");
    }

    @Override
    public void setValue(Double value) {
        if (value >= max) {
            return;
        }
//        this.ConfigObject.setIntValue(value);

        this.prop.setProperty(value);
        this.callback.accept(value);
    }

    public double getMin() {
        return min;
    }

    public double getMax() {
        return max;
    }
}
