package io.github.kingstefan26.stefans_util.core.module;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;

public interface moduleInterface {
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
