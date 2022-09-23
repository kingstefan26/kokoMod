/*
 * Copyright (c) 2022. All copyright reserved
 */

package io.github.kingstefan26.stefans_util.core.config.attotations;

import java.lang.reflect.Field;

public class ProcessedFiled {
    public final Field filed;
    public final Object parent;

    public ProcessedFiled(Field filed, Object parent) {
        this.filed = filed;
        this.parent = parent;
    }
}
