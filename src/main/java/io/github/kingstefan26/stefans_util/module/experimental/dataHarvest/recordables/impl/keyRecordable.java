/*
 * Copyright (c) 2021. All copyright reserved
 */

package io.github.kingstefan26.stefans_util.module.experimental.dataHarvest.recordables.impl;

import io.github.kingstefan26.stefans_util.module.experimental.dataHarvest.recordables.recordable;

public class keyRecordable extends recordable {
    public int getKey() {
        return key;
    }

    int key;

    public keyRecordable(long timestamp, int key) {
        super(timestamp);
        this.key = key;
    }
}
