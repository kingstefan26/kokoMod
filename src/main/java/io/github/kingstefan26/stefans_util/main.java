package io.github.kingstefan26.stefans_util;

import io.github.kingstefan26.stefans_util.core.commands.commandIndex;
import io.github.kingstefan26.stefans_util.core.kokoMod;
import io.github.kingstefan26.stefans_util.util.ShaderResourcePack;
import io.github.kingstefan26.stefans_util.util.renderUtil.updateWidowTitle;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.client.resources.SimpleReloadableResourceManager;
import net.minecraft.command.CommandBase;
import net.minecraft.init.Items;
import net.minecraft.stats.Achievement;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Objects;

@Mod(modid = main.MODID, version = main.VERSION)
public class main {
    public static final String MODID = "stefan_util";
    public static final String VERSION = "0.2.8-ALPHA";
    public static boolean debug = false;

    @Mod.Instance
    public static main instance;

    @Nonnull
    private ShaderResourcePack dummyPack = new ShaderResourcePack();

    public static final Logger logger = LogManager.getLogger();
    public static Achievement cockbone;

    @SuppressWarnings("unchecked")
    public main(){
        String a = System.getProperty("kokomod.debug","false");
        if(Objects.equals(a, "true")) {
            debug = true;
        }
        ((List<IResourcePack>) ReflectionHelper.getPrivateValue(Minecraft.class, Minecraft.getMinecraft(), "field_110449_ao", "defaultResourcePacks")).add(dummyPack);
    }

    @EventHandler
    public void preInit(final FMLPreInitializationEvent event) {
        for(CommandBase c : commandIndex.getCommandIndex().getCommands()){
            ClientCommandHandler.instance.registerCommand(c);
        }
        cockbone = (new Achievement("a", "cock with no cock bone", 0, 0, Items.bone, null)).registerStat();

        // Add our dummy resourcepack
        ((SimpleReloadableResourceManager)Minecraft.getMinecraft().getResourceManager()).registerReloadListener(dummyPack);
    }

    @EventHandler
    public static void Init(FMLPreInitializationEvent event) {
        updateWidowTitle.updateTitle("Kokoclient V69.420");
        kokoMod.getkokoMod().init();
    }
}