package io.github.kingstefan26.stefans_util.module;

import io.github.kingstefan26.stefans_util.core.module.blueprints.Module;
import io.github.kingstefan26.stefans_util.core.module.blueprints.UtilModule;

import io.github.kingstefan26.stefans_util.module.debug.testTracer;


import io.github.kingstefan26.stefans_util.module.debug.test.test;


import io.github.kingstefan26.stefans_util.module.macro.macroUtil.lastLeftOff.lastLeftOff;
import io.github.kingstefan26.stefans_util.module.macro.wart.wartMacroVerticalDesign;
import io.github.kingstefan26.stefans_util.module.util.SBinfo;
import io.github.kingstefan26.stefans_util.module.util.teleportListener;
import io.github.kingstefan26.stefans_util.module.render.blurClickGui;

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


        productionModuleIndex.add(wartMacroVerticalDesign.getwartMacroVerticalDesign());
        productionModuleIndex.add(lastLeftOff.getLastLeftOff());
        productionModuleIndex.add(blurClickGui.getBlurClickGui());

        //utilModuleIndex.add(SBinfo.getSBinfo());
        utilModuleIndex.add(teleportListener.getTeleportListner());

        moduleArray.addAll(productionModuleIndex);

    }

    public void loadDebugModules(){
        debugModuleIndex = new ArrayList<>();

        debugModuleIndex.add(new test());
        //debugModuleIndex.add(testTracer.gettesttracer());

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
