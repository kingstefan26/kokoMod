package io.github.kingstefan26.kokomod.core.module;

import java.util.ArrayList;

public class moduleIndex {
    public static ArrayList<String> productionModuleIndex;
    public static ArrayList<String> debugModuleIndex;
    public static ArrayList<String> utilModuleIndex;

    public static void init(){
        productionModuleIndex = new ArrayList<>();
        debugModuleIndex = new ArrayList<>();
        utilModuleIndex = new ArrayList<>();

        productionModuleIndex.add("io.github.kingstefan26.kokomod.module.combat.LmbAutoCliker");
        productionModuleIndex.add("io.github.kingstefan26.kokomod.module.combat.RmbAutoCliker");
        productionModuleIndex.add("io.github.kingstefan26.kokomod.module.macro.sugarCane.caneMacro");
        productionModuleIndex.add("io.github.kingstefan26.kokomod.module.macro.wart.wartMacro");
        productionModuleIndex.add("io.github.kingstefan26.kokomod.module.macro.wart.wartMacronoTppad");
        productionModuleIndex.add("io.github.kingstefan26.kokomod.module.misc.amiTimedOut.amiTimedOut");
        productionModuleIndex.add("io.github.kingstefan26.kokomod.module.misc.stolenFarmOverlay");
        productionModuleIndex.add("io.github.kingstefan26.kokomod.module.player.Sprint");
        productionModuleIndex.add("io.github.kingstefan26.kokomod.module.player.dynamicRender");
        productionModuleIndex.add("io.github.kingstefan26.kokomod.module.player.iWillcancelYouOnTwitter");
        productionModuleIndex.add("io.github.kingstefan26.kokomod.module.render.BatEsp");
        productionModuleIndex.add("io.github.kingstefan26.kokomod.module.render.HUD");

        utilModuleIndex.add("io.github.kingstefan26.kokomod.module.util.ClickGUI");

    }

}
