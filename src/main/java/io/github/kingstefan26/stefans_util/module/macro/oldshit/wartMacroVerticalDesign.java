package io.github.kingstefan26.stefans_util.module.macro.oldshit;

import io.github.kingstefan26.stefans_util.Main;
import io.github.kingstefan26.stefans_util.core.module.ModuleManager;
import io.github.kingstefan26.stefans_util.core.module.moduleDecorators.impl.keyBindDecorator;
import io.github.kingstefan26.stefans_util.core.module.moduleframes.BasicModule;
import io.github.kingstefan26.stefans_util.core.setting.impl.CheckSetting;
import io.github.kingstefan26.stefans_util.core.setting.impl.SliderNoDecimalSetting;
import io.github.kingstefan26.stefans_util.module.macro.util.cropType;
import io.github.kingstefan26.stefans_util.module.macro.util.macroStages;
import io.github.kingstefan26.stefans_util.module.render.LastLeftOff;
import io.github.kingstefan26.stefans_util.service.impl.WorldInfoService;
import io.github.kingstefan26.stefans_util.service.impl.chatService;
import io.github.kingstefan26.stefans_util.util.StefanutilEvents;
import io.github.kingstefan26.stefans_util.util.renderUtil.drawCenterString;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;


public class wartMacroVerticalDesign extends BasicModule {

    private io.github.kingstefan26.stefans_util.module.macro.util.macroMenu macroMenu;

    private boolean isMacroingReady;
    private int wantedPitch;
    private int wantedYaw;
    private boolean perfectHeadRotation;
    private boolean experimentalGui;

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


	public wartMacroVerticalDesign() {
        super("wart macro my design", "my design wart macro!", ModuleManager.Category.MACRO, new keyBindDecorator("wartMacroverticalDesing"));
	}

