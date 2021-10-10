package io.github.kingstefan26.stefans_util;

import io.github.kingstefan26.stefans_util.core.commands.commandRegistry;
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
import net.minecraftforge.fml.common.ProgressManager;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Objects;

@Mod(modid = main.MODID, version = main.VERSION, clientSideOnly = true, acceptedMinecraftVersions = "1.8.9")
public class main {
    public static final String MODID = "stefan_util";
    public static final String VERSION = "0.2.8-ALPHA";
    public static boolean debug = false;

    @Mod.Instance
    public static main instance;

    @Nonnull
    private ShaderResourcePack dummyPack = new ShaderResourcePack();

    public static final Logger logger = LogManager.getLogger("main-kokomod");

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
        ProgressManager.ProgressBar progressBar = ProgressManager.push("kokomod", 2);
        long start = System.currentTimeMillis();
        if(debug) logger.info("started repo refresh");

        kokoMod.refreshRepo(logger, progressBar);
        long stop = System.currentTimeMillis();
        if(debug) logger.info("finished repo refresh in " + (stop - start) + "ms");

        while (progressBar.getStep() < progressBar.getSteps())
            progressBar.step("random-"+progressBar.getStep());
        ProgressManager.pop(progressBar);

        for(CommandBase a : new commandRegistry().simpleCommands){
            ClientCommandHandler.instance.registerCommand(a);
        }

        // Add our dummy resourcepack
        ((SimpleReloadableResourceManager)Minecraft.getMinecraft().getResourceManager()).registerReloadListener(dummyPack);
    }

    @EventHandler
    public static void Init(FMLPreInitializationEvent event) {

//        Thread t = new Thread(() -> {
//        });
//        t.setName("REPO-REFRESH");
//        t.start();
        updateWidowTitle.updateTitle("Kokoclient V69.420");
        kokoMod.getkokoMod().init();
    }
}