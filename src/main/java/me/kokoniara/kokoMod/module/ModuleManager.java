package me.kokoniara.kokoMod.module;

import java.util.ArrayList;

import me.kokoniara.kokoMod.module.combat.LmbAutoCliker;
import me.kokoniara.kokoMod.module.misc.amiTimedOut;
import me.kokoniara.kokoMod.module.misc.macos.caneMacro;
import me.kokoniara.kokoMod.module.misc.farmReadycane;
import me.kokoniara.kokoMod.module.combat.RmbAutoCliker;
import me.kokoniara.kokoMod.module.misc.tpsDispay;
import me.kokoniara.kokoMod.module.player.Sprint;
import me.kokoniara.kokoMod.module.player.iWillcancelYouOnTwitter;
import me.kokoniara.kokoMod.module.render.BatEsp;
import me.kokoniara.kokoMod.module.render.ClickGUI;
import me.kokoniara.kokoMod.module.render.HUD;

public class ModuleManager {

	public ArrayList<Module> modules;
	
	public ModuleManager() {
		(modules = new ArrayList<Module>()).clear();

		this.modules.add(new ClickGUI());
		this.modules.add(new iWillcancelYouOnTwitter());
		this.modules.add(new HUD());
		this.modules.add(new LmbAutoCliker());
		this.modules.add(new RmbAutoCliker());
		this.modules.add(new Sprint());
		this.modules.add(new BatEsp());
		this.modules.add(new farmReadycane());
		this.modules.add(new tpsDispay());
		this.modules.add(new amiTimedOut());
		this.modules.add(new caneMacro());
	}
	
	public Module getModule(String name) {
		for (Module m : this.modules) {
			if (m.getName().equalsIgnoreCase(name)) {
				return m;
			}
		}
		return null;
	}
	
	public ArrayList<Module> getModuleList() {
		return this.modules;
	}
	
	public ArrayList<Module> getModulesInCategory(Category c) {
		ArrayList<Module> mods = new ArrayList<Module>();
		for (Module m : this.modules) {
			if (m.getCategory() == c) {
				mods.add(m);
			}
		}
		return mods;
	}
}
