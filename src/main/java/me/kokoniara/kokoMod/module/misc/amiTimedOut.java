package me.kokoniara.kokoMod.module.misc;

import me.kokoniara.kokoMod.module.Category;
import me.kokoniara.kokoMod.module.Module;
import me.kokoniara.kokoMod.util.sendChatMessage;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class amiTimedOut extends Module {
    private long serverPing;
    public amiTimedOut() {
        super("am i Timed out?", "shows if you timed out", Category.MISC, true, " amitimedout enabled", " amitimedout diasabled");
    }
    @SubscribeEvent
    public void onPlayerTick(TickEvent.ClientTickEvent e){
        serverPing = mc.getCurrentServerData().pingToServer;
        //System.out.println("serverPing: " + serverPing);
        sendChatMessage.sendClientMessage("serverPing: " + serverPing, false);
    }

}