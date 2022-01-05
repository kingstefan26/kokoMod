/*
 * Copyright (c) 2022. All copyright reserved
 */

package io.github.kingstefan26.stefans_util.core.newConfig.impl;

import io.github.kingstefan26.stefans_util.core.newConfig.Iproperty;

public class floatProp implements Iproperty {
    private final String name;
    private float value;

    public floatProp(String name, float deafultValue) {
        this.name = name;
        this.value = deafultValue;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setProperty(Object set) {
        value = (float) set;
    }

    @Override
    public Object getProperty() {
        return value;
    }
}
