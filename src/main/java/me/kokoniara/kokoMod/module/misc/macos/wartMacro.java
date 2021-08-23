package me.kokoniara.kokoMod.module.misc.macos;

import me.kokoniara.kokoMod.kokoMod;
import me.kokoniara.kokoMod.module.Category;
import me.kokoniara.kokoMod.module.Module;
import me.kokoniara.kokoMod.settings.Setting;
import me.kokoniara.kokoMod.util.forgeEventClasses.playerTeleported;
import me.kokoniara.kokoMod.util.isOnUpdater;
import me.kokoniara.kokoMod.util.renderUtil.drawCenterString;
import me.kokoniara.kokoMod.util.sendChatMessage;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.util.ArrayList;

import static me.kokoniara.kokoMod.module.misc.macos.macroStages.*;

public class wartMacro extends Module {

    private final drawCenterString drawCenterStringOBJ = drawCenterString.getdrawCenterString();
    private EntityPlayerSP player;

    private boolean ismacroingReady;
    private int wantedPitch = 9;
    private int wantedYaw = 90;


    private macroStages macroWalkStage = DEFAULT;
    private macroStages lastTurnOffStage = DEFAULT;
    ArrayList macroWalkHistory = new ArrayList();

    private boolean playerTeleported;
    private long playerSpeedCheckTimer;
    private long topWalkTimer;

    private int playerYaw;
    private int playerPitch;
    private double playerSpeed;


    private final boolean debug = true;

    public wartMacro() {
        super("wart macro", "macros wart!", Category.MISC, true, " wart macro enabed", " wart macro disabled");
        kokoMod.instance.settingsManager.rSetting(new Setting("yaw", this, 90, 0, 90, true));
        kokoMod.instance.settingsManager.rSetting(new Setting("pitch", this, 9, 0, 90, true));
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.RenderTickEvent event) {
        if (mc == null || mc.theWorld == null || mc.thePlayer == null) return;

        //mc.thePlayer.closeScreen();
        /*
        prevents you form using the mod without a keybind
         */
        if (this.getKey() == 0) {
            sendChatMessage.sendClientMessage("please set a keybind!", true);
            this.toggle();
            return;
        }


        player = mc.thePlayer; // re making the player cuz we accessing fields

        //get player speed to see if we reached the end of the farm already
        playerSpeed = player.getDistance(player.lastTickPosX, player.lastTickPosY, player.lastTickPosZ);

        //that runs if we just stared the module, checks if we can start moving and breaking
        if (!ismacroingReady) {
            //notify the user what we want them to do
            drawCenterStringOBJ.GuiNotif(mc, "macro will start when you lock your head postion on the right angle");

            //update player pitch and yaw with up to date info
            playerYaw = Math.round(mc.thePlayer.rotationYaw);
            playerPitch = Math.round(mc.thePlayer.rotationPitch);

            //if the yaw if at a 45o angle and pitch is 0, so we can start macroing
            boolean temp = playerYaw % wantedYaw == 0 && playerPitch == wantedPitch;
            //assign the results to ismacroingready, so we can start
            ismacroingReady = temp;


            if (lastTurnOffStage == DEFAULT && macroWalkStage == DEFAULT) {
                macroWalkStage = LEFT;
                macroWalkHistory.add(DEFAULT);
            } else if (lastTurnOffStage != DEFAULT) {
                macroWalkStage = lastTurnOffStage;
            }


        } else {
            //locks mouse and keyboard
            Mouse.getDX();
            Mouse.getDY();
            mc.mouseHelper.deltaX = mc.mouseHelper.deltaY = 0;


            //the text :)
            drawCenterStringOBJ.GuiNotif(mc, "macroing ur life away!");

            //show the release message
            ScaledResolution scaled = new ScaledResolution(mc);
            int width = scaled.getScaledWidth();
            int height = scaled.getScaledHeight();
            drawCenterStringOBJ.drawCenterStringOnScreenLittleToDown(mc, "press key " + Keyboard.getKeyName(this.getKeyBindingObj().getKeyCode()) + " to stop", "ff002f");

            //just holds the attack key down
            KeyBinding.setKeyBindState(mc.gameSettings.keyBindAttack.getKeyCode(), true);

            //checks the speed every half second so we dont spam the variable
            if (System.currentTimeMillis() - this.playerSpeedCheckTimer > 500) {
                //reset the timer
                this.playerSpeedCheckTimer = System.currentTimeMillis();

                if (!playerTeleported) {
                    macroStages t = nextWalkStage(macroWalkStage, playerSpeed);
                    if (t != null) {
                        macroWalkStage = t;
                    }
                } else {
                    macroWalkStage = LEFT;
                    playerTeleported = false;
                    macroWalkHistory.clear();
                    macroWalkHistory.add(DEFAULT);

                }
                macroWalk(macroWalkStage);

            }
        }
    }

