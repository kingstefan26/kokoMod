package io.github.kingstefan26.kokomod.core.module;

import io.github.kingstefan26.kokomod.core.module.blueprints.Module;
import io.github.kingstefan26.kokomod.module.moduleIndex;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;

import static io.github.kingstefan26.kokomod.main.debug;


public class ModuleManager {

	public static ModuleManager ModuleManager;
	public static ModuleManager getModuleManager(){
		if(ModuleManager == null) ModuleManager = new ModuleManager();
		return ModuleManager;
	}


	
	public ModuleManager() {
		moduleIndex.instance = moduleIndex.getmoduleIndex();
		if(debug) moduleIndex.getmoduleIndex().loadDebugModules();
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
	
	public ArrayList<Module> getModuleList() {
		return moduleIndex.getmoduleIndex().getAllModules();
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
				}
			}
		} catch (Exception q) { q.printStackTrace(); }
	}
}
