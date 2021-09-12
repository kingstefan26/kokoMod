package me.kokoniara.kokoMod.module.modules.macros.wart;

import me.kokoniara.kokoMod.module.moduleUtil.module.Category;
import me.kokoniara.kokoMod.module.moduleUtil.module.Module;
import me.kokoniara.kokoMod.module.moduleUtil.settings.Setting;
import me.kokoniara.kokoMod.module.moduleUtil.settings.SettingsManager;
import me.kokoniara.kokoMod.module.modules.macros.macroUtil.macroStages;
import me.kokoniara.kokoMod.util.forgeEventClasses.playerFallEvent;
import me.kokoniara.kokoMod.util.forgeEventClasses.playerTeleportEvent;
import me.kokoniara.kokoMod.util.SBinfo.isOnUpdater;
import me.kokoniara.kokoMod.util.renderUtil.drawCenterString;
import me.kokoniara.kokoMod.util.sendChatMessage;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import static me.kokoniara.kokoMod.main.debug;
import static me.kokoniara.kokoMod.module.modules.macros.macroUtil.macroStages.*;

public class wartMacronoTppad extends Module {

    private final drawCenterString drawCenterStringOBJ = drawCenterString.getdrawCenterString();
    private EntityPlayerSP player;

    private boolean ismacroingReady;
    private int wantedPitch = 9;
    private int wantedYaw = 90;


    private macroStages macroWalkStage = RIGHT;
    macroStages[] macroWalkHistory = {DEFAULT, DEFAULT};

    private boolean playerTeleported;
    private boolean playerFallen;
    private long playerSpeedCheckTimer;
    private long YSpeedTimer;
    private long topWalkTimer;
    private long interpolateTimer;

    private float wantedplayerYaw;
    private float originalYaw;
    private int steps;

    private int playerYaw;
    private int playerPitch;
    private double playerSpeed;


