package io.github.kingstefan26.kokomod.module;

import io.github.kingstefan26.kokomod.core.module.blueprints.Module;
import io.github.kingstefan26.kokomod.core.module.blueprints.UtilModule;
import io.github.kingstefan26.kokomod.module.combat.LmbAutoCliker;
import io.github.kingstefan26.kokomod.module.combat.RmbAutoCliker;
import io.github.kingstefan26.kokomod.module.macro.macroUtil.lastLeftOff.lastLeftOff;
import io.github.kingstefan26.kokomod.module.macro.sugarCane.caneMacro;
import io.github.kingstefan26.kokomod.module.macro.wart.wartMacro;
import io.github.kingstefan26.kokomod.module.macro.wart.wartMacroVerticalDesign;
import io.github.kingstefan26.kokomod.module.macro.wart.wartMacronoTppad;
import io.github.kingstefan26.kokomod.module.misc.amiTimedOut.amiTimedOut;
import io.github.kingstefan26.kokomod.module.misc.stolenFarmOverlay;
import io.github.kingstefan26.kokomod.module.misc.test.test;
import io.github.kingstefan26.kokomod.module.player.Sprint;
import io.github.kingstefan26.kokomod.module.player.iWillcancelYouOnTwitter;
import io.github.kingstefan26.kokomod.module.render.BatEsp;
import io.github.kingstefan26.kokomod.module.render.ClickGUI;
import io.github.kingstefan26.kokomod.module.render.HUD;
import io.github.kingstefan26.kokomod.module.util.SBinfo;
import io.github.kingstefan26.kokomod.module.util.teleportListener;

import java.util.ArrayList;


public class moduleIndex {
    public static moduleIndex instance;
    public static moduleIndex getmoduleIndex(){
        if(instance == null) instance = new moduleIndex();
        return instance;
    }

    private final ArrayList<Module> productionModuleIndex;
    private final ArrayList<UtilModule> utilModuleIndex;
    private ArrayList<Module> debugModuleIndex = new ArrayList<>();
    private final ArrayList<Module> moduleArray;

    private moduleIndex() {
        productionModuleIndex = new ArrayList<>();
        moduleArray = new ArrayList<>();
        utilModuleIndex = new ArrayList<>();

        productionModuleIndex.add(new ClickGUI());
        productionModuleIndex.add(new iWillcancelYouOnTwitter());
        productionModuleIndex.add(new HUD());
        productionModuleIndex.add(new LmbAutoCliker());
        productionModuleIndex.add(new RmbAutoCliker());
        productionModuleIndex.add(new Sprint());
        productionModuleIndex.add(new BatEsp());
        productionModuleIndex.add(new amiTimedOut());
        productionModuleIndex.add(new caneMacro());
        productionModuleIndex.add(new wartMacro());
        productionModuleIndex.add(new wartMacronoTppad());
        productionModuleIndex.add(wartMacroVerticalDesign.getwartMacroVerticalDesign());
        productionModuleIndex.add(lastLeftOff.getLastLeftOff());
        productionModuleIndex.add(new stolenFarmOverlay());

        utilModuleIndex.add(SBinfo.getSBinfo());
        utilModuleIndex.add(teleportListener.getTeleportListner());

        moduleArray.addAll(productionModuleIndex);

    }

    public void loadDebugModules(){
        debugModuleIndex = new ArrayList<>();

        debugModuleIndex.add(new test());

        moduleArray.addAll(debugModuleIndex);
    }

    public void unloadDebugModules() {
        for(Module m : moduleArray){
            for(Module a : debugModuleIndex){
                if(m.equals(a)){
                    moduleArray.remove(m);
                }
            }
        }

        for(Module a : debugModuleIndex){
            a.setToggled(false);
            Module refrence = debugModuleIndex.get(debugModuleIndex.indexOf(a));
            refrence = null;
        }
        debugModuleIndex = null;
    }

    public ArrayList<Module> getAllModules(){
        return this.moduleArray;
    }

    public ArrayList<Module> getProductionModules(){
        return this.productionModuleIndex;
    }

    public ArrayList<Module> getDebugModules(){
        return this.debugModuleIndex;
    }

    public ArrayList<UtilModule> getUtillModules(){
        return this.utilModuleIndex;
    }

}
