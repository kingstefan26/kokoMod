package io.github.kingstefan26.stefans_util.core.setting.impl;

import io.github.kingstefan26.stefans_util.core.config.ConfigManager;
import io.github.kingstefan26.stefans_util.core.module.moduleframes.BasicModule;
import io.github.kingstefan26.stefans_util.core.setting.AbstractSetting;
import io.github.kingstefan26.stefans_util.core.setting.SettingsCore;

import java.util.function.Consumer;

public class SliderNoDecimalSetting extends AbstractSetting<Double> {
    double min;
    double max;

    /**
     * @param name         Name of setting
     * @param parentModule name of parrent module
     * @param deafultValue default value of said setting
     * @param min          minimum value of setting
     * @param max          max value of setting
     * @param callback     this is callback with Dobule whenever a user changes it
     */
    public SliderNoDecimalSetting(String name, BasicModule parentModule, double deafultValue, double min, double max, Consumer<Double> callback, String... comment) {
        super(name, parentModule, SettingsCore.type.SLIDER_NO_DECIMAL, callback);
        this.min = min;
        this.max = max;

        if (comment != null && comment.length > 0) {
            this.comment = comment[0];
        }

        this.prop = ConfigManager.getInstance().getConfigObjectSpetial(parentModule.getName() + "." + name, deafultValue);

        this.callback.accept(getValue());
    }


    @Override
    public Double getValue() {
        Object a = this.prop.getProperty();


        if (a instanceof Double) {
            int t = Math.toIntExact(Math.round((Double) a));

            return (double) t;

        } else if (a instanceof Integer) {
            int t = Math.toIntExact(Math.round((Integer) a));

            return (double) t;
        }

        throw new IllegalArgumentException("FUCK THIS SHIT IM OUT");
    }

    @Override
    public void setValue(Double value) {
        if (value >= max) {
            return;
        }

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
