package io.github.kingstefan26.stefans_util.module.macro.wart;

import io.github.kingstefan26.stefans_util.core.module.ModuleManager;
import io.github.kingstefan26.stefans_util.core.module.Module;
import io.github.kingstefan26.stefans_util.core.setting.Setting;
import io.github.kingstefan26.stefans_util.core.setting.SettingsManager;
import io.github.kingstefan26.stefans_util.module.macro.macroUtil.macroStages;
import io.github.kingstefan26.stefans_util.module.util.SBinfo;
import io.github.kingstefan26.stefans_util.module.util.chat;
import io.github.kingstefan26.stefans_util.util.stefan_utilEvents;
import io.github.kingstefan26.stefans_util.util.renderUtil.drawCenterString;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import static io.github.kingstefan26.stefans_util.module.macro.macroUtil.macroStages.*;

public class wartMacro extends Module {

    private EntityPlayerSP player;

    private boolean ismacroingReady;
    private int wantedPitch = 9;
    private int wantedYaw = 90;


    private macroStages macroWalkStage = LEFT;
    macroStages[] macroWalkHistory = {DEFAULT, DEFAULT};

    private boolean playerTeleported;
    private long playerSpeedCheckTimer;
    private long topWalkTimer;

    private int playerYaw;
    private int playerPitch;
    private double playerSpeed;


    private final boolean debug = false;

    public wartMacro() {
        super("wart macro", "macros wart!", ModuleManager.Category.MACRO, true);
        SettingsManager.getSettingsManager().rSetting(new Setting("yaw", this, 90, 0, 90, true));
        SettingsManager.getSettingsManager().rSetting(new Setting("pitch", this, 9, 0, 90, true));
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.RenderTickEvent event) {
        if (mc == null || mc.theWorld == null || mc.thePlayer == null) return;

        player = mc.thePlayer; // re making the player cuz we accessing fields

        //get player speed to see if we reached the end of the farm already
        playerSpeed = player.getDistance(player.lastTickPosX, player.lastTickPosY, player.lastTickPosZ);

        //that runs if we just stared the module, checks if we can start moving and breaking
        if (!ismacroingReady) {
            //notify the user what we want them to do
            drawCenterString.GuiNotif(mc, "macro will start when you lock your head postion on the right angle");

            //update player pitch and yaw with up to date info
            playerYaw = Math.round(mc.thePlayer.rotationYaw);
            playerPitch = Math.round(mc.thePlayer.rotationPitch);

            //if the yaw if at a 45o angle and pitch is 0, so we can start macroing

            ismacroingReady = playerYaw % wantedYaw == 0 && playerPitch == wantedPitch;

        }
        if (ismacroingReady) {
            //locks mouse and keyboard
            Mouse.getDX();
            Mouse.getDY();
            mc.mouseHelper.deltaX = mc.mouseHelper.deltaY = 0;

            //Mouse.setGrabbed(false);

            //the text :)
            drawCenterString.GuiNotif(mc, "macroing ur life away!");

            //show the release message
            ScaledResolution scaled = new ScaledResolution(mc);
            int width = scaled.getScaledWidth();
            int height = scaled.getScaledHeight();
            drawCenterString.drawCenterStringOnScreenLittleToDown(mc, "press key " + Keyboard.getKeyName(this.getKeyBindingObj().getKeyCode()) + " to stop", "ff002f");

            //just holds the attack key down
            KeyBinding.setKeyBindState(mc.gameSettings.keyBindAttack.getKeyCode(), true);

            //checks the speed every half second so we dont spam the variable
            if (System.currentTimeMillis() - this.playerSpeedCheckTimer > 500) {
                //reset the timer
                this.playerSpeedCheckTimer = System.currentTimeMillis();

                if (playerTeleported) {
                    macroWalkStage = LEFT;
                    macroWalkHistory[0] = LEFT;
                    playerTeleported = false;

                } else {
                    if (playerSpeed <= 0.1F) {
                        macroStages t = nextWalkStage(macroWalkStage);
                        if (t == null) {
                            return;
                        }
                        macroWalkStage = t ;
                    }
                }

            }
            macroWalk(macroWalkStage);
        }
    }

