package me.kokoniara.kokoMod.util;

import me.kokoniara.kokoMod.util.forgeEventClasses.playerTeleported;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class teleportListener {
    private static teleportListener teleportListenerINSTANCE;

    private teleportListener(){
    }

    public static teleportListener getTeleportListner() {
        if(teleportListenerINSTANCE == null){
            teleportListenerINSTANCE = new teleportListener();
            MinecraftForge.EVENT_BUS.register(teleportListenerINSTANCE);
        }
        return teleportListenerINSTANCE;
    }

    private double playerSpeed;
    private double distanceWalked;
    Minecraft mc = Minecraft.getMinecraft();

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if( mc == null || mc.theWorld == null || mc.thePlayer == null ) return;
        EntityPlayerSP p = mc.thePlayer;
        playerSpeed = p.getDistance(p.lastTickPosX, p.lastTickPosY, p.lastTickPosZ);

//        if(distanceWalked >= 20000000){
//            distanceWalked = 0;
//        }else if ((distanceWalked + playerSpeed) - distanceWalked > 8){
//            MinecraftForge.EVENT_BUS.post(new playerTeleported());
//        }
//        distanceWalked = distanceWalked + playerSpeed;
        if(playerSpeed > 1){
            MinecraftForge.EVENT_BUS.post(new playerTeleported());
        }
    }
}