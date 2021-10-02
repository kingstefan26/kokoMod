package io.github.kingstefan26.stefans_util.module.macro.macroUtil.lastLeftOff;

import io.github.kingstefan26.stefans_util.module.macro.macroUtil.cropType;
import io.github.kingstefan26.stefans_util.module.macro.macroUtil.macroStages;

public class lastleftoffObject {
	float x;
	float y;
	float z;
	long time;
	macroStages lastStage;
	cropType type;
	public lastleftoffObject(float x,float y,float z,cropType type, macroStages macroStage){
		this.x = x;
		this.y = y;
		this.z = z;
		this.type = type;
		this.lastStage = macroStage;
	}
	public lastleftoffObject(float x,float y,float z,cropType type, macroStages macroStage, long time){
		this.x = x;
		this.y = y;
		this.z = z;
		this.time = time;
		this.type = type;
		this.lastStage = macroStage;
	}
	public float getX() {return this.x;}
	public float getY() {return this.y;}
	public float getZ() {return this.z;}
	public long getTime() {return this.time;}
	public cropType getCropType() {return this.type;}
	public macroStages getMacroStage(){return this.lastStage;}
}
