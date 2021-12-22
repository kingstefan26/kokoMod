/*
 * Copyright (c) 2021. All copyright reserved
 */

package io.github.kingstefan26.stefans_util.module.experimental.dataHarvest.recordables.impl;

import io.github.kingstefan26.stefans_util.module.experimental.dataHarvest.recordables.recordable;

public class mouseClickRecordable extends recordable {
    int button;
    public mouseClickRecordable(long timestamp) {
        super(timestamp);
    }
}
