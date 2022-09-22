/*
 * Copyright (c) 2022. All copyright reserved
 */

package io.github.kingstefan26.stefans_util.core.setting.attnotationSettings;

import java.lang.reflect.Field;

public interface IannotationProcessor {
    default void init(Object parent) {
        try {
            Field[] fields = parent.getClass().getDeclaredFields();

            for (Field field : fields) {
                processField(field, parent);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    void processField(Field field, Object parent);

}
