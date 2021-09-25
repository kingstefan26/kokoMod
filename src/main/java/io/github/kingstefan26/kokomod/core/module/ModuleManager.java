package io.github.kingstefan26.kokomod.core.module;

import io.github.kingstefan26.kokomod.core.module.blueprints.Module;
import io.github.kingstefan26.kokomod.module.moduleIndex;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import static io.github.kingstefan26.kokomod.main.debug;


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

	@SubscribeEvent
	public void onClientTick(TickEvent.ClientTickEvent e){
		for (Module m : moduleIndex.getmoduleIndex().getAllModules()) {
			if(m.isToggled()){
				m.onTick();
			}
		}
		for (Module m : moduleRegistery.getModuleRegistery().loadedModules) {
			if(m.isToggled()){
				m.onTick();
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
				}
			}
		} catch (Exception q) { q.printStackTrace(); }
	}
}
