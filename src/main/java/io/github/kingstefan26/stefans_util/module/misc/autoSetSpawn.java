/*
 * Copyright (c) 2022. All copyright reserved
 */

package io.github.kingstefan26.stefans_util.module.misc;

import io.github.kingstefan26.stefans_util.core.module.ModuleMenagers.moduleManager;
import io.github.kingstefan26.stefans_util.core.module.moduleFrames.basicModule;
import io.github.kingstefan26.stefans_util.core.setting.impl.SliderNoDecimalSetting;
import io.github.kingstefan26.stefans_util.service.impl.WorldInfoService;
import io.github.kingstefan26.stefans_util.service.impl.chatService;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class autoSetSpawn extends basicModule {
    public autoSetSpawn() {
        super("autoSetSpawn", "every configurable delay sets spawn on private island", moduleManager.Category.MISC);
    }



    // in minutes
    int ogValue;
    int delay;

    @Override
    public void onLoad() {
        new SliderNoDecimalSetting("delay", this, 10, 1, 50, (newvalue)->{
            ogValue = (int) newvalue;
            delay = updateDelay(ogValue);
        });
        super.onLoad();
    }

    int counter;

    int updateDelay(int base){
        int delay = base + (int)(Math.random() * ((2 - -2) + 1));
        return Math.min(delay, 1);
    }

    @Override
    public void onTick(TickEvent.ClientTickEvent e) {
        if(e.phase != TickEvent.Phase.START) return;
        counter++;

        if(counter >= delay * 1200){
            counter = 0;
            delay = updateDelay(ogValue);
            if(WorldInfoService.isOnPrivateIsland()){
                chatService.queueClientChatMessage("autoSetSpawn set spawn");
                chatService.sendMessage("/setspawn");
            }

        }
    }


    @Override
    public void onEnable() {
        if(mc.isSingleplayer()){
            this.toggle();
        }
        super.onEnable();
    }

}
