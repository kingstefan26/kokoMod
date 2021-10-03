package io.github.kingstefan26.stefans_util.core.module;

import io.github.kingstefan26.stefans_util.core.module.moduleInterface;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;

public class UtilModule implements moduleInterface {

	protected static Minecraft mc = Minecraft.getMinecraft();

	private final String name;


	public UtilModule(String name) {
		super();
		this.name = name;
		this.onEnable();
	}

	public String getName() {
		return this.name;
	}

	@Override
	public void onEnable() {
		MinecraftForge.EVENT_BUS.register(this);
	}

	@Override
	public void onDisable() {
		MinecraftForge.EVENT_BUS.unregister(this);
	}

	@Override
	public void onLoad() {

	}

	@Override
	public void onTick(TickEvent.ClientTickEvent e) {

	}

	@Override
	public void onWorldRender(RenderWorldLastEvent e) {

	}

	@Override
	public void onPlayerFall() {

	}

	@Override
	public void onPlayerTeleport() {

	}

	@Override
	public void onGuiRender(RenderGameOverlayEvent e) {

	}

	@Override
	public void onChat(ClientChatReceivedEvent e) {

	}

	@Override
	public void onUnload() {

	}
}
