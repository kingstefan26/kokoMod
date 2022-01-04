package io.github.kingstefan26.stefans_util.core.preRewrite.setting;

import io.github.kingstefan26.stefans_util.core.preRewrite.module.Module;

import java.util.ArrayList;


/**
 *  Made by HeroCode
 *  it's free to use
 *  but you have to credit him
 *
 *  im about to make this so diffrent that miss herocode is gonna shit himself
 *
 *  @author HeroCode
 */
public class SettingsManager {

	public static SettingsManager SettingsManager;

	public static SettingsManager getSettingsManager(){
		if(SettingsManager == null) SettingsManager = new SettingsManager();
		return SettingsManager;
	}

	private ArrayList<Setting> settings;

	
	public SettingsManager(){
		this.settings = new ArrayList<Setting>();
	}
	
	public void rSetting(Setting in){
		this.settings.add(in);
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
		System.err.println("[KOKOMOD-DEBUG] Error Setting NOT found: '" + name +"'!");
		return null;
	}

	public Setting getSettingByName(String name, Module mod){
		for(Setting set : getSettings()){
			if(set.getParentMod().equals(mod)){
				if(set.getName().equalsIgnoreCase(name)){
					return set;
				}
			}
		}
		System.err.println("[KOKOMOD-DEBUG] Error Setting NOT found: '" + name +"'!");
		return null;
	}


}