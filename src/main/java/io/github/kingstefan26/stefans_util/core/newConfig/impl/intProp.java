/*
 * Copyright (c) 2022. All copyright reserved
 */

package io.github.kingstefan26.stefans_util.core.newConfig.impl;

import io.github.kingstefan26.stefans_util.core.newConfig.Iproperty;

public class intProp implements Iproperty {
    private final String name;
    private int value;

    public intProp(String name, int deafultValue) {
        this.name = name;
        this.value = deafultValue;
    }


    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setProperty(Object set) {
        value = (int) set;
    }

    @Override
    public Object getProperty() {
        return value;
    }
}