	@Override
	public void onLoad() {
		new SliderNoDecimalSetting("yaw", this, 90, 1, 90, (newvalue)->{
			this.wantedYaw = Math.toIntExact(Math.round(newvalue));
		});

		new SliderNoDecimalSetting("pitch", this, 9, 0, 90, (newvalue)->{
            this.wantedPitch = Math.toIntExact(Math.round(newvalue));
		});

		new CheckSetting("perfect head rotation", this, false, (newvalue)->{
			experimentalGui = (boolean) newvalue;
		});

		new CheckSetting("experimental gui", this, false, (newvalue)->{
			perfectHeadRotation = (boolean) newvalue;
		});

		super.onLoad();
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
                MinecraftForge.EVENT_BUS.post(new StefanutilEvents.playerFallEvent());
			}
		}
	}

	private void notReadyOnTickRoutine() {
        //notify the user what we want them to do
        drawCenterString.GuiNotif(mc, "macro will start when you lock your head position on the right angle");
        drawCenterString.drawCenterStringOnScreenLittleToDown(mc,
                "press key " + Keyboard.getKeyName(this.getLocalDecoratorManager().keyBindDecorator.keybind.getKeyCode()) + " to stop", "ff002f");

        //update player pitch and yaw with up to date info
        playerYaw = Math.round(mc.thePlayer.rotationYaw);
        playerPitch = Math.round(mc.thePlayer.rotationPitch);

        isMacroingReady = playerYaw % wantedYaw == 0 && playerPitch == wantedPitch;

        if (isMacroingReady) {
            LastLeftOff.nullLastLeftOff();
            if (perfectHeadRotation) {
				mc.thePlayer.rotationYaw = wantedYaw;
				mc.thePlayer.rotationPitch = wantedPitch;
			}
			if(experimentalGui){
				guiCloseGrace = false;
				mc.displayGuiScreen(macroMenu);
			}
		}
	}

	private void readyOnTickRoutine() {
		//locks mouse and keyboard
		Mouse.getDX();
		Mouse.getDY();
		mc.mouseHelper.deltaX = mc.mouseHelper.deltaY = 0;


		KeyBinding.setKeyBindState(mc.gameSettings.keyBindAttack.getKeyCode(), true);

        if (Main.isDebug()) Mouse.setGrabbed(false);

		//the text :)
		drawCenterString.GuiNotif(mc, "macroing ur life away!");

		//show the release message
		if(experimentalGui){
			drawCenterString.drawCenterStringOnScreenLittleToDown(mc,
					"press key esc or tha button to stop",
					"ff002f");
		}else{
            drawCenterString.drawCenterStringOnScreenLittleToDown(mc, "press key "
                    + Keyboard.getKeyName(this.getLocalDecoratorManager().keyBindDecorator.keybind.getKeyCode()) +
                    " to stop", "ff002f");
		}


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
        if (this.getLocalDecoratorManager().keyBindDecorator.keybind.getKeyCode() == 0) {
            chatService.queueClientChatMessage("please set a keybinding!", chatService.chatEnum.PREFIX);
            this.setToggled(false);
            return;
        }
        if (!Main.isDebug()) {
            if (!WorldInfoService.isOnPrivateIsland()) {
                chatService.queueClientChatMessage("please join a your island!", chatService.chatEnum.PREFIX);
                this.setToggled(false);
                return;
            }
        }
        if (LastLeftOff.getLastLeftOff().getLastleftoffObject() != null) {
            macroWalkStage = LastLeftOff.getLastLeftOff().getLastleftoffObject().getMacroStage();
        } else {
            macroWalkStage = macroStages.RIGHT;
        }

        fallCounter = 0;
        //reset the timer on enable
        this.playerSpeedCheckTimer = System.currentTimeMillis();
        this.YSpeedTimer = System.currentTimeMillis();

        if (experimentalGui) {

            guiCloseGrace = true;
        }

        mc.displayGuiScreen(null);
        chatService.queueClientChatMessage("enabled wart macro", chatService.chatEnum.PREFIX);

    }

	@Override
	public void onDisable() {
		super.onDisable();
		mc.displayGuiScreen(null);

        chatService.queueClientChatMessage("disabled wart macro", chatService.chatEnum.PREFIX);
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
		LastLeftOff.getLastLeftOff().registerLastLeftOff(new LastLeftOff.LastleftoffObject((float) mc.thePlayer.posX, (float) mc.thePlayer.posY, (float) mc.thePlayer.posZ,
				cropType.WART, macroWalkStage, System.currentTimeMillis()));
	}

    @SubscribeEvent
    public void onPlayerFallEvent(StefanutilEvents.playerFallEvent e) {
        if (isMacroingReady && !playerTeleported) {
            fallCounter++;
            chatService.queueClientChatMessage("fallen for the " + fallCounter + " time", chatService.chatEnum.PREFIX);
            playerFallen = true;
        }
    }


    @SubscribeEvent
    public void onPlayerTeleportEvent(StefanutilEvents.playerTeleportEvent event) {
        if (isMacroingReady) {
            chatService.queueClientChatMessage("teleport detected!", chatService.chatEnum.PREFIX);
            playerTeleported = true;
        }
    }

    /*
    if the opened gui is not the macro gui we quit
     */
    @SubscribeEvent
    public void onGuiOpen(GuiOpenEvent e) {
        if (experimentalGui) {
            if (!(e.gui instanceof io.github.kingstefan26.stefans_util.module.macro.util.macroMenu) && !guiCloseGrace) {
                this.toggle();
            }
		}else{
			e.setCanceled(true);
		}
	}

	/*
	unloads the mod on world change to avoid captcha being spammed
	 */
	@SubscribeEvent
	public void onUnloadWorld(WorldEvent.Unload event) {
		super.setToggled(false);
		chatService.queueClientChatMessage(this.getName() + " was unloaded because you switched worlds", chatService.chatEnum.PREFIX);
	}
}
