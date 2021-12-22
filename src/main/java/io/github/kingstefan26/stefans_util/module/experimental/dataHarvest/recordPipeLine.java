/*
 * Copyright (c) 2021. All copyright reserved
 */

package io.github.kingstefan26.stefans_util.module.experimental.dataHarvest;

import io.github.kingstefan26.stefans_util.module.experimental.dataHarvest.recordables.impl.keyRecordable;
import io.github.kingstefan26.stefans_util.module.experimental.dataHarvest.recordables.recordable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;

public class recordPipeLine {
    private static recordPipeLine instance;
    public static recordPipeLine getInstance() {
        return instance == null ? instance = new recordPipeLine() : instance;
    }

    Logger logger = LogManager.getLogger("recordPipeline");

    public void submitData(recordable recordable){
        processData(recordable);
    }

    private void processData(recordable recordable){
        String content = null;
        if(recordable instanceof keyRecordable){
            content = "key: " + Keyboard.getKeyName(((keyRecordable)recordable).getKey());
        }
        if(content != null)
            recordableDataSaver.getInstance().logEvent(content, System.currentTimeMillis());
    }
}
