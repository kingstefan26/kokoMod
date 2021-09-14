package io.github.kingstefan26.kokomod.module.modules.macros.wart;

import io.github.kingstefan26.kokomod.main;
import io.github.kingstefan26.kokomod.module.moduleUtil.module.Category;
import io.github.kingstefan26.kokomod.module.moduleUtil.module.Module;
import io.github.kingstefan26.kokomod.module.moduleUtil.settings.Setting;
import io.github.kingstefan26.kokomod.module.moduleUtil.settings.SettingsManager;
import io.github.kingstefan26.kokomod.module.modules.macros.macroUtil.cropType;
import io.github.kingstefan26.kokomod.module.modules.macros.macroUtil.lastLeftOff.lastLeftOff;
import io.github.kingstefan26.kokomod.module.modules.macros.macroUtil.lastLeftOff.lastleftoffObject;
import io.github.kingstefan26.kokomod.module.modules.macros.macroUtil.macroMenu;
import io.github.kingstefan26.kokomod.module.modules.macros.macroUtil.macroStages;
import io.github.kingstefan26.kokomod.util.SBinfo.isOnUpdater;
import io.github.kingstefan26.kokomod.util.forgeEventClasses.playerFallEvent;
import io.github.kingstefan26.kokomod.util.forgeEventClasses.playerTeleportEvent;
import io.github.kingstefan26.kokomod.util.renderUtil.drawCenterString;
import io.github.kingstefan26.kokomod.util.sendChatMessage;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class wartMacroVerticalDesign extends Module {
	public static wartMacroVerticalDesign wartMacroVerticalDesign;

	public static wartMacroVerticalDesign getwartMacroVerticalDesign(){
		if(wartMacroVerticalDesign == null) wartMacroVerticalDesign = new wartMacroVerticalDesign();
		return wartMacroVerticalDesign;
	}

	private final drawCenterString drawCenterStringOBJ = drawCenterString.getdrawCenterString();
	private io.github.kingstefan26.kokomod.module.modules.macros.macroUtil.macroMenu macroMenu;

	private boolean isMacroingReady;
	private int wantedPitch;
	private int wantedYaw;
	private boolean perfectHeadRotation;

	private macroStages macroWalkStage = macroStages.RIGHT;

	private boolean playerTeleported;
	private boolean playerFallen;
	private boolean guiCloseGrace;


	private long playerSpeedCheckTimer;
	private long YSpeedTimer;


	private int playerYaw;
	private int playerPitch;
	private double playerSpeed;
	private int fallCounter;


	private wartMacroVerticalDesign() {
		super("wart macro my design", "my design wart macro!", Category.MACRO, true);
		SettingsManager.getSettingsManager().rSetting(new Setting("yaw", this, 90, 1, 90, true));
		SettingsManager.getSettingsManager().rSetting(new Setting("pitch", this, 9, 0, 90, true));
		SettingsManager.getSettingsManager().rSetting(new Setting("perfect head rotation", this, true));
	}

	@SubscribeEvent
	public void onClientTick(TickEvent.RenderTickEvent event) {
		if (mc == null || mc.theWorld == null || mc.thePlayer == null) return;

		//that runs if we just stared the module, checks if we can start moving and breaking

		OnTickRoutine();
		if (isMacroingReady) {
			readyOnTickRoutine();
		} else {
			notReadyOnTickRoutine();
		}
	}

	private void OnTickRoutine() {
		EntityPlayerSP player = mc.thePlayer; // re making the player cuz we accessing fields

		//get player speed to see if we reached the end of the farm already
		playerSpeed = player.getDistance(player.lastTickPosX, player.lastTickPosY, player.lastTickPosZ);

		if (System.currentTimeMillis() - YSpeedTimer > 500) {
			YSpeedTimer = System.currentTimeMillis();
			if (player.posY - player.lastTickPosY < 0) {
				MinecraftForge.EVENT_BUS.post(new playerFallEvent());
			}
		}
	}

	private void notReadyOnTickRoutine() {
		//notify the user what we want them to do
		drawCenterStringOBJ.GuiNotif(mc, "macro will start when you lock your head position on the right angle");
		drawCenterStringOBJ.drawCenterStringOnScreenLittleToDown(mc,
				"press key " + Keyboard.getKeyName(this.getKeyBindingObj().getKeyCode()) + " to stop", "ff002f");

		//update player pitch and yaw with up to date info
		playerYaw = Math.round(mc.thePlayer.rotationYaw);
		playerPitch = Math.round(mc.thePlayer.rotationPitch);

		isMacroingReady = playerYaw % wantedYaw == 0 && playerPitch == wantedPitch;

		if(isMacroingReady){
			lastLeftOff.nullLastLeftOff();
			if(perfectHeadRotation){
				mc.thePlayer.rotationYaw = wantedYaw;
				mc.thePlayer.rotationPitch = wantedPitch;
			}
			guiCloseGrace = false;
			mc.displayGuiScreen(macroMenu);
		}
	}

	private void readyOnTickRoutine() {
		//locks mouse and keyboard
		Mouse.getDX();
		Mouse.getDY();
		mc.mouseHelper.deltaX = mc.mouseHelper.deltaY = 0;


		KeyBinding.setKeyBindState(mc.gameSettings.keyBindAttack.getKeyCode(), true);

		if (main.debug) Mouse.setGrabbed(false);

		//the text :)
		drawCenterStringOBJ.GuiNotif(mc, "macroing ur life away!");

		//show the release message
		drawCenterStringOBJ.drawCenterStringOnScreenLittleToDown(mc,
				"press key esc or tha button to stop",
				"ff002f");


		//checks the speed every half second so we don't spam the variable
		if (System.currentTimeMillis() - this.playerSpeedCheckTimer > 500) {
			//reset the timer
			this.playerSpeedCheckTimer = System.currentTimeMillis();

			if (playerTeleported) {
				playerTeleported = false;
			}
			if (playerFallen) {
				playerFallen = false;
			}
			if (playerSpeed <= 0.1F) {
				if (macroWalkStage == macroStages.RIGHT) {
					macroWalkStage = macroStages.LEFT;
				}else if (macroWalkStage == macroStages.LEFT) {
					macroWalkStage = macroStages.RIGHT;
				}
			}

		}
		macroWalk(macroWalkStage);
	}


	/*
	clicks and releases buttons based on input
	 */
	private void macroWalk(macroStages m) {
		if (m == macroStages.DEFAULT) {
			return;
		}


		switch (m) {
			case LEFT:
				KeyBinding.setKeyBindState(mc.gameSettings.keyBindRight.getKeyCode(), false);
				KeyBinding.setKeyBindState(mc.gameSettings.keyBindLeft.getKeyCode(), true);
				break;
			case RIGHT:
				KeyBinding.setKeyBindState(mc.gameSettings.keyBindRight.getKeyCode(), true);
				KeyBinding.setKeyBindState(mc.gameSettings.keyBindLeft.getKeyCode(), false);
				break;
		}
	}



	@Override
	public void onEnable() {
		super.onEnable();
		if (this.getKey() == 0) {
			sendChatMessage.sendClientMessage("please set a keybinding!", true);
			this.setToggled(false);
			return;
		}
		if (!main.debug) {
			if (!isOnUpdater.isOnPrivateIsland()) {
				sendChatMessage.sendClientMessage("please join a your island!", true);
				this.setToggled(false);
				return;
			}
		}
		if(lastLeftOff.getLastleftoffObject() != null) {
			macroWalkStage = lastLeftOff.getLastleftoffObject().getMacroStage();
		}else{
			macroWalkStage = macroStages.RIGHT;
		}

		fallCounter = 0;
		this.wantedPitch = SettingsManager.getSettingsManager().getSettingByName("pitch", this).getValInt();
		this.wantedYaw = SettingsManager.getSettingsManager().getSettingByName("yaw", this).getValInt();
		this.perfectHeadRotation = SettingsManager.getSettingsManager().getSettingByName("perfect head rotation", this).getValBoolean();
		//reset the timer on enable
		this.playerSpeedCheckTimer = System.currentTimeMillis();
		this.YSpeedTimer = System.currentTimeMillis();

		if(this.macroMenu == null) macroMenu = new macroMenu(this);

		guiCloseGrace = true;
		mc.displayGuiScreen(null);
		sendChatMessage.sendClientMessage("enabled wart macro", true);

	}

	@Override
	public void onDisable() {
		super.onDisable();
		mc.displayGuiScreen(null);

		sendChatMessage.sendClientMessage("disabled wart macro", true);
        /*
        reset every variable & unpress every key on disable
         */
		//mc.setIngameFocus();
		playerTeleported = false;
		isMacroingReady = false;
		playerFallen = false;

		playerYaw = 0;
		playerPitch = 0;
		playerSpeed = 0;

		KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.getKeyCode(), false);
		KeyBinding.setKeyBindState(mc.gameSettings.keyBindBack.getKeyCode(), false);
		KeyBinding.setKeyBindState(mc.gameSettings.keyBindRight.getKeyCode(), false);
		KeyBinding.setKeyBindState(mc.gameSettings.keyBindLeft.getKeyCode(), false);
		KeyBinding.setKeyBindState(mc.gameSettings.keyBindAttack.getKeyCode(), false);
		lastLeftOff.registerLastLeftOff(new lastleftoffObject( (int)mc.thePlayer.posX , (int)mc.thePlayer.posY ,(int)mc.thePlayer.posZ ,
		cropType.WART, macroWalkStage));
	}

	@SubscribeEvent
	public void onPlayerFallEvent(playerFallEvent e) {
		if (isMacroingReady && !playerTeleported) {
			fallCounter++;
			sendChatMessage.sendClientMessage("fallen for the " + fallCounter + " time", true);
			playerFallen = true;
		}
	}


	@SubscribeEvent
	public void onPlayerTeleportEvent(playerTeleportEvent event) {
		if (isMacroingReady) {
			sendChatMessage.sendClientMessage("teleport detected!", true);
			playerTeleported = true;
		}
	}

	/*
	if the opened gui is not the macro gui we quit
	 */
	@SubscribeEvent
	public void onGuiOpen(GuiOpenEvent e) {
		if (!(e.gui instanceof io.github.kingstefan26.kokomod.module.modules.macros.macroUtil.macroMenu) && !guiCloseGrace) {
			this.toggle();
		}
	}

	/*
	unloads the mod on world change to avoid captcha being spammed
	 */
	@SubscribeEvent
	public void onUnloadWorld(WorldEvent.Unload event) {
		super.setToggled(false);
		sendChatMessage.sendClientMessage(this.getName() + " was unloaded because you switched worlds", true);
	}
}
