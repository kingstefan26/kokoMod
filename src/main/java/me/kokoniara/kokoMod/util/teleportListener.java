package me.kokoniara.kokoMod.util;

import me.kokoniara.kokoMod.util.forgeEventClasses.playerTeleportEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class teleportListener {
    public static teleportListener teleportListener;

    private teleportListener() {
    }

    public static teleportListener getTeleportListner() {
        if (teleportListener == null) {
            teleportListener = new teleportListener();
            MinecraftForge.EVENT_BUS.register(teleportListener);
        }
        return teleportListener;
    }

    Minecraft mc = Minecraft.getMinecraft();

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (mc == null || mc.theWorld == null || mc.thePlayer == null) return;
        EntityPlayerSP p = mc.thePlayer;
        double playerSpeed = p.getDistance(p.lastTickPosX, p.lastTickPosY, p.lastTickPosZ);
        if (playerSpeed > 5) {
            MinecraftForge.EVENT_BUS.post(new playerTeleportEvent());
        }
    }
}