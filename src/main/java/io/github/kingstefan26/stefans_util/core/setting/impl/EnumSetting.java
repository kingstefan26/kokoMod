/*
 * Copyright (c) 2022. All copyright reserved
 */

package io.github.kingstefan26.stefans_util.core.setting.impl;

import io.github.kingstefan26.stefans_util.core.config.ConfigManager;
import io.github.kingstefan26.stefans_util.core.module.moduleframes.BasicModule;
import io.github.kingstefan26.stefans_util.core.setting.AbstractSetting;
import io.github.kingstefan26.stefans_util.core.setting.SettingsCore;

import java.util.ArrayList;
import java.util.function.Consumer;

public class EnumSetting<C extends Enum> extends AbstractSetting<String> {

    private final ArrayList<String> possibleValues;

    public EnumSetting(String name, BasicModule parentModule, C defaultVal, C[] values, Consumer<String> callback) {
        super(name, parentModule, SettingsCore.type.ENUM, callback);
        this.possibleValues = new ArrayList<>();
        for (C value : values) {
            possibleValues.add(value.toString());
        }
        this.prop = ConfigManager.getInstance().getConfigObject(parentModule.getName() + "." + name, defaultVal.toString());
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
