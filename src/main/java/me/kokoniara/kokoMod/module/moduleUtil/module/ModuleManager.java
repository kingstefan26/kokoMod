package me.kokoniara.kokoMod.module.moduleUtil.module;

import java.util.ArrayList;

import me.kokoniara.kokoMod.module.modules.combat.LmbAutoCliker;
import me.kokoniara.kokoMod.module.modules.macros.macroUtil.lastLeftOff.lastLeftOff;
import me.kokoniara.kokoMod.module.modules.macros.sugarCane.caneMacro;
import me.kokoniara.kokoMod.module.modules.combat.RmbAutoCliker;
import me.kokoniara.kokoMod.module.modules.macros.wart.wartMacro;
import me.kokoniara.kokoMod.module.modules.macros.wart.wartMacroVerticalDesign;
import me.kokoniara.kokoMod.module.modules.macros.wart.wartMacronoTppad;
import me.kokoniara.kokoMod.module.modules.player.*;
import me.kokoniara.kokoMod.module.modules.misc.*;
import me.kokoniara.kokoMod.module.modules.render.*;

import static me.kokoniara.kokoMod.main.debug;


public class ModuleManager {

	public static ModuleManager ModuleManager;
	public static ModuleManager getModuleManager(){
		if(ModuleManager == null) ModuleManager = new ModuleManager();
		return ModuleManager;
	}

	public ArrayList<Module> modules;
	
	public ModuleManager() {
		modules = new ArrayList<Module>();

		this.modules.add(new ClickGUI());
		this.modules.add(new iWillcancelYouOnTwitter());
		this.modules.add(new HUD());
		this.modules.add(new LmbAutoCliker());
		this.modules.add(new RmbAutoCliker());
		this.modules.add(new Sprint());
		this.modules.add(new BatEsp());
		this.modules.add(new amiTimedOut());
		this.modules.add(new caneMacro());
		this.modules.add(new wartMacro());
		this.modules.add(new wartMacronoTppad());
		this.modules.add(new wartMacroVerticalDesign());
		this.modules.add(new lastLeftOff());
		if(debug) this.modules.add(new test());
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
