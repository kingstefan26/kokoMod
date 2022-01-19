/*
 * Copyright (c) 2022. All copyright reserved
 */

package io.github.kingstefan26.stefans_util.module.debug;

import io.github.kingstefan26.stefans_util.core.module.moduleFrames.prototypeModule;

public class testSpeed extends prototypeModule {
    public testSpeed() {
        super("testing speed");
    }

    @Override
    protected void PROTOTYPETEST() {
        logger.info(mc.thePlayer.getDistance(mc.thePlayer.lastTickPosX, mc.thePlayer.lastTickPosY, mc.thePlayer.lastTickPosZ));
    }
}
