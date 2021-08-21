package me.kokoniara.kokoMod.module.misc;

import me.kokoniara.kokoMod.module.Category;
import me.kokoniara.kokoMod.module.Module;
import me.kokoniara.kokoMod.util.renderUtil.drawCenterString;
import me.kokoniara.kokoMod.util.sendChatMessage;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class tpsDispay extends Module {
    private long lastTick;
    private long tps = 0;
    private drawCenterString drawCenterStringOBJ = drawCenterString.getdrawCenterString();

    public tpsDispay() {
        super("tpsMeter", "displays the tsp", Category.MISC, true, " enabled tps display", " disabled tps display");
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
            sendChatMessage.sendClientMessage("current tick rate: " + tps, false);
            //sendChatMessage.sendClientMessage("server tick is late for: " + tps + " miliseconds", false);
        }
        if(System.currentTimeMillis() - this.lastTick >= 1000){
            drawCenterStringOBJ.GuiNotif(mc, "the server stopped responding sad");

        }
    }



    @Override
    public void onEnable(){
        super.onEnable();
        this.lastTick = 0;
    }
}

