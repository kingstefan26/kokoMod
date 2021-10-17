package io.github.kingstefan26.stefans_util.core.rewrite.module.interfaces;

import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public interface moduleMinecraftInterfaceEvents {
    void onTick(TickEvent.ClientTickEvent e);
    void onWorldRender(RenderWorldLastEvent e);
    void onPlayerFall();
    void onPlayerTeleport();
    void onGuiRender(RenderGameOverlayEvent e);
    void onChat(ClientChatReceivedEvent e);
}
