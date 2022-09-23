package io.github.kingstefan26.stefans_util;

import io.github.kingstefan26.stefans_util.core.Globals;
import io.github.kingstefan26.stefans_util.core.Kokomod;
import io.github.kingstefan26.stefans_util.core.config.ConfigManager;
import io.github.kingstefan26.stefans_util.core.config.prop.impl.boolProp;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = Globals.MODID, version = Globals.VERSION, clientSideOnly = true, acceptedMinecraftVersions = "1.8.9")
public class Main {
    private static boolean debug = false;
    private static boolean firstStartup;


    public static final Logger logger = LogManager.getLogger("main-kokomod");

    public static boolean isDebug() {
        return debug;
    }

    public static void setDebug(boolean debug) {
        Main.debug = debug;
    }

    public static boolean isFirstStartup() {
        return firstStartup;
    }

    public static void setFirstStartup(boolean firstStartup) {
        Main.firstStartup = firstStartup;
    }


    @EventHandler
    public void preInit(final FMLPreInitializationEvent event) {
        Kokomod.getkokoMod().init();
    }


    @EventHandler
    public void postInit(final FMLPostInitializationEvent event) {
        boolProp t = (boolProp) ConfigManager.getInstance().getConfigObject("firstStartup", false);
        setFirstStartup(t.getProperty());
        Runtime.getRuntime().addShutdownHook(new Thread(() -> t.set(false)));
    }

}