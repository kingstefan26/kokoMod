package io.github.kingstefan26.stefans_util.core.setting.newSetting.impl;

import io.github.kingstefan26.stefans_util.core.config.configObject;
import io.github.kingstefan26.stefans_util.core.module.Module;
import io.github.kingstefan26.stefans_util.core.setting.newSetting.general.AbstractSetting;
import io.github.kingstefan26.stefans_util.core.setting.newSetting.general.SettingsCore;

import java.util.ArrayList;

public class MultichoiseSetting extends AbstractSetting {
    private final ArrayList<String> possibleValues;

    public MultichoiseSetting(String name, Module parentModule, String deafultValue, ArrayList<String> possibleValues) {
        super(name, parentModule, SettingsCore.type.multiChoise);
        this.possibleValues = possibleValues;
        this.ConfigObject = new configObject(name, parentModule.getName(), deafultValue);
    }

    public MultichoiseSetting(String name, Module parentModule, String deafultValue, ArrayList<String> possibleValues, String comment) {
        super(name, parentModule, SettingsCore.type.multiChoise);
        this.possibleValues = possibleValues;
        this.comment = comment;
        this.ConfigObject = new configObject(name, parentModule.getName(), deafultValue);
    }

    public void setValue(String value) {
        if (!possibleValues.contains(value)) {
            throw new IllegalArgumentException("This value is not possible with current set!");
        } else {
            this.ConfigObject.setStringValue(value);
        }
    }

    public String getValue() {
        return this.ConfigObject.getStringValue();
    }

    public ArrayList<String> getAllPossibleValues() {
        return possibleValues;
    }
}
