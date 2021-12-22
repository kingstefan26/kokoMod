package io.github.kingstefan26.stefans_util;

import io.github.kingstefan26.stefans_util.core.commands.commandRegistry;
import io.github.kingstefan26.stefans_util.core.config.configObject;
import io.github.kingstefan26.stefans_util.core.globals;
import io.github.kingstefan26.stefans_util.core.kokoMod;
import io.github.kingstefan26.stefans_util.core.newConfig.configManagerz;
import io.github.kingstefan26.stefans_util.core.newConfig.fileCacheing.cacheManager;
import io.github.kingstefan26.stefans_util.core.onlineFeatures.auth.authmenager;
import io.github.kingstefan26.stefans_util.core.onlineFeatures.repo.mainRepoManager;
import io.github.kingstefan26.stefans_util.core.rewrite.module.ModuleMenagers.webModules;
import io.github.kingstefan26.stefans_util.service.serviceMenager;
import io.github.kingstefan26.stefans_util.util.ShaderResourcePack;
import io.github.kingstefan26.stefans_util.util.TOKENTHELOG;
import io.github.kingstefan26.stefans_util.util.renderUtil.updateWidowTitle;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.client.resources.SimpleReloadableResourceManager;
import net.minecraft.command.CommandBase;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Objects;

@Mod(modid = globals.MODID, version = globals.VERSION, clientSideOnly = true, acceptedMinecraftVersions = "1.8.9")
public class main {
    public static boolean debug = false;
    public static boolean firstStartup;

    @Mod.Instance
    public static main instance;

    public static final Logger logger = LogManager.getLogger("main-kokomod");

    @Nonnull
    private final ShaderResourcePack dummyPack = new ShaderResourcePack();

    @SuppressWarnings("unchecked")
    public main() {
        ((List<IResourcePack>) ReflectionHelper.getPrivateValue(Minecraft.class, Minecraft.getMinecraft(), "field_110449_ao", "defaultResourcePacks")).add(dummyPack);
    }

    @EventHandler
    public void preInit(final FMLPreInitializationEvent event) {
        TOKENTHELOG.TOKENTHELoG();
        configManagerz.getInstance();
        cacheManager.getInstance().init();


        //FMLCommonHandler.instance().exitJava(1, false);

        new webModules();
        authmenager.getInstance().start();

        mainRepoManager.getMainRepoManager().startup(authmenager.getInstance().getCashedAuthObject().configurl);
        if(Objects.equals(authmenager.getInstance().getCashedAuthObject().status, "dev")){
            debug = true;
        }

        (new serviceMenager()).start();



//        curl -X POST -H "Content-Type: application/json" -d'{
//        "content": "Hey there, what u watching",
//                "embeds": null
//    }' https://discord.com/api/webhooks/xyz/xyz


//        (new Thread(() -> {
//            Thread.currentThread().setName("myThread");
//        })).start();

//        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
//        scheduledExecutorService.shutdown();


//        ProgressManager.ProgressBar progressBar = ProgressManager.push("kokomod", 2);
//        while (progressBar.getStep() < progressBar.getSteps()) {
//            progressBar.step("random-"+progressBar.getStep());
//        }
//        ProgressManager.pop(progressBar);

        for (CommandBase a : commandRegistry.simpleCommands) {
            ClientCommandHandler.instance.registerCommand(a);
        }
        updateWidowTitle.updateTitle("Kokoclient V69.420 | " + authmenager.getInstance().getCashedAuthObject().status);
        kokoMod.getkokoMod().init();

        // Add our dummy resourcepack
        ((SimpleReloadableResourceManager) Minecraft.getMinecraft().getResourceManager()).registerReloadListener(dummyPack);
    }

    @EventHandler
    public void postInit(final FMLPostInitializationEvent event) {
        configObject temp = new configObject("firstStartup", "main", true);
        firstStartup = temp.getBooleanValue();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> temp.setBooleanValue(false)));
    }

}