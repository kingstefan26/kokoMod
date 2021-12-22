/*
 * Copyright (c) 2021. All copyright reserved
 */

package io.github.kingstefan26.stefans_util.module.experimental.dataHarvest;

public class recordManager {
    private static recordManager instance;
    public static recordManager getInstance() {
        return instance == null ? instance = new recordManager() : instance;
    }
    void init(){
        recordPipeLine.getInstance();
    }
}
