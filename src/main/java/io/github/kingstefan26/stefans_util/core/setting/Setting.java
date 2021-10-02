package io.github.kingstefan26.stefans_util.core.setting;

import io.github.kingstefan26.stefans_util.core.config.confgValueType;
import io.github.kingstefan26.stefans_util.core.config.configMenager;
import io.github.kingstefan26.stefans_util.core.config.configObject;
import io.github.kingstefan26.stefans_util.core.module.blueprints.Module;

import java.util.ArrayList;

/**
 *  Made by HeroCode
 *  it's free to use
 *  but you have to credit him
 *
 *  @author HeroCode
 */

public class Setting {

	private String name;
	private Module parent;
	private settingType mode;
	
	private String sval;

	private ArrayList<String> options;
	
	private boolean booleanValue;
	
	private double dval;
	private double min;
	private double max;
	private boolean onlyint = false;

	private confgValueType type;

	private configObject settingConfigObject;


	public Setting(String name, Module parent, confgValueType type){
		this.name = name;
		this.parent = parent;
		this.type = type;

		configMenager.getConfigManager().createConfigObject(new configObject(name, parent.getName(), confgValueType.PERSISTENT));
		settingConfigObject = configMenager.getConfigManager().getConfigObjectByNameAndTypeAndParent(name, parent.getName(), type);

		this.mode = settingType.CHECK;
	}

	public Setting(String name, Module parent, String sval, ArrayList<String> options){
		this.name = name;
		this.parent = parent;
		this.sval = sval;
		this.options = options;
		this.mode = settingType.COMBO;
		this.type = confgValueType.STRING;

		configMenager.getConfigManager().createConfigObject(new configObject(name, parent.getName(), "default"));
		settingConfigObject = configMenager.getConfigManager().getConfigObjectByNameAndTypeAndParent(name, parent.getName(), type);
	}


	public Setting(String name, Module parent, boolean bval){
		this.name = name;
		this.parent = parent;
		this.booleanValue = bval;
		this.mode = settingType.CHECK;
		this.type = confgValueType.BOOLEAN;

		configMenager.getConfigManager().createConfigObject(new configObject(name, parent.getName(), false));
		settingConfigObject = configMenager.getConfigManager().getConfigObjectByNameAndTypeAndParent(name, parent.getName(), type);
	}


	public Setting(String name, Module parent, double dval, double min, double max, boolean onlyint){
		this.name = name;
		this.parent = parent;
		this.dval = dval;
		this.min = min;
		this.max = max;
		this.onlyint = onlyint;
		if(onlyint){
			this.type = confgValueType.INT;
			configMenager.getConfigManager().createConfigObject(new configObject(name, parent.getName(), (int)dval));
			settingConfigObject = configMenager.getConfigManager().getConfigObjectByNameAndTypeAndParent(name, parent.getName(), type);
		}else{
			this.type = confgValueType.DOUBLE;
			configMenager.getConfigManager().createConfigObject(new configObject(name, parent.getName(), dval));
			settingConfigObject = configMenager.getConfigManager().getConfigObjectByNameAndTypeAndParent(name, parent.getName(), type);
		}
		this.mode = settingType.SLIDER;
	}

	
	public String getName(){ return name; }

	public settingType getMode(){ return mode;}
	public ArrayList<String> getOptions(){
		return this.options;
	}
	public Module getParentMod(){ return parent; }

	public String getValString(){
		this.sval = settingConfigObject.getStringValue();
		return this.sval;
	}
	public void setValString(String in){
		settingConfigObject.setStringValue(in);
		this.sval = in;
	}

	public boolean getValBoolean(){
		this.booleanValue = settingConfigObject.getBooleanValue();
		return this.booleanValue;
	}
	public void setValBoolean(boolean in){
		settingConfigObject.setBooleanValue(in);
		this.booleanValue = in;
	}
	
	public double getValDouble(){
		if(this.onlyint){
			this.dval = (int)dval;
			return settingConfigObject.getIntValue();
		}
		return settingConfigObject.getDoubleValue();
	}
	public int getValInt(){
		return settingConfigObject.getIntValue();
	}

	public void setValDouble(double in){
		if(onlyint) {
			settingConfigObject.setIntValue((int)in);
		}else {
			settingConfigObject.setDoubleValue(in);
		}
		this.dval = in;
	}
	public void setValInt(int in){
		settingConfigObject.setIntValue(in);
		this.dval = in;
	}
	
	public double getMin(){
		return this.min;
	}
	
	public double getMax(){
		return this.max;
	}
	
	public boolean isCombo(){
		return this.mode == settingType.COMBO;
	}
	
	public boolean isCheck(){
		return this.mode == settingType.CHECK;
	}
	
	public boolean isSlider(){
		return this.mode == settingType.SLIDER;
	}
	
	public boolean isonlyInt(){
		return this.onlyint;
	}
}
