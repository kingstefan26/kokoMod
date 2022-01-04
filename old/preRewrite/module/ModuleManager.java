package io.github.kingstefan26.stefans_util.core.preRewrite.module;

import io.github.kingstefan26.stefans_util.service.impl.chatService;
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


public class ModuleManager {

	public enum Category {
		COMBAT, MOVEMENT, PLAYER, RENDER, MISC, MACRO, UtilModule, DEBUG
	}


	public static ModuleManager ModuleManager;
	public static ModuleManager getModuleManager() {
		if(ModuleManager == null) ModuleManager = new ModuleManager();
		return ModuleManager;
	}

	private ModuleManager() {
		moduleRegistery.getModuleRegistery();
		MinecraftForge.EVENT_BUS.register(this);
	}


//	void onEnable();
//	void onDisable();
//	void onLoad();
//	void onTick();
//	void onWorldRender();
//	void onPlayerFall();
//	void onPlayerTeleport();
//	void onGuiRender();
//	void onUnload();

	@SubscribeEvent
	public void onClientTick(TickEvent.ClientTickEvent e){
		for (Module m : moduleRegistery.getModuleRegistery().loadedModules) {
			if(m.isToggled()){
				m.onTick(e);
			}
		}
	}

	@SubscribeEvent
	public void onWorldRender(RenderWorldLastEvent e){
		for (Module m : moduleRegistery.getModuleRegistery().loadedModules) {
			if(m.isToggled()){
				m.onWorldRender(e);
			}
		}
	}

	@SubscribeEvent
	public void onPlayerFall(stefan_utilEvents.playerFallEvent e) {
		for (Module m : moduleRegistery.getModuleRegistery().loadedModules) {
			if(m.isToggled()){
				m.onPlayerFall();
			}
		}
	}

	@SubscribeEvent
	public void onPlayerTeleport(stefan_utilEvents.playerTeleportEvent e) {
		for (Module m : moduleRegistery.getModuleRegistery().loadedModules) {
			if(m.isToggled()){
				m.onPlayerTeleport();
			}
		}
	}

	@SubscribeEvent
	public void onGuiRender(RenderGameOverlayEvent e){
		for (Module m : moduleRegistery.getModuleRegistery().loadedModules) {
			if(m.isToggled()){
				m.onGuiRender(e);
			}
		}
	}

	@SubscribeEvent
	public void onChat(ClientChatReceivedEvent e){
		for (Module m : moduleRegistery.getModuleRegistery().loadedModules) {
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
					for (Module m : moduleRegistery.getModuleRegistery().loadedModules) {
						if (m.getKey() == keyCode) {
//							m.toggle();
						}
					}
					if(keyCode == Keyboard.KEY_RSHIFT){
//						Minecraft.getMinecraft().displayGuiScreen(ClickGui.getClickGui());
						chatService.queueClientChatMessage("sorry this menu has been depricated, try the new APOSTROPHE one");
					}
				}
			}
		} catch (Exception q) { q.printStackTrace(); }
	}
}
