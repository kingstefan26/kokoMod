package io.github.kingstefan26.stefans_util.core.rewrite.setting.impl;

import io.github.kingstefan26.stefans_util.core.config.configObject;
import io.github.kingstefan26.stefans_util.core.rewrite.module.moduleFrames.basicModule;
import io.github.kingstefan26.stefans_util.core.rewrite.setting.general.AbstractSetting;
import io.github.kingstefan26.stefans_util.core.rewrite.setting.general.SettingsCore;

import java.util.ArrayList;
import java.util.function.Consumer;

public class MultichoiseSetting extends AbstractSetting {
    private final ArrayList<String> possibleValues;

    public MultichoiseSetting(String name, basicModule parentModule, String deafultValue, ArrayList<String> possibleValues, Consumer<Object> callback) {
        super(name, parentModule, SettingsCore.type.multiChoise, callback);
        this.possibleValues = possibleValues;
        this.ConfigObject = new configObject(name, parentModule.getName(), deafultValue);
    }

    public MultichoiseSetting(String name, basicModule parentModule, String deafultValue, ArrayList<String> possibleValues,Consumer<Object> callback, String comment) {
        super(name, parentModule, SettingsCore.type.multiChoise, callback);
        this.possibleValues = possibleValues;
        this.comment = comment;
        this.ConfigObject = new configObject(name, parentModule.getName(), deafultValue);
    }

    public void setValue(String value) {
        if (!possibleValues.contains(value)) {
            throw new IllegalArgumentException("This value is not possible with current set!");
        } else {
            this.callback.accept(value);
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
