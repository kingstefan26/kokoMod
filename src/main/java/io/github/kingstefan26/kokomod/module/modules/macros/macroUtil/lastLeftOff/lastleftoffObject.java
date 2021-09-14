package io.github.kingstefan26.kokomod.module.modules.macros.macroUtil.lastLeftOff;

import io.github.kingstefan26.kokomod.module.modules.macros.macroUtil.cropType;
import io.github.kingstefan26.kokomod.module.modules.macros.macroUtil.macroStages;

public class lastleftoffObject {
	int x;
	int y;
	int z;
	macroStages lastStage;
	cropType type;
	String comment;
	public lastleftoffObject(int x,int y,int z,cropType type, macroStages macroStage){
		this.x = x;
		this.y = y;
		this.z = z;
		this.type = type;
		this.lastStage = macroStage;
	}
	public lastleftoffObject(int x,int y,int z,cropType type, String comment){
		this.x = x;
		this.y = y;
		this.z = z;
		this.type = type;
		this.comment = comment;
	}
	public int getX() {return this.x;}
	public int getY() {return this.y;}
	public int getZ() {return this.z;}
	public cropType getCropType() {return this.type;}
	public String getComment(){return this.comment;}
	public macroStages getMacroStage(){return this.lastStage;}
}
