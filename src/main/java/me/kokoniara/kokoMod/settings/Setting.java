package me.kokoniara.kokoMod.settings;

import java.util.ArrayList;

import me.kokoniara.kokoMod.config.configObject;
import me.kokoniara.kokoMod.kokoMod;
import me.kokoniara.kokoMod.module.Module;

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
	
	private boolean bval;
	
	private double dval;
	private double min;
	private double max;
	private boolean onlyint = false;
	

	public Setting(String name, Module parent, String sval, ArrayList<String> options){
		this.name = name;
		this.parent = parent;
		this.sval = sval;
		this.options = options;
		this.mode = settingType.COMBO;
	}
	
	public Setting(String name, Module parent, boolean bval){
		this.name = name;
		this.parent = parent;
		this.bval = bval;
		this.mode = settingType.CHECK;
	}
	
	public Setting(String name, Module parent, double dval, double min, double max, boolean onlyint){
		this.name = name;
		this.parent = parent;
		this.dval = dval;
		this.min = min;
		this.max = max;
		this.onlyint = onlyint;
		this.mode = settingType.SLIDER;
	}
	
	public String getName(){

		return name;
	}

	public settingType getMode(){ return mode;}
	
	public Module getParentMod(){
		return parent;
	}
	
	public String getValString(){
		return this.sval;
	}
	
	public void setValString(String in){
		this.sval = in;
	}
	
	public ArrayList<String> getOptions(){
		return this.options;
	}
	
	public boolean getValBoolean(){
		return this.bval;
	}
	
	public void setValBoolean(boolean in){

		this.bval = in;
	}
	
	public double getValDouble(){
		if(this.onlyint){
			this.dval = (int)dval;
		}
		return this.dval;
	}

	public void setValDouble(double in){
		this.dval = in;
	}
	
	public double getMin(){
		return this.min;
	}
	
	public double getMax(){
		return this.max;
	}
	
	public boolean isCombo(){
		if(this.mode == settingType.COMBO){
			return true;
		}else{
			return false;
		}
	}
	
	public boolean isCheck(){
		if(this.mode == settingType.CHECK){
			return true;
		}else{
			return false;
		}
	}
	
	public boolean isSlider(){
		if(this.mode == settingType.SLIDER){
			return true;
		}else{
			return false;
		}
	}
	
	public boolean onlyInt(){
		return this.onlyint;
	}
}
