package me.kokoniara.kokoMod.settings;

import java.util.ArrayList;

import me.kokoniara.kokoMod.config.configObject;
import me.kokoniara.kokoMod.kokoMod;
import me.kokoniara.kokoMod.module.Module;
import me.kokoniara.kokoMod.config.configMenager;

import static me.kokoniara.kokoMod.config.confgValueType.BOOLEAN;
import static me.kokoniara.kokoMod.config.confgValueType.DOUBLE;
import static me.kokoniara.kokoMod.main.config;


/**
 *  Made by HeroCode
 *  it's free to use
 *  but you have to credit him
 *
 *  @author HeroCode
 */
public class SettingsManager {
	
	private ArrayList<Setting> settings;

	
	public SettingsManager(){
		this.settings = new ArrayList<Setting>();
	}
	
	public void rSetting(Setting in){
		this.settings.add(in);
	}

	public void updateSetting(){

	}
	
	public ArrayList<Setting> getSettings(){
		return this.settings;
	}

	public void putSettings(ArrayList<Setting> a){
		settings = a;
	}

	public ArrayList<Setting> getSettingsByMod(Module mod){
		ArrayList<Setting> out = new ArrayList<Setting>();
		for(Setting s : getSettings()){
			if(s.getParentMod().equals(mod)){
				out.add(s);
			}
		}
		if(out.isEmpty()){
			return null;
		}
		return out;
	}
	
	public Setting getSettingByName(String name){
		for(Setting set : getSettings()){
			if(set.getName().equalsIgnoreCase(name)){
				return set;
			}
		}
		System.err.println("[Tutorial] Error Setting NOT found: '" + name +"'!");
		return null;
	}



}