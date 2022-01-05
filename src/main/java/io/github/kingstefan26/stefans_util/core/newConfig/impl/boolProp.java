/*
 * Copyright (c) 2022. All copyright reserved
 */

package io.github.kingstefan26.stefans_util.core.newConfig.impl;

import io.github.kingstefan26.stefans_util.core.newConfig.Iproperty;

public class boolProp implements Iproperty {
    public boolProp(String name, boolean deafultValue) {
        this.name = name;
        this.value = deafultValue;
    }
    private final String name;

    private boolean value;


    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setProperty(Object set) {
        value = (boolean) set;
    }

    @Override
    public Object getProperty() {
        return value;
    }

}
