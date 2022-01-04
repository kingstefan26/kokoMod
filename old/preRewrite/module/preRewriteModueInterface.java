package io.github.kingstefan26.stefans_util.core.preRewrite.module;

import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public interface preRewriteModueInterface {
    void onEnable();
    void onDisable();
    void onLoad();
    void onTick(TickEvent.ClientTickEvent e);
    void onWorldRender(RenderWorldLastEvent e);
    void onPlayerFall();
    void onPlayerTeleport();
    void onGuiRender(RenderGameOverlayEvent e);
    void onChat(ClientChatReceivedEvent e);
    void onUnload();
}
