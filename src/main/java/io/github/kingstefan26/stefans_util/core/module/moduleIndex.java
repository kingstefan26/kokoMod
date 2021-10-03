package io.github.kingstefan26.stefans_util.core.module;

import java.util.ArrayList;

public class moduleIndex {
    public static ArrayList<String> productionModuleIndex;
    public static ArrayList<String> debugModuleIndex;
    public static ArrayList<String> utilModuleIndex;

    public static void init(){
        productionModuleIndex = new ArrayList<>();
        debugModuleIndex = new ArrayList<>();
        utilModuleIndex = new ArrayList<>();

        productionModuleIndex.add("io.github.kingstefan26.stefans_util.module.combat.LmbAutoCliker");
        productionModuleIndex.add("io.github.kingstefan26.stefans_util.module.combat.RmbAutoCliker");
        productionModuleIndex.add("io.github.kingstefan26.stefans_util.module.macro.sugarCane.caneMacro");
        productionModuleIndex.add("io.github.kingstefan26.stefans_util.module.macro.wart.wartMacro");
        productionModuleIndex.add("io.github.kingstefan26.stefans_util.module.macro.wart.wartMacronoTppad");
        productionModuleIndex.add("io.github.kingstefan26.stefans_util.module.misc.amiTimedOut");
        productionModuleIndex.add("io.github.kingstefan26.stefans_util.module.misc.stolenFarmOverlay");
        productionModuleIndex.add("io.github.kingstefan26.stefans_util.module.player.Sprint");
        productionModuleIndex.add("io.github.kingstefan26.stefans_util.module.player.dynamicRender");
        productionModuleIndex.add("io.github.kingstefan26.stefans_util.module.player.iWillcancelYouOnTwitter");
        productionModuleIndex.add("io.github.kingstefan26.stefans_util.module.render.BatEsp");
        productionModuleIndex.add("io.github.kingstefan26.stefans_util.module.render.HUD");
        productionModuleIndex.add("io.github.kingstefan26.stefans_util.module.misc.cashmoney");
        productionModuleIndex.add("io.github.kingstefan26.stefans_util.module.macro.wart.UniversalWartMacro");
        productionModuleIndex.add("io.github.kingstefan26.stefans_util.module.render.blurClickGui");
        productionModuleIndex.add("io.github.kingstefan26.stefans_util.module.macro.macroUtil.lastLeftOff.lastLeftOff");
        productionModuleIndex.add("io.github.kingstefan26.stefans_util.module.macro.wart.wartMacroVerticalDesign");

        debugModuleIndex.add("io.github.kingstefan26.stefans_util.module.debug.testingchat");
        debugModuleIndex.add("io.github.kingstefan26.stefans_util.module.debug.testRaytrace");
        debugModuleIndex.add("io.github.kingstefan26.stefans_util.module.debug.testTracer");
        debugModuleIndex.add("io.github.kingstefan26.stefans_util.module.debug.testttf");
        debugModuleIndex.add("io.github.kingstefan26.stefans_util.module.debug.keyMouselocktest");


        utilModuleIndex.add("io.github.kingstefan26.stefans_util.module.util.chat");
        utilModuleIndex.add("io.github.kingstefan26.stefans_util.module.util.SBinfo");
        utilModuleIndex.add("io.github.kingstefan26.stefans_util.module.util.teleportListener");
        utilModuleIndex.add("io.github.kingstefan26.stefans_util.module.util.inputLocker");


    }

}
