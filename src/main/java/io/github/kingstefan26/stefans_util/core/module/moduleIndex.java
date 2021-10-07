package io.github.kingstefan26.stefans_util.core.module;

import com.google.common.reflect.ClassPath;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class moduleIndex {
    public static ArrayList<String> productionModuleIndex;
    public static ArrayList<String> debugModuleIndex;
    public static ArrayList<String> utilModuleIndex;
    static Logger logger = LogManager.getLogger("moduleIndex");


    /**
     * IM NOT DOCUMENTIGN THIS SHIT HAHA LLL
     */
    public static void findAllclassNames(){
        productionModuleIndex = new ArrayList<>();
        debugModuleIndex = new ArrayList<>();
        utilModuleIndex = new ArrayList<>();

        ArrayList<String> clazzlist = new ArrayList<>();
        try{
            final ClassLoader loader = Thread.currentThread().getContextClassLoader();

            for (final ClassPath.ClassInfo info : ClassPath.from(loader).getTopLevelClasses()) {
                if (info.getName().startsWith("io.github.kingstefan26.stefans_util.module")) {
                    final Class<?> clazz = info.load();
                    if(Module.class.isAssignableFrom(clazz) || UtilModule.class.isAssignableFrom(clazz)) {
                        clazzlist.add(clazz.getName());
                    }
                }
            }

        }catch(Exception ignored){}

        final String DebugRegex = "io\\.github\\.kingstefan26\\.stefans_util\\.module\\.debug.+";
        final String utilRegex = "io\\.github\\.kingstefan26\\.stefans_util\\.module\\.util.+";


        final Pattern DebugRegexPattern = Pattern.compile(DebugRegex, Pattern.MULTILINE);
        final Pattern utilRegexPattern = Pattern.compile(utilRegex, Pattern.MULTILINE);


        for(String clazzname : clazzlist){
            Matcher Match0 = DebugRegexPattern.matcher(clazzname);
            Matcher Match1 = utilRegexPattern.matcher(clazzname);
            boolean foundmatch = false;
            while (Match0.find()) {
                //logger.info("Full match: " + Match0.group(0));
                foundmatch = true;
                debugModuleIndex.add(clazzname);
            }
            while (Match1.find()) {
                //logger.info("Full match: " + Match1.group(0));
                foundmatch = true;
                utilModuleIndex.add(clazzname);

            }

            if(!foundmatch) {
                productionModuleIndex.add(clazzname);
            }

        }

    }

    public static void init(){

        findAllclassNames();

//        productionModuleIndex.add("io.github.kingstefan26.stefans_util.module.combat.LmbAutoCliker");
//        productionModuleIndex.add("io.github.kingstefan26.stefans_util.module.combat.RmbAutoCliker");
//        productionModuleIndex.add("io.github.kingstefan26.stefans_util.module.macro.sugarCane.caneMacro");
//        productionModuleIndex.add("io.github.kingstefan26.stefans_util.module.macro.wart.wartMacro");
//        productionModuleIndex.add("io.github.kingstefan26.stefans_util.module.macro.wart.wartMacronoTppad");
//        productionModuleIndex.add("io.github.kingstefan26.stefans_util.module.misc.amiTimedOut");
//        productionModuleIndex.add("io.github.kingstefan26.stefans_util.module.misc.stolenFarmOverlay");
//        productionModuleIndex.add("io.github.kingstefan26.stefans_util.module.player.Sprint");
//        productionModuleIndex.add("io.github.kingstefan26.stefans_util.module.player.dynamicRender");
//        productionModuleIndex.add("io.github.kingstefan26.stefans_util.module.player.iWillcancelYouOnTwitter");
//        productionModuleIndex.add("io.github.kingstefan26.stefans_util.module.render.BatEsp");
//        productionModuleIndex.add("io.github.kingstefan26.stefans_util.module.render.HUD");
//        productionModuleIndex.add("io.github.kingstefan26.stefans_util.module.misc.cashmoney");
//        productionModuleIndex.add("io.github.kingstefan26.stefans_util.module.macro.wart.UniversalWartMacro");
//        productionModuleIndex.add("io.github.kingstefan26.stefans_util.module.render.blurClickGui");
//        productionModuleIndex.add("io.github.kingstefan26.stefans_util.module.macro.macroUtil.lastLeftOff.lastLeftOff");
//        productionModuleIndex.add("io.github.kingstefan26.stefans_util.module.macro.wart.wartMacroVerticalDesign");
//
//        debugModuleIndex.add("io.github.kingstefan26.stefans_util.module.debug.testingchat");
//        debugModuleIndex.add("io.github.kingstefan26.stefans_util.module.debug.testRaytrace");
//        debugModuleIndex.add("io.github.kingstefan26.stefans_util.module.debug.testTracer");
//        debugModuleIndex.add("io.github.kingstefan26.stefans_util.module.debug.testttf");
//        debugModuleIndex.add("io.github.kingstefan26.stefans_util.module.debug.keyMouselocktest");
//
//
//        utilModuleIndex.add("io.github.kingstefan26.stefans_util.module.util.chat");
//        utilModuleIndex.add("io.github.kingstefan26.stefans_util.module.util.SBinfo");
//        utilModuleIndex.add("io.github.kingstefan26.stefans_util.module.util.teleportListener");
//        utilModuleIndex.add("io.github.kingstefan26.stefans_util.module.util.inputLocker");

        logger.info("producrion modules:");
        productionModuleIndex.forEach(logger::info);
        logger.info("debug modules:");
        debugModuleIndex.forEach(logger::info);
        logger.info("util modules:");
        utilModuleIndex.forEach(logger::info);
    }

}
