package io.github.kingstefan26.stefans_util.core.rewrite.module.ModuleMenagers;


import io.github.kingstefan26.stefans_util.core.rewrite.clickGui.newClickGui;
import io.github.kingstefan26.stefans_util.core.rewrite.module.moduleDecorators.impl.keyBindDecorator;
import io.github.kingstefan26.stefans_util.core.rewrite.module.moduleFrames.basicModule;
import io.github.kingstefan26.stefans_util.util.stefan_utilEvents;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;


public class moduleManager {

	public enum Category {
		COMBAT, MOVEMENT, PLAYER, RENDER, MISC, MACRO, UtilModule, DEBUG
	}


	public static moduleManager ModuleManager;
	public static moduleManager getModuleManager() {
		if(ModuleManager == null) ModuleManager = new moduleManager();
		return ModuleManager;
	}

	private moduleManager() {
		moduleRegistery.getModuleRegistery();
		MinecraftForge.EVENT_BUS.register(this);
	}

	@SubscribeEvent
	public void onClientTick(TickEvent.ClientTickEvent e){
		for (basicModule m : moduleRegistery.getModuleRegistery().loadedModules) {
			if(m.isToggled()){
				m.onTick(e);
			}
		}
	}

	@SubscribeEvent
	public void onWorldRender(RenderWorldLastEvent e){
		for (basicModule m : moduleRegistery.getModuleRegistery().loadedModules) {
			if(m.isToggled()){
				m.onWorldRender(e);
			}
		}
	}

	@SubscribeEvent
	public void onPlayerFall(stefan_utilEvents.playerFallEvent e) {
		for (basicModule m : moduleRegistery.getModuleRegistery().loadedModules) {
			if(m.isToggled()){
				m.onPlayerFall();
			}
		}
	}

	@SubscribeEvent
	public void onPlayerTeleport(stefan_utilEvents.playerTeleportEvent e) {
		for (basicModule m : moduleRegistery.getModuleRegistery().loadedModules) {
			if(m.isToggled()){
				m.onPlayerTeleport();
			}
		}
	}

	@SubscribeEvent
	public void onGuiRender(RenderGameOverlayEvent e){
		for (basicModule m : moduleRegistery.getModuleRegistery().loadedModules) {
			if(m.isToggled()){
				m.onGuiRender(e);
			}
		}
	}

	@SubscribeEvent
	public void onChat(ClientChatReceivedEvent e){
		for (basicModule m : moduleRegistery.getModuleRegistery().loadedModules) {
			if(m.isToggled()){
				m.onChat(e);
			}
		}
	}

	@SubscribeEvent
	public void key(InputEvent.KeyInputEvent e) {
		if (Minecraft.getMinecraft().theWorld == null || Minecraft.getMinecraft().thePlayer == null)
			return;
		try {
			if (Keyboard.isCreated()) {
				if (Keyboard.getEventKeyState()) {
					int keyCode = Keyboard.getEventKey();
					if (keyCode <= 0)
						return;
					for (basicModule m : moduleRegistery.getModuleRegistery().loadedModules) {
						m.moduleDecorators.forEach(decorator -> {
							if(decorator.getClass().getName().equals(keyBindDecorator.class.getName())){
								keyBindDecorator temp = (keyBindDecorator) decorator;
								if(temp.keybind.getKeyCode() == keyCode){
									temp.fireKeyBind();
								}
							}
						});
					}
					if(keyCode == Keyboard.KEY_APOSTROPHE) Minecraft.getMinecraft().displayGuiScreen(newClickGui.getClickGui());
				}
			}
		} catch (Exception ignored) {}
	}
}
