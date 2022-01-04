package io.github.kingstefan26.stefans_util;

import io.github.kingstefan26.stefans_util.core.commands.commandRegistry;
import io.github.kingstefan26.stefans_util.core.config.configObject;
import io.github.kingstefan26.stefans_util.core.globals;
import io.github.kingstefan26.stefans_util.core.kokoMod;
import io.github.kingstefan26.stefans_util.core.newConfig.configManagerz;
import io.github.kingstefan26.stefans_util.core.newConfig.fileCacheing.cacheManager;
import io.github.kingstefan26.stefans_util.core.onlineFeatures.auth.authmenager;
import io.github.kingstefan26.stefans_util.core.onlineFeatures.repo.mainRepoManager;
import io.github.kingstefan26.stefans_util.core.module.ModuleMenagers.moduleManager;
import io.github.kingstefan26.stefans_util.service.serviceMenager;
import io.github.kingstefan26.stefans_util.util.renderUtil.updateWidowTitle;
import net.minecraft.command.CommandBase;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Objects;

@Mod(modid = globals.MODID, version = globals.VERSION, clientSideOnly = true, acceptedMinecraftVersions = "1.8.9")
public class main {
    public static boolean debug = false;
    public static boolean firstStartup;

    @Mod.Instance
    public static main instance;

    public static final Logger logger = LogManager.getLogger("main-kokomod");


    public static boolean connectedToKokoCLoud = false;
    long recoocnectTimer;

    @EventHandler
    public void preInit(final FMLPreInitializationEvent event) {
        configManagerz.getInstance();
        cacheManager.getInstance().init();


        //FMLCommonHandler.instance().exitJava(1, false);

        authmenager.getInstance().start();
        if(!connectedToKokoCLoud) {
            recoocnectTimer = System.currentTimeMillis() + 5000;
            MinecraftForge.EVENT_BUS.register(this);
        }

        moduleManager.getModuleManager();

        mainRepoManager.getMainRepoManager().startup(authmenager.getInstance().getCashedAuthObject().configurl);
        if(Objects.equals(authmenager.getInstance().getCashedAuthObject().status, "dev")){
            debug = true;
        }

        (new serviceMenager()).start();




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

    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent e){
        if(!connectedToKokoCLoud){
            if(System.currentTimeMillis() >= recoocnectTimer){
                logger.info("trying to recconect to kokocloud");
                authmenager.getInstance().start();
                if(!connectedToKokoCLoud) {
                    recoocnectTimer = System.currentTimeMillis() + 5000;
                } else {
                    // if we connect, unregister this from the bus cuz doing an unnecessary bool check every tick is not what we need in life
                    MinecraftForge.EVENT_BUS.unregister(this);
                }
            }
        }
    }

    @EventHandler
    public void postInit(final FMLPostInitializationEvent event) {
        configObject temp = new configObject("firstStartup", "main", true);
        firstStartup = temp.getBooleanValue();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> temp.setBooleanValue(false)));
    }

}