package io.github.kingstefan26.kokomod.module.util;

import io.github.kingstefan26.kokomod.core.module.blueprints.UtilModule;

public class SBinfo extends UtilModule {
    public static SBinfo SBinfo;
    public static SBinfo getSBinfo(){
        if(SBinfo == null) SBinfo = new SBinfo();
        return SBinfo;
    }

    public SBinfo(){
        super("SBinfo");
    }


}
