package me.kokoniara.kokoMod.util.SBinfo;

public class SBinfo {
	public static SBinfo SBinfo;
	public static SBinfo getSBinfo(){
		if(SBinfo == null) SBinfo = new SBinfo();
		return SBinfo;
	}
}
