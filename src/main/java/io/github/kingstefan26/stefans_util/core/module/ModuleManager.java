package io.github.kingstefan26.stefans_util.core.module;

import io.github.kingstefan26.stefans_util.core.clickgui.ClickGui;
import io.github.kingstefan26.stefans_util.core.module.blueprints.Module;
import io.github.kingstefan26.stefans_util.core.module.blueprints.UtilModule;
import io.github.kingstefan26.stefans_util.main;
import io.github.kingstefan26.stefans_util.module.moduleIndex;
import io.github.kingstefan26.stefans_util.util.forgeEventClasses.playerFallEvent;
import io.github.kingstefan26.stefans_util.util.forgeEventClasses.playerTeleportEvent;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;

import static io.github.kingstefan26.stefans_util.main.debug;


public class ModuleManager {

	public static ModuleManager ModuleManager;
	public static ModuleManager getModuleManager() {
		if(ModuleManager == null) ModuleManager = new ModuleManager();
		return ModuleManager;
	}

	private ModuleManager() {
		moduleIndex.instance = moduleIndex.getmoduleIndex();
		if(debug) moduleIndex.getmoduleIndex().loadDebugModules();
		moduleRegistery.getModuleRegistery();
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	public Module getModule(String name) {
		for (Module m : moduleIndex.getmoduleIndex().getAllModules()) {
			if (m.getName().equalsIgnoreCase(name)) {
				return m;
			}
		}
		return null;
	}

	public ArrayList<Module> getModulesInCategory(Category c) {
		ArrayList<Module> mods = new ArrayList<>();
		for (Module m : moduleIndex.getmoduleIndex().getAllModules()) {
			if (m.getCategory() == c) {
				mods.add(m);
			}
		}
		return mods;
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
		for (Module m : moduleIndex.getmoduleIndex().getAllModules()) {
			if(m.isToggled()){
				m.onTick(e.type,e.side,e.phase);
			}
		}
		for (Module m : moduleRegistery.getModuleRegistery().loadedModules) {
			if(m.isToggled()){
				m.onTick(e.type,e.side,e.phase);
			}
		}
		if(!moduleRegistery.getModuleRegistery().loadedUtilModules.isEmpty()){
			for (UtilModule ma : moduleRegistery.getModuleRegistery().loadedUtilModules) {
				ma.onTick(e.type,e.side,e.phase);
			}
		}
	}

	@SubscribeEvent
	public void onWorldRender(RenderWorldLastEvent e){
		for (Module m : moduleIndex.getmoduleIndex().getAllModules()) {
			if(m.isToggled()){
				m.onWorldRender(e.context,e.partialTicks);
			}
		}
		for (Module m : moduleRegistery.getModuleRegistery().loadedModules) {
			if(m.isToggled()){
				m.onWorldRender(e.context,e.partialTicks);
			}
		}

		for (UtilModule m : moduleRegistery.getModuleRegistery().loadedUtilModules) {
			m.onWorldRender(e.context,e.partialTicks);
		}
	}

	@SubscribeEvent
	public void onPlayerFall(playerFallEvent e) {
		for (Module m : moduleIndex.getmoduleIndex().getAllModules()) {
			if(m.isToggled()){
				m.onPlayerFall();
			}
		}
		for (Module m : moduleRegistery.getModuleRegistery().loadedModules) {
			if(m.isToggled()){
				m.onPlayerFall();
			}
		}
		for (UtilModule m : moduleRegistery.getModuleRegistery().loadedUtilModules) {
			m.onPlayerFall();
		}
	}

	@SubscribeEvent
	public void onPlayerTeleport(playerTeleportEvent e) {
		for (Module m : moduleIndex.getmoduleIndex().getAllModules()) {
			if(m.isToggled()){
				m.onPlayerTeleport();
			}
		}
		for (Module m : moduleRegistery.getModuleRegistery().loadedModules) {
			if(m.isToggled()){
				m.onPlayerTeleport();
			}
		}
		for (UtilModule m : moduleRegistery.getModuleRegistery().loadedUtilModules) {
			m.onPlayerTeleport();
		}
	}

	@SubscribeEvent
	public void onGuiRender(RenderGameOverlayEvent e){
		for (Module m : moduleIndex.getmoduleIndex().getAllModules()) {
			if(m.isToggled()){
				m.onGuiRender(e.partialTicks,e.resolution,e.type);
			}
		}
		for (Module m : moduleRegistery.getModuleRegistery().loadedModules) {
			if(m.isToggled()){
				m.onGuiRender(e.partialTicks,e.resolution,e.type);
			}
		}
		for (UtilModule m : moduleRegistery.getModuleRegistery().loadedUtilModules) {
			m.onGuiRender(e.partialTicks,e.resolution,e.type);
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
					for (Module m : moduleIndex.getmoduleIndex().getAllModules()) {
						if (m.getKey() == keyCode) {
							m.toggle();
						}
					}
					for (Module m : moduleRegistery.getModuleRegistery().loadedModules) {
						if (m.getKey() == keyCode) {
							m.toggle();
						}
					}
					if(keyCode == Keyboard.KEY_RSHIFT){
						Minecraft.getMinecraft().displayGuiScreen(ClickGui.getClickGui());
					}
				}
			}
		} catch (Exception q) { q.printStackTrace(); }
	}
}