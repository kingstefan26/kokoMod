package io.github.kingstefan26.kokomod;

import io.github.kingstefan26.kokomod.core.commands.commandIndex;
import io.github.kingstefan26.kokomod.core.kokoMod;
import io.github.kingstefan26.kokomod.util.renderUtil.updateWidowTitle;

import net.minecraft.command.CommandBase;

import net.minecraft.init.Items;
import net.minecraft.stats.Achievement;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Objects;

@Mod(modid = main.MODID, version = main.VERSION)
public class main {
    public static final String MODID = "kokoMod";
    public static final String VERSION = "0.2.8-ALPHA";
    public static boolean debug = false;

    public static final Logger logger = LogManager.getLogger();
    public static Achievement cockbone;

    public main(){
        String a = System.getProperty("kokomod.debug","false");
        if(Objects.equals(a, "true")) {
            debug = true;
        }
    }

    @EventHandler
    public void preInit(final FMLPreInitializationEvent event) {
        for(CommandBase c : commandIndex.getCommandIndex().getCommands()){
            ClientCommandHandler.instance.registerCommand(c);
        }
        cockbone = (new Achievement("a", "cock with no cock bone", 0, 0, Items.bone, (Achievement)null)).registerStat();
    }

    @EventHandler
    public static void Init(FMLPreInitializationEvent event) {
        updateWidowTitle.updateTitle("Kokoclient V69.420");
        kokoMod.getkokoMod().init();
    }
}