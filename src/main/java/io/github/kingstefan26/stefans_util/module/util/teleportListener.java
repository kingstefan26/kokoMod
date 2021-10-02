package io.github.kingstefan26.stefans_util.module.util;

import io.github.kingstefan26.stefans_util.core.module.UtilModule;
import io.github.kingstefan26.stefans_util.util.forgeEventClasses.playerTeleportEvent;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class teleportListener extends UtilModule {
    public static teleportListener teleportListener;

    public static teleportListener getTeleportListner() {
        if (teleportListener == null) {
            teleportListener = new teleportListener();
        }
        return teleportListener;
    }

    private teleportListener() {
        super("teleport listener");
    }


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