    private macroStages nextWalkStage(macroStages currentWalkStage, double PlayerSpeed) {
        macroStages temp = null;
        if (PlayerSpeed <= 0.1F) {
            //rules to what happen if the player stopped moving
            switch (currentWalkStage) {
                case RIGHT:
                    macroWalkHistory.add(RIGHT);
                    temp = TOP;
                    topWalkTimer = System.currentTimeMillis();
                    break;
                case LEFT:
                    macroWalkHistory.add(LEFT);
                    topWalkTimer = System.currentTimeMillis();
                    temp = TOP;
                    break;
                case TOP:
                    if (System.currentTimeMillis() - this.topWalkTimer > 200) {
                        if (macroWalkHistory != null && !macroWalkHistory.isEmpty()) {
                            if (RIGHT.equals(macroWalkHistory.get(macroWalkHistory.size() - 1))) {
                                macroWalkStage = LEFT;
                            }
                            if (LEFT.equals(macroWalkHistory.get(macroWalkHistory.size() - 1))) {
                                macroWalkStage = RIGHT;
                            }
                            macroWalkHistory.clear();
                            macroWalkHistory.add(DEFAULT);
                        } else {
                            macroWalkHistory.add(LEFT);
                            macroWalkStage = LEFT;
                        }
                    }
                    break;
            }
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
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindBack.getKeyCode(), false);
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindRight.getKeyCode(), false);
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindLeft.getKeyCode(), true);
                break;
            case RIGHT:
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.getKeyCode(), false);
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindBack.getKeyCode(), false);
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindRight.getKeyCode(), true);
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindLeft.getKeyCode(), false);
                break;
            case TOP:
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.getKeyCode(), true);
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindBack.getKeyCode(), false);
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindRight.getKeyCode(), false);
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindLeft.getKeyCode(), false);
                break;
            case BOTTOM:
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.getKeyCode(), false);
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindBack.getKeyCode(), true);
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindRight.getKeyCode(), false);
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindLeft.getKeyCode(), false);
                break;
        }
    }

    @Override
    public void onEnable() {
        super.onEnable();
        if (!debug) {
            if (!isOnUpdater.isOnPrivateIsland()) {
                sendChatMessage.sendClientMessage("please join a your island!", true);
                this.toggle();
                return;
            }
        }

        this.wantedPitch = kokoMod.instance.settingsManager.getSettingByName("pitch", this).getValInt();
        this.wantedYaw = kokoMod.instance.settingsManager.getSettingByName("yaw", this).getValInt();
        //reset the timer on enable
        this.playerSpeedCheckTimer = System.currentTimeMillis();

        mc.displayGuiScreen(null);
    }

    @Override
    public void onDisable() {
        super.onDisable();

        lastTurnOffStage = macroWalkStage;
        /*
        reset every variable & unpress every key on disable
         */
        macroWalkStage = DEFAULT;
        playerTeleported = false;
        ismacroingReady = false;
        macroWalkStage = macroStages.DEFAULT;
        macroWalkHistory.clear();
        playerYaw = 0;
        playerPitch = 0;
        playerSpeed = 0;
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.getKeyCode(), false);
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindBack.getKeyCode(), false);
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindRight.getKeyCode(), false);
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindLeft.getKeyCode(), false);
    }

    @SubscribeEvent
    public void onPlayerTeleportEvent(playerTeleported event) {
        if (ismacroingReady) {
            sendChatMessage.sendClientMessage(" teleport detected!", true);
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
        sendChatMessage.sendClientMessage(" wart macro was unloaded because you switched worlds", true);
    }
}
