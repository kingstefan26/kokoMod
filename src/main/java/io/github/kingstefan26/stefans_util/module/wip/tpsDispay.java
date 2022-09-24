/*
 * Copyright (c) 2022. All copyright reserved
 */

package io.github.kingstefan26.stefans_util.module.wip;

import io.github.kingstefan26.stefans_util.core.module.ModuleManager;
import io.github.kingstefan26.stefans_util.core.module.moduleDecorators.impl.onoffMessageDecorator;
import io.github.kingstefan26.stefans_util.core.module.moduleframes.BasicModule;
import io.github.kingstefan26.stefans_util.service.impl.chatService;
import io.github.kingstefan26.stefans_util.util.renderUtil.drawCenterString;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class tpsDispay extends BasicModule {
    private long lastTick;
    private long tps = 0;

    public tpsDispay() {
        super("tpsMeter", "displays the tsp", ModuleManager.Category.WIP, new onoffMessageDecorator());
    }

    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent event) {
        long currenttime = System.currentTimeMillis();
        long temp = currenttime - lastTick;
        if(temp == 0) return;
        tps = 1000 / temp;
        System.out.println("current tick time: " + currenttime + ", last tick time: " + lastTick + ", the diffrance bettwen the last tick and currnet tick: " + temp + ", the diffrance divided by 1000: "+ tps);
        lastTick = currenttime;
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event){
        if(tps < 20){
            //drawCenterString.GuiNotif(mc, "the server tick rate is: " + tps);
//            kokoMod.instance.renderEngine.drawCenterString.GuiNotif(mc, "the server tick rate is: " + tps);
            chatService.queueClientChatMessage("current tick rate: " + tps, chatService.chatEnum.NOPREFIX);
            //sendChatMessage.sendClientMessage("server tick is late for: " + tps + " miliseconds", false);
        }
        if(System.currentTimeMillis() - this.lastTick >= 1000){
            drawCenterString.GuiNotif(mc, "the server stopped responding sad");
        }
    }



    @Override
    public void onEnable(){
        super.onEnable();
        this.lastTick = 0;
    }
}

