package io.github.kingstefan26.stefans_util;

import io.github.kingstefan26.stefans_util.core.commands.commandRegistry;
import io.github.kingstefan26.stefans_util.core.config.configObject;
import io.github.kingstefan26.stefans_util.core.globals;
import io.github.kingstefan26.stefans_util.core.kokoMod;
import net.minecraft.command.CommandBase;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = globals.MODID, version = globals.VERSION, clientSideOnly = true, acceptedMinecraftVersions = "1.8.9")
public class main {
    public static boolean debug = false;
    public static boolean firstStartup;

    @Mod.Instance
    public static main instance;

    public static final Logger logger = LogManager.getLogger("main-kokomod");


    public static boolean connectedToKokoCLoud = false;


    @EventHandler
    public void preInit(final FMLPreInitializationEvent event) {
        for (CommandBase a : commandRegistry.simpleCommands) {
            ClientCommandHandler.instance.registerCommand(a);
        }

        kokoMod.getkokoMod().init();
    }



    @EventHandler
    public void postInit(final FMLPostInitializationEvent event) {
        configObject temp = new configObject("firstStartup", "main", true);
        firstStartup = temp.getBooleanValue();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> temp.setBooleanValue(false)));
    }

}