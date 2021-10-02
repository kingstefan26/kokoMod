package io.github.kingstefan26.stefans_util.core.module.blueprints;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;

public interface moduleInterface {
    void onEnable();
    void onDisable();
    void onLoad();
    void onTick(TickEvent.Type type, Side side, TickEvent.Phase phase);
    void onWorldRender(RenderGlobal context, float partialTick);
    void onPlayerFall();
    void onPlayerTeleport();
    void onGuiRender(float partialTicks, ScaledResolution resolution, RenderGameOverlayEvent.ElementType type);
    void onUnload();
}
