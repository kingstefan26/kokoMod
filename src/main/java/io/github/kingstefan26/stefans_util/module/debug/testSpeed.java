/*
 * Copyright (c) 2022. All copyright reserved
 */

package io.github.kingstefan26.stefans_util.module.debug;

import io.github.kingstefan26.stefans_util.core.module.moduleFrames.prototypeModule;
import io.github.kingstefan26.stefans_util.module.macro.util.util;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class testSpeed extends prototypeModule {
    public testSpeed() {
        super("testing speed");
    }

    int longtimer = 0;
    BlockPos pos10SecondsAgo;

    @Override
    protected void PROTOTYPETEST() {
//        logger.info(mc.thePlayer.getDistance(0, mc.thePlayer.lastTickPosY, 0));

//        logger.info(mc.thePlayer.posY - mc.thePlayer.lastTickPosY);
        logger.info(mc.thePlayer.fallDistance);
    }

    @SubscribeEvent
    public void onTickClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            longtimer++;
            if (longtimer % 40 == 0) {
                longtimer = 0;
                if (pos10SecondsAgo == null) {
                    pos10SecondsAgo = util.getPlayerFeetBlockPos();
                } else {
                    BlockPos current = util.getPlayerFeetBlockPos();
                    System.out.println(current.distanceSq(pos10SecondsAgo));
                    if (current.distanceSq(pos10SecondsAgo) < 1) {
                        System.out.println("player is not moving!");
                    }
                    pos10SecondsAgo = current;
                }

            }
        }
    }

}
