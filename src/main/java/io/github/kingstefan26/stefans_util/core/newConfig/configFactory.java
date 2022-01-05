/*
 * Copyright (c) 2022. All copyright reserved
 */

package io.github.kingstefan26.stefans_util.core.newConfig;

import io.github.kingstefan26.stefans_util.core.newConfig.impl.*;

public class configFactory {
    private static configFactory instance;
    public static configFactory getInstance() {
        return instance == null ? instance = new configFactory() : instance;
    }

    public Iproperty getProperty(String name, Object deafultValue){
        if(deafultValue instanceof String){
            return new stringProp(name, (String) deafultValue);
        } else if (deafultValue instanceof Double){
            return new doubleProp(name, (Double) deafultValue);
        } else if (deafultValue instanceof Integer) {
            return new intProp(name, (int) deafultValue);
        } else if (deafultValue instanceof Boolean) {
            return new boolProp(name, (boolean) deafultValue);
        } else if (deafultValue instanceof Float) {
            return new floatProp(name, (Float) deafultValue);
        }
        return null;
    }
}
