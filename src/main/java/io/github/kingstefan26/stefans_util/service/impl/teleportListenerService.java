package io.github.kingstefan26.stefans_util.service.impl;

import io.github.kingstefan26.stefans_util.service.Service;
import io.github.kingstefan26.stefans_util.util.stefan_utilEvents;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class teleportListenerService extends Service {

    public teleportListenerService() {
        super("teleport listener");
    }


    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (mc == null || mc.theWorld == null || mc.thePlayer == null) return;
        EntityPlayerSP p = mc.thePlayer;
        double playerSpeed = p.getDistance(p.lastTickPosX, p.lastTickPosY, p.lastTickPosZ);
        if (playerSpeed > 5) {
            MinecraftForge.EVENT_BUS.post(new stefan_utilEvents.playerTeleportEvent());
        }
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }
}