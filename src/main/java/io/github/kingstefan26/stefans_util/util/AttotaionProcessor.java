/*
 * Copyright (c) 2022. All copyright reserved
 */

package io.github.kingstefan26.stefans_util.util;

import java.lang.reflect.Field;

public abstract class AttotaionProcessor {
    public void init(Object parent) {
        try {
            Field[] fields = parent.getClass().getDeclaredFields();

            for (Field field : fields) {
                processField(field, parent);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public abstract void processField(Field field, Object parent);

}
