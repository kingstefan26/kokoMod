package io.github.kingstefan26.stefans_util.core.module.interfaces;

import io.github.kingstefan26.stefans_util.util.StefanutilEvents;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public interface moduleMinecraftInterfaceEvents {
    void onTick(TickEvent.ClientTickEvent e);

    void onRenderTick(TickEvent.RenderTickEvent e);

    void onWorldRender(RenderWorldLastEvent e);

    void onPlayerFall();

    void onPlayerTeleport();

    void onGuiRender(RenderGameOverlayEvent e);

    void onChat(ClientChatReceivedEvent e);

    void onGuiOpen(GuiOpenEvent e);

    void onPlayerTeleportEvent(StefanutilEvents.playerTeleportEvent e);

    void onUnloadWorld(WorldEvent.Unload event);

    void onHighestClientTick(TickEvent.ClientTickEvent event);

    void onKeyInput(InputEvent.KeyInputEvent event);
}