    public wartMacronoTppad() {
        super("wart macro noTp", "no tp pad wart macro!", Category.MACRO, true);
        SettingsManager.getSettingsManager().rSetting(new Setting("yaw", this, 90, 1, 90, true));
        SettingsManager.getSettingsManager().rSetting(new Setting("pitch", this, 9, 0, 90, true));
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.RenderTickEvent event) {
        if (mc == null || mc.theWorld == null || mc.thePlayer == null) return;

        player = mc.thePlayer; // re making the player cuz we accessing fields

        //get player speed to see if we reached the end of the farm already
        playerSpeed = player.getDistance(player.lastTickPosX, player.lastTickPosY, player.lastTickPosZ);

        if(System.currentTimeMillis() - YSpeedTimer > 400){
            YSpeedTimer = System.currentTimeMillis();
            if(player.posY - player.lastTickPosY < 0){
                MinecraftForge.EVENT_BUS.post(new playerFallEvent());
            }
        }


        //that runs if we just stared the module, checks if we can start moving and breaking
        if (!ismacroingReady) {
            //notify the user what we want them to do
            drawCenterStringOBJ.GuiNotif(mc, "macro will start when you lock your head postion on the right angle");
            drawCenterStringOBJ.drawCenterStringOnScreenLittleToDown(mc, "press key " + Keyboard.getKeyName(this.getKeyBindingObj().getKeyCode()) + " to stop", "ff002f");


            //update player pitch and yaw with up to date info
            playerYaw = Math.round(mc.thePlayer.rotationYaw);
            playerPitch = Math.round(mc.thePlayer.rotationPitch);

            //if the yaw if at a 45o angle and pitch is 0, so we can start macroing
            boolean temp = playerYaw % wantedYaw == 0 && playerPitch == wantedPitch;

            ismacroingReady = temp;

        }
        if (ismacroingReady) {
            //locks mouse and keyboard
            Mouse.getDX();
            Mouse.getDY();
            mc.mouseHelper.deltaX = mc.mouseHelper.deltaY = 0;

            mc.setIngameNotInFocus();

            if(debug) Mouse.setGrabbed(false);

            //the text :)
            drawCenterStringOBJ.GuiNotif(mc, "macroing ur life away!");

            //show the release message
            drawCenterStringOBJ.drawCenterStringOnScreenLittleToDown(mc, "press key " + Keyboard.getKeyName(this.getKeyBindingObj().getKeyCode()) + " to stop", "ff002f");



            //checks the speed every half second so we dont spam the variable
            if (System.currentTimeMillis() - this.playerSpeedCheckTimer > 500) {
                //reset the timer
                this.playerSpeedCheckTimer = System.currentTimeMillis();

                if (playerTeleported) {
                    macroWalkStage = LEFT;
                    macroWalkHistory[0] = LEFT;
                    //just holds the attack key down
                    KeyBinding.setKeyBindState(mc.gameSettings.keyBindAttack.getKeyCode(), true);
                    playerTeleported = false;
                }else if(playerFallen){
                    KeyBinding.setKeyBindState(mc.gameSettings.keyBindAttack.getKeyCode(), false);
                    KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.getKeyCode(), false);
                    KeyBinding.setKeyBindState(mc.gameSettings.keyBindRight.getKeyCode(), false);
                    KeyBinding.setKeyBindState(mc.gameSettings.keyBindLeft.getKeyCode(), false);
                    KeyBinding.setKeyBindState(mc.gameSettings.keyBindSmoothCamera.getKeyCode(), true);
                    if(wantedplayerYaw == 0){
                        wantedplayerYaw = player.rotationYaw + 180;
                        macroWalkStage = DEFAULT;
                        originalYaw = player.rotationYaw;
                        interpolateTimer = System.currentTimeMillis();
                    }else{
//                        if(steps > 0){
//                            if(System.currentTimeMillis() - interpolateTimer > 2){
//                                interpolateTimer = System.currentTimeMillis();
//                                player.rotationYaw = originalYaw + (steps/100) * wantedplayerYaw - originalYaw;
//                                steps--;
//                            }
//                        }
                        if(System.currentTimeMillis() - interpolateTimer > 2){
                            interpolateTimer = System.currentTimeMillis();

                            player.rotationYaw = player.rotationYaw + 45;
                        }
                    }
                    if(wantedplayerYaw == player.rotationYaw){
                        macroWalkStage = LEFT;
                        playerFallen = false;
                        originalYaw = wantedplayerYaw = 0;
                        macroWalkHistory[0] = LEFT;
                        KeyBinding.setKeyBindState(mc.gameSettings.keyBindAttack.getKeyCode(), true);
                        KeyBinding.setKeyBindState(mc.gameSettings.keyBindSmoothCamera.getKeyCode(), false);
                    }
                } else {
                    if (playerSpeed <= 0.1F) {
                        macroStages t = nextWalkStage(macroWalkStage);
                        if (t == null) {
                            return;
                        }
                        //just holds the attack key down
                        KeyBinding.setKeyBindState(mc.gameSettings.keyBindAttack.getKeyCode(), true);
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
        if(m == DEFAULT){
            return;
        }
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
            sendChatMessage.sendClientMessage("please set a keybind!", true);
            this.setToggled(false);
            return;
        }
        if (!debug) {
            if (!isOnUpdater.isOnPrivateIsland()) {
                sendChatMessage.sendClientMessage("please join a your island!", true);
                this.setToggled(false);
                return;
            }
        }
        if(macroWalkStage == DEFAULT) macroWalkStage = RIGHT;

        sendChatMessage.sendClientMessage(" enabled wart macro", true);


        this.wantedPitch = SettingsManager.getSettingsManager().getSettingByName("pitch", this).getValInt();
        this.wantedYaw = SettingsManager.getSettingsManager().getSettingByName("yaw", this).getValInt();
        //reset the timer on enable
        this.playerSpeedCheckTimer = System.currentTimeMillis();
        this.YSpeedTimer = System.currentTimeMillis();

        mc.displayGuiScreen(null);

    }

    @Override
    public void onDisable() {
        super.onDisable();

        sendChatMessage.sendClientMessage(" disabled wart macro", true);
        /*
        reset every variable & unpress every key on disable
         */
        mc.setIngameFocus();
        playerTeleported = false;
        ismacroingReady = false;
        playerFallen = false;

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
    public void onPlayerFallEvent(playerFallEvent e){
        if(ismacroingReady && !playerTeleported){
            sendChatMessage.sendClientMessage(" daddy cum harder!", true);
            playerFallen = true;
        }
    }


    @SubscribeEvent
    public void onPlayerTeleportEvent(playerTeleportEvent event) {
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
