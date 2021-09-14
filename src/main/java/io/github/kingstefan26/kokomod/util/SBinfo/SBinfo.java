package io.github.kingstefan26.kokomod.util.SBinfo;

public class SBinfo {
	public static SBinfo SBinfo;
	public static SBinfo getSBinfo(){
		if(SBinfo == null) SBinfo = new SBinfo();
		return SBinfo;
	}
}