    private macroStages nextWalkStage(macroStages currentWalkStage) {
        macroStages temp = null;
        //rules to what happen if the player stopped moving
        switch (currentWalkStage) {
            case RIGHT:
                macroWalkHistory[0] = RIGHT;
                temp = TOP;
                topWalkTimer = System.currentTimeMillis();
                break;
            case LEFT:
                macroWalkHistory[0] = LEFT;
                topWalkTimer = System.currentTimeMillis();
                temp = TOP;
                break;
            case TOP:
                if (RIGHT.equals(macroWalkHistory[0])) {
                    temp = LEFT;
                } else if (LEFT.equals(macroWalkHistory[0])) {
                    temp = RIGHT;
                } else {
                    macroWalkHistory[0] = TOP;
                }

                break;
        }
        return temp;
    }

    /*
    clicks and releases buttons based on input
     */
    private void macroWalk(macroStages m) {
        switch (m) {
            case LEFT:
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.getKeyCode(), false);
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindLeft.getKeyCode(), true);
                break;
            case RIGHT:
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.getKeyCode(), false);
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindRight.getKeyCode(), true);
                break;
            case TOP:
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.getKeyCode(), true);
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindRight.getKeyCode(), false);
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindLeft.getKeyCode(), false);
                break;
        }
    }

    @Override
    public void onEnable() {
        super.onEnable();
        if (this.getKey() == 0) {
            chat.queueClientChatMessage("please set a keybind!", chat.chatEnum.CHATPREFIX);
            this.setToggled(false);
            return;
        }
        if (!debug) {
            if (!SBinfo.isOnPrivateIsland()) {
                chat.queueClientChatMessage("please join a your island!", chat.chatEnum.CHATPREFIX);
                this.setToggled(false);
                return;
            }
        }

        chat.queueClientChatMessage(" enabled wart macro", chat.chatEnum.CHATPREFIX);


        this.wantedPitch = SettingsManager.getSettingsManager().getSettingByName("pitch", this).getValInt();
        this.wantedYaw = SettingsManager.getSettingsManager().getSettingByName("yaw", this).getValInt();
        //reset the timer on enable
        this.playerSpeedCheckTimer = System.currentTimeMillis();

        mc.displayGuiScreen(null);
    }

    @Override
    public void onDisable() {
        super.onDisable();

        chat.queueClientChatMessage("disabled wart macro", chat.chatEnum.CHATPREFIX);
        /*
        reset every variable & unpress every key on disable
         */
        playerTeleported = false;
        ismacroingReady = false;

        playerYaw = 0;
        playerPitch = 0;
        playerSpeed = 0;

        KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.getKeyCode(), false);
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindBack.getKeyCode(), false);
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindRight.getKeyCode(), false);
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindLeft.getKeyCode(), false);
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindAttack.getKeyCode(), false);
    }

    @SubscribeEvent
    public void onPlayerTeleportEvent(stefan_utilEvents.playerTeleportEvent event) {
        if (ismacroingReady) {
            chat.queueClientChatMessage("teleport detected!", chat.chatEnum.CHATPREFIX);
            playerTeleported = true;
        }
    }

    /*
    if the macro is running prevent any gui from being open
     */
    @SubscribeEvent
    public void onGuiOpen(GuiOpenEvent e) {
        if (macroWalkStage != macroStages.DEFAULT) {
            e.setCanceled(true);
        }
    }

    /*
    unloads the mod on world change to avoid captcha being spamed
     */
    @SubscribeEvent
    public void onUnloadWorld(WorldEvent.Unload event) {
        super.setToggled(false);
        chat.queueClientChatMessage("wart macro was unloaded because you switched worlds", chat.chatEnum.CHATPREFIX);
    }
}
