package io.github.kingstefan26.kokomod;

import io.github.kingstefan26.kokomod.core.commands.commandIndex;
import io.github.kingstefan26.kokomod.core.kokoMod;
import io.github.kingstefan26.kokomod.util.renderUtil.updateWidowTitle;
import net.minecraft.command.CommandBase;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = main.MODID, version = main.VERSION)
public class main {
    public static final String MODID = "kokoMod";
    public static final String VERSION = "0.2.8-ALPHA";
    public static final boolean debug = false;

    @EventHandler
    public void preInit(final FMLPreInitializationEvent event) {
        for(CommandBase c : commandIndex.getCommandIndex().getCommands()){
            ClientCommandHandler.instance.registerCommand(c);
        }
    }

    @EventHandler
    public static void Init(FMLPreInitializationEvent event) {
        updateWidowTitle.updateTitle("Kokoclient V69.420");
        kokoMod.getkokoMod().init();
    }
}