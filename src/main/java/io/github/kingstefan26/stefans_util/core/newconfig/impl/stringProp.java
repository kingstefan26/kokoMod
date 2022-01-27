/*
 * Copyright (c) 2022. All copyright reserved
 */

package io.github.kingstefan26.stefans_util.core.newconfig.impl;

import com.google.gson.annotations.Expose;
import io.github.kingstefan26.stefans_util.core.newconfig.Property;


public class stringProp extends Property<String> {
    @Expose
    private String value;

    public stringProp(String name, String deafultValue) {
        super(name);
        this.value = deafultValue;
    }

    @Override
    protected void set(String value) {
        this.value = value;
    }

    @Override
    public String getProperty() {
        return value;
    }
}
