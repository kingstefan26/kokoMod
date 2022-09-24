/*
 * Copyright (c) 2022. All copyright reserved
 */

package io.github.kingstefan26.stefans_util.module.misc;

import io.github.kingstefan26.stefans_util.core.module.ModuleManager;
import io.github.kingstefan26.stefans_util.core.module.moduleframes.BasicModule;
import io.github.kingstefan26.stefans_util.service.impl.WorldInfoService;
import io.github.kingstefan26.stefans_util.service.impl.chatService;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class alwaysOnIsland extends BasicModule {
    public alwaysOnIsland() {
        super("alwaysOnIsland", "warps you to your island whenever you leave", ModuleManager.Category.MISC);
    }

    int tickCouter;

    @Override
    public void onTick(TickEvent.ClientTickEvent e) {
        if (e.phase != TickEvent.Phase.START) return;
        tickCouter++;

        if(tickCouter % 20 == 0){
            tickCouter = 0;
            if(WorldInfoService.isOnHypixel()){
               if(!WorldInfoService.isOnPrivateIsland()){
                   chatService.queueClientChatMessage("alwaysOnIsland warped you");
                   chatService.sendMessage("/warp island");
               }
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
