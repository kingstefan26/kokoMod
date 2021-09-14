package io.github.kingstefan26.kokomod.module.moduleUtil.module;

import io.github.kingstefan26.kokomod.module.modules.combat.LmbAutoCliker;
import io.github.kingstefan26.kokomod.module.modules.combat.RmbAutoCliker;
import io.github.kingstefan26.kokomod.module.modules.macros.macroUtil.lastLeftOff.lastLeftOff;
import io.github.kingstefan26.kokomod.module.modules.macros.sugarCane.caneMacro;
import io.github.kingstefan26.kokomod.module.modules.macros.wart.wartMacro;
import io.github.kingstefan26.kokomod.module.modules.macros.wart.wartMacroVerticalDesign;
import io.github.kingstefan26.kokomod.module.modules.macros.wart.wartMacronoTppad;
import io.github.kingstefan26.kokomod.module.modules.misc.amiTimedOut;
import io.github.kingstefan26.kokomod.module.modules.misc.test.test;
import io.github.kingstefan26.kokomod.module.modules.player.Sprint;
import io.github.kingstefan26.kokomod.module.modules.player.iWillcancelYouOnTwitter;
import io.github.kingstefan26.kokomod.module.modules.render.BatEsp;
import io.github.kingstefan26.kokomod.module.modules.render.ClickGUI;
import io.github.kingstefan26.kokomod.module.modules.render.HUD;

import java.util.ArrayList;

import static io.github.kingstefan26.kokomod.main.debug;


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
		this.modules.add(wartMacroVerticalDesign.getwartMacroVerticalDesign());
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
