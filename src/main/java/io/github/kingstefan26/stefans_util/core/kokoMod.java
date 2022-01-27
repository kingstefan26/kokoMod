package io.github.kingstefan26.stefans_util.core;

import io.github.kingstefan26.stefans_util.core.clickGui.ClickGui;
import io.github.kingstefan26.stefans_util.core.config.configMenager;
import io.github.kingstefan26.stefans_util.core.fileCacheing.cacheManager;
import io.github.kingstefan26.stefans_util.core.module.ModuleMenagers.moduleManager;
import io.github.kingstefan26.stefans_util.core.newConfig.configManagerz;
import io.github.kingstefan26.stefans_util.core.onlineFeatures.auth.authmenager;
import io.github.kingstefan26.stefans_util.core.onlineFeatures.dynamicModules.webModuleMenager;
import io.github.kingstefan26.stefans_util.core.onlineFeatures.repo.mainRepoManager;
import io.github.kingstefan26.stefans_util.core.setting.general.SettingsCore;
import io.github.kingstefan26.stefans_util.main;
import io.github.kingstefan26.stefans_util.service.impl.chatService;
import io.github.kingstefan26.stefans_util.service.serviceMenager;
import io.github.kingstefan26.stefans_util.util.handelers.PacketHandler;
import io.github.kingstefan26.stefans_util.util.renderUtil.updateWidowTitle;
import io.github.kingstefan26.stefans_util.util.stefan_utilEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiErrorScreen;
import net.minecraft.event.ClickEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class kokoMod {
	Logger logger = LogManager.getLogger("kokoMod-Main");
	public static boolean isAllowedToPlay = true;

	public static kokoMod instance;
    public static kokoMod getkokoMod(){
    	if(instance == null) instance = new kokoMod();
    	return instance;
    }

	public void init() {
		MinecraftForge.EVENT_BUS.register(this);
		configManagerz.getInstance();
		cacheManager.getInstance().init();

		moduleManager.getModuleManager();
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



		configMenager.getConfigManager();
		SettingsCore.getSettingsCore();

		ClickGui.getClickGui();
	}


	@SubscribeEvent
	public void onstefan_utilsconnectedToKokoCloud(stefan_utilEvents.connectedToKokoCloud event) {
		if (authmenager.getInstance().getCashedAuthObject() == null) return;
		if (authmenager.getInstance().getCashedAuthObject().status.equals("dev")) {
			main.debug = true;
		}

		updateWidowTitle.updateTitle("Kokoclient V69.420 | " + authmenager.getInstance().getCashedAuthObject().status);
	}

	@SubscribeEvent
	public void onTick(TickEvent.ClientTickEvent ev) throws Throwable {
		try {
			if (ev.side == Side.SERVER) return;
			if (ev.phase == TickEvent.Phase.START) {
				if (!isAllowedToPlay) {
					if (Minecraft.getMinecraft().currentScreen instanceof GuiErrorScreen) return;

					Minecraft.getMinecraft().displayGuiScreen(new GuiErrorScreen("you fool are not allowed to use this mod.",
							"smh") {
                        @Override
                        public void initGui() {
                            super.initGui();
                            this.buttonList.clear();
                            this.buttonList.add(new GuiButton(0, width / 2 - 50, height - 50, 100, 20, "close"));
                        }

                        @Override
                        protected void actionPerformed(GuiButton button){
							Runtime.getRuntime().halt(0);
                            //FMLCommonHandler.instance().exitJava(-1, true);
                        }
                    });
                }
			}
		} catch (Exception ignored){
			Runtime.getRuntime().halt(0);
			//FMLCommonHandler.instance().exitJava(-1, true);
		}
	}

	public boolean firstStartup = true;

	boolean lockfirstStartup = false;

    @SubscribeEvent
    public void onFirstWorldJoin(TickEvent.ClientTickEvent e){
		if(main.firstStartup && !lockfirstStartup){
			Minecraft mc = Minecraft.getMinecraft();
			if(mc != null){
				if(mc.thePlayer != null || mc.theWorld != null){
					String[] temp = {
							"Hi its seems like its first time using kokoMod",
							"so welcome...",
							"you will quickly regret using this mod (｀ڼ´)",
							"§4this is gonna be a fun time "
					};
					for(String t : temp){
						chatService.queueCleanChatMessage(t);
					}
					lockfirstStartup = true;
				}
			}
		}else{
			if(firstStartup){
				Minecraft mc = Minecraft.getMinecraft();
				if(mc != null){
					if(mc.thePlayer != null || mc.theWorld != null){
						firstStartup = false;
						ChatComponentText b = new ChatComponentText("§bThis version of Cock Mod™ "
								+ globals.VERSION);
						b.getChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "KokoModPrivacyPolicy"));

						chatService.queueClientChatMessage(b, chatService.chatEnum.CHATCOMPONENT);

						ChatComponentText a = new ChatComponentText(
								" does not contain stds nor aids! §oall rights reserved§o§r Session id steal-er privacy policy §nhere§n");
						a.getChatStyle().setChatClickEvent(new ClickEvent(net.minecraft.event.ClickEvent.Action.OPEN_URL,
								"https://youtu.be/dQw4w9WgXcQ"));
						chatService.queueClientChatMessage(a, chatService.chatEnum.CHATCOMPONENT);

						chatService.lockEnableMessages = false;
					}
				}
			}
		}
    }

	@SubscribeEvent
	public void onreporeload(mainRepoManager.repoReloadedEvent e){
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
		event.manager.channel().pipeline().addBefore("packet_handler", "kingstefan26_packet_handler", new PacketHandler());
		System.out.println("Added packet handler to channel pipeline.");
	}
}