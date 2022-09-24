package io.github.kingstefan26.stefans_util.core;

import io.github.kingstefan26.stefans_util.Main;
import io.github.kingstefan26.stefans_util.core.clickGui.ClickGui;
import io.github.kingstefan26.stefans_util.core.commands.commandRegistry;
import io.github.kingstefan26.stefans_util.core.config.ConfigManager;
import io.github.kingstefan26.stefans_util.core.fileCacheing.cacheManager;
import io.github.kingstefan26.stefans_util.core.module.ModuleManager;
import io.github.kingstefan26.stefans_util.core.onlineFeatures.auth.authmenager;
import io.github.kingstefan26.stefans_util.core.onlineFeatures.dynamicModules.webModuleMenager;
import io.github.kingstefan26.stefans_util.core.onlineFeatures.repo.mainRepoManager;
import io.github.kingstefan26.stefans_util.core.setting.SettingsCore;
import io.github.kingstefan26.stefans_util.service.impl.chatService;
import io.github.kingstefan26.stefans_util.service.serviceMenager;
import io.github.kingstefan26.stefans_util.util.StefanutilEvents;
import io.github.kingstefan26.stefans_util.util.handelers.PacketHandler;
import io.github.kingstefan26.stefans_util.util.renderUtil.updateWidowTitle;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiErrorScreen;
import net.minecraft.command.CommandBase;
import net.minecraft.event.ClickEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This Class is the entry point to mod stuff
 */
public class Kokomod {
    public static boolean connectedToKokoCLoud = false;
    static boolean isAllowedToPlay = true;
    static Kokomod instance;
    Logger logger = LogManager.getLogger("kokoMod-Main");
    boolean firstStartup = true;
    boolean lockfirstStartup = false;

    public static Kokomod getkokoMod() {
        if (instance == null) instance = new Kokomod();
        return instance;
    }

    public static boolean isConnectedToKokoCLoud() {
        return connectedToKokoCLoud;
    }

    public static void setConnectedToKokoCLoud(boolean connectedToKokoCLoud) {
        Kokomod.connectedToKokoCLoud = connectedToKokoCLoud;
    }

    public void init() {
        MinecraftForge.EVENT_BUS.register(this);

        for (CommandBase a : commandRegistry.simpleCommands) {
            ClientCommandHandler.instance.registerCommand(a);
        }


        ConfigManager.getInstance();
        cacheManager.getInstance().init();

        ModuleManager.getInstance().initModuleManager();

        (new serviceMenager()).start();
//		mainRepoManager.getMainRepoManager().startup(globals.publicRepoURL);

        logger.info("starting webmodule manager");
        webModuleMenager.getInstance();

        logger.info("starting authmanager");
        authmenager.getInstance().start();


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
//		Class<?> caller = Reflection.getCallerClass();


        SettingsCore.getSettingsCore();

        ClickGui.getClickGui();
    }

    @SubscribeEvent
    public void onstefanutilsconnectedToKokoCloud(StefanutilEvents.connectedToKokoCloud event) {
        if (authmenager.getInstance().getCashedAuthObject() == null) return;
        if (authmenager.getInstance().getCashedAuthObject().status.equals("dev")) {
            Main.setDebug(true);
        }

        updateWidowTitle.updateTitle(
                "Kokoclient V69.420 | " + authmenager.getInstance().getCashedAuthObject().status);
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent ev) {
        try {
            if (ev.side == Side.SERVER) return;
            if (ev.phase != TickEvent.Phase.START) return;
            if (!isAllowedToPlay) {
                if (Minecraft.getMinecraft().currentScreen instanceof GuiErrorScreen) return;

                Minecraft.getMinecraft().displayGuiScreen(
                        new GuiErrorScreen("you fool are not allowed to use this mod.",
                                "smh") {
                            @Override
                            public void initGui() {
                                super.initGui();
                                this.buttonList.clear();
                                this.buttonList.add(
                                        new GuiButton(0,
                                                width / 2 - 50,
                                                height - 50,
                                                100,
                                                20,
                                                "close"));
                            }

                            @Override
                            protected void actionPerformed(GuiButton button) {
                                Runtime.getRuntime().halt(0);
                                //FMLCommonHandler.instance().exitJava(-1, true);
                            }
                        });
            }
        } catch (Exception ignored) {
            Runtime.getRuntime().halt(0);
            //FMLCommonHandler.instance().exitJava(-1, true);
        }
    }

    @SubscribeEvent
    public void onFirstWorldJoin(TickEvent.ClientTickEvent e) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc == null) return;
        if (mc.theWorld == null) return;
        if (mc.thePlayer == null) return;

        if (Main.isFirstStartup() && !lockfirstStartup) {


            String[] temp = {
                    "Hi its seems like its first time using kokoMod",
                    "so welcome...",
                    "you will quickly regret using this mod (｀ڼ´)",
                    "§4this is gonna be a fun time "
            };
            for (String t : temp) {
                chatService.queueCleanChatMessage(t);
            }
            lockfirstStartup = true;
        } else {
            if (firstStartup) {
                firstStartup = false;
                ChatComponentText b = new ChatComponentText("§bThis version of Cock Mod™ " + Globals.VERSION);
                b.getChatStyle().setChatClickEvent(
                        new ClickEvent(ClickEvent.Action.RUN_COMMAND, "KokoModPrivacyPolicy"));

                chatService.queueClientChatMessage(b, chatService.chatEnum.CHATCOMPONENT);


                ChatComponentText a = new ChatComponentText(
                        " does not contain stds nor aids! §oall rights"
                                + " reserved§o§r Session id steal-er privacy policy §nhere§n");
                a.getChatStyle().setChatClickEvent(
                        new ClickEvent(ClickEvent.Action.OPEN_URL, "https://youtu.be/dQw4w9WgXcQ"));
                chatService.queueClientChatMessage(a, chatService.chatEnum.CHATCOMPONENT);

                chatService.lockEnableMessages = false;
            }
        }
    }

    @SubscribeEvent
    public void onreporeload(mainRepoManager.repoReloadedEvent e) {
//		alowedUsers.clear();
//
//		JsonObject data = mainRepoManager.getMainRepoManager().getMainrepoobject().allowedUsers;
//
//		Set<Map.Entry<String, JsonElement>> entrySet = data.entrySet();
//		for(Map.Entry<String,JsonElement> entry : entrySet){
//			alowedUsers.put(entry.getKey(), entry.getValue().getAsString());
//		}
//		if(debug) logger.info("current user uuid: " + Minecraft.getMinecraft().getSession().getProfile().getId().toString().replace("-", ""));
//		isAllowedToPlay = alowedUsers.containsValue(Minecraft.getMinecraft().getSession().getProfile().getId().toString().replace("-", ""));
//		if(debug){
//			logger.info("Whitelisted users:");
//			alowedUsers.forEach((K, V)  -> logger.info(K + " uuid:" + V));
//		}
    }


    @SubscribeEvent
    public void onServerConnect(FMLNetworkEvent.ClientConnectedToServerEvent event) {
        event.manager.channel().pipeline().addBefore("packet_handler",
                "kingstefan26_packet_handler", new PacketHandler());
        logger.info("Added packet handler to channel pipeline.");
    }
}