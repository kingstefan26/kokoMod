package io.github.kingstefan26.stefans_util.core.setting.impl;

import io.github.kingstefan26.stefans_util.core.config.ConfigManager;
import io.github.kingstefan26.stefans_util.core.module.moduleframes.BasicModule;
import io.github.kingstefan26.stefans_util.core.setting.AbstractSetting;
import io.github.kingstefan26.stefans_util.core.setting.SettingsCore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Consumer;

public class MultichoiseSetting extends AbstractSetting<String> {
    private final ArrayList<String> possibleValues;

    public MultichoiseSetting(String name, BasicModule parentModule, String deafultValue, String[] possibleValues, Consumer<String> callback) {
        super(name, parentModule, SettingsCore.type.MULTI_CHOICE, callback);
        this.possibleValues = new ArrayList<>(Arrays.asList(possibleValues));
        this.comment = comment;

        this.prop = ConfigManager.getInstance().getConfigObject(parentModule.getName() + "." + name, deafultValue);

        setValue(getValue());
    }


    public MultichoiseSetting(String name, BasicModule parentModule, String deafultValue, String[] possibleValues, Consumer<String> callback, String comment) {
        super(name, parentModule, SettingsCore.type.MULTI_CHOICE, callback);
        this.possibleValues = new ArrayList<>(Arrays.asList(possibleValues));
        this.comment = comment;
        this.prop = ConfigManager.getInstance().getConfigObject(parentModule.getName() + "." + name, deafultValue);

        setValue(getValue());

    }

    @Override
    public String getValue() {
        return (String) this.prop.getProperty();
    }

    @Override
    public void setValue(String value) {
        if (!possibleValues.contains(value)) {
            throw new IllegalArgumentException("This value is not possible with current set!");
        } else {
            this.callback.accept(value);
            this.prop.setProperty(value);
        }
    }

    public ArrayList<String> getAllPossibleValues() {
        return possibleValues;
    }
}
