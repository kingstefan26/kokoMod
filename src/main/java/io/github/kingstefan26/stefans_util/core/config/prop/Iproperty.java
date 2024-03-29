/*
 * Copyright (c) 2022. All copyright reserved
 */

package io.github.kingstefan26.stefans_util.core.config.prop;

import java.util.function.Consumer;

public interface Iproperty<T> {
    String getName();

    void setCallBack(Consumer<?> callback);

    T getProperty();

    void setProperty(T value);
}
