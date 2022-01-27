/*
 * Copyright (c) 2022. All copyright reserved
 */

package io.github.kingstefan26.stefans_util.core.setting.attnotationSettings.attnotaions;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface slidersetting {
    String name();

    String description() default "null";

    double defaultValue();

    double min();

    double max();
}
