package io.github.kingstefan26.kokomod.core.module;

import io.github.kingstefan26.kokomod.module.combat.LmbAutoCliker;
import io.github.kingstefan26.kokomod.module.combat.RmbAutoCliker;
import io.github.kingstefan26.kokomod.module.macro.macroUtil.lastLeftOff.lastLeftOff;
import io.github.kingstefan26.kokomod.module.macro.sugarCane.caneMacro;
import io.github.kingstefan26.kokomod.module.macro.wart.wartMacro;
import io.github.kingstefan26.kokomod.module.macro.wart.wartMacroVerticalDesign;
import io.github.kingstefan26.kokomod.module.macro.wart.wartMacronoTppad;
import io.github.kingstefan26.kokomod.module.misc.amiTimedOut;
import io.github.kingstefan26.kokomod.module.misc.test.test;
import io.github.kingstefan26.kokomod.module.player.Sprint;
import io.github.kingstefan26.kokomod.module.player.iWillcancelYouOnTwitter;
import io.github.kingstefan26.kokomod.module.render.BatEsp;
import io.github.kingstefan26.kokomod.module.render.ClickGUI;
import io.github.kingstefan26.kokomod.module.render.HUD;

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
