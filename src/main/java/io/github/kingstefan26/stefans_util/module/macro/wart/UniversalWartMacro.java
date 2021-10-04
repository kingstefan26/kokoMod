package io.github.kingstefan26.stefans_util.module.macro.wart;

import io.github.kingstefan26.stefans_util.core.module.Module;
import io.github.kingstefan26.stefans_util.core.module.ModuleManager;
import io.github.kingstefan26.stefans_util.core.module.moduleRegistery;
import io.github.kingstefan26.stefans_util.core.setting.Setting;
import io.github.kingstefan26.stefans_util.core.setting.SettingsManager;
import io.github.kingstefan26.stefans_util.main;
import io.github.kingstefan26.stefans_util.module.macro.macroUtil.cropType;
import io.github.kingstefan26.stefans_util.module.macro.macroUtil.lastLeftOff.lastLeftOff;
import io.github.kingstefan26.stefans_util.module.macro.macroUtil.lastLeftOff.lastleftoffObject;
import io.github.kingstefan26.stefans_util.module.macro.macroUtil.macroMenu;
import io.github.kingstefan26.stefans_util.module.macro.macroUtil.macroStages;
import io.github.kingstefan26.stefans_util.module.util.chat;
import io.github.kingstefan26.stefans_util.module.util.inputLocker;
import io.github.kingstefan26.stefans_util.util.renderUtil.drawCenterString;
import io.github.kingstefan26.stefans_util.util.stefan_utilEvents;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.util.ArrayList;
import java.util.Objects;

public class UniversalWartMacro extends Module {

    public UniversalWartMacro() {
        super("UniversalWartMacro", "macros wart", ModuleManager.Category.MACRO, true);
    }

    enum walkStates {
        LEFT, RIGHT, TOP, BOTTOM, HOLD, DEFAULT, LEFTTOP, RIGHTTOP, RIGHTBOTTOM, LEFTBOTTOM
    }


    public ArrayList<String> options;
    private final drawCenterString drawCenterStringOBJ = drawCenterString.getdrawCenterString();
    private io.github.kingstefan26.stefans_util.module.macro.macroUtil.macroMenu macroMenu;

    private boolean isMacroingReady;
    private int wantedPitch;
    private int wantedYaw;
    private boolean perfectHeadRotation;
    private boolean experimentalGui;

    private walkStates macroWalkStage = walkStates.RIGHT;

    private boolean playerTeleported;
    private boolean playerFallen;
    private boolean guiCloseGrace;


    private long playerSpeedCheckTimer;
    private long YSpeedTimer;


    private int playerYaw;
    private int playerPitch;
    private double playerSpeed;
    private int fallCounter;


    @Override
    public void onLoad(){
        options = new ArrayList<>();
        options.add("vertical design");
        options.add("horizontal with pads");
        options.add("horizontal with no pad");
        SettingsManager.getSettingsManager().rSetting(new Setting("version", this,"vertical design",options));
        SettingsManager.getSettingsManager().rSetting(new Setting("yaw", this, 90, 1, 90, true));
        SettingsManager.getSettingsManager().rSetting(new Setting("pitch", this, 9, 0, 90, true));
        SettingsManager.getSettingsManager().rSetting(new Setting("perfect head rotation", this, true));
        SettingsManager.getSettingsManager().rSetting(new Setting("experimental gui", this, false));
        super.onLoad();
    }

    private float getYaw(){
        float yaw = mc.thePlayer.rotationYawHead;
        yaw %= 360;
        if(yaw < 0) yaw += 360;
        if(yaw > 180) yaw -= 360;
        return yaw;
    }


    @SubscribeEvent
    public void onRenderLast(TickEvent.RenderTickEvent a){
        if (mc == null || mc.theWorld == null || mc.thePlayer == null) return;
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
                MinecraftForge.EVENT_BUS.post(new stefan_utilEvents.playerFallEvent());
            }
        }
    }

    private void notReadyOnTickRoutine() {
        //notify the user what we want them to do
        drawCenterStringOBJ.GuiNotif(mc,
                "macro will start when you lock your head position on the right angle");
        drawCenterStringOBJ.drawCenterStringOnScreenLittleToDown(mc,
                "press key " + Keyboard.getKeyName(this.getKeyBindingObj().getKeyCode()) + " to stop",
                "ff002f");

        //update player pitch and yaw with up to date info
//        playerYaw = Math.round(mc.thePlayer.rotationYaw);
//        playerPitch = Math.round(mc.thePlayer.rotationPitch);

//        isMacroingReady = playerYaw % wantedYaw == 0 && playerPitch == wantedPitch;
        isMacroingReady = true;

        if(isMacroingReady){
            //lastLeftOff.nullLastLeftOff();
            if(perfectHeadRotation){
                mc.thePlayer.rotationYaw = wantedYaw;
                mc.thePlayer.rotationPitch = wantedPitch;
            }
            if(experimentalGui){
                guiCloseGrace = false;
                mc.displayGuiScreen(macroMenu);
            }
            inputLocker.locked = true;
            inputLocker.unlockkey = this.getKeyBindingObj().getKeyCode();
        }
    }

    private void readyOnTickRoutine() {
        //the text :)
        drawCenterStringOBJ.GuiNotif(mc, "Whats good korea");

        //show the release message

        drawCenterStringOBJ.drawCenterStringOnScreenLittleToDown(mc, "press key "
                + Keyboard.getKeyName(this.getKeyBindingObj().getKeyCode()) +
                " to stop", "ff002f");



        //checks the speed every half second so we don't spam the variable
        if (System.currentTimeMillis() - this.playerSpeedCheckTimer > 200) {
            //reset the timer
            this.playerSpeedCheckTimer = System.currentTimeMillis();

            if (playerTeleported) {
                playerTeleported = false;
            }
            if (playerFallen) {
                playerFallen = false;
            }
            switch (macroWalkStage){
                case LEFT:
                    macroWalkStage = walkStates.LEFTTOP;
                    break;
                case RIGHT:
                    macroWalkStage = walkStates.RIGHTBOTTOM;
                    break;
                case TOP:
                    macroWalkStage = walkStates.RIGHTTOP;
                    break;
                case BOTTOM:
                    macroWalkStage = walkStates.LEFTBOTTOM;
                    break;
                case LEFTTOP:
                    macroWalkStage = walkStates.RIGHTTOP;
                    break;
                case RIGHTTOP:
                    macroWalkStage = walkStates.RIGHT;
                    break;
                case RIGHTBOTTOM:
                    macroWalkStage = walkStates.BOTTOM;
                    break;
                case LEFTBOTTOM:
                    macroWalkStage = walkStates.LEFT;
                    break;
            }
            this.logger.info(macroWalkStage);

        }
        macroWalk(macroWalkStage);
    }


    private void macroWalk(walkStates m) {
        if (m == walkStates.DEFAULT) {
            return;
        }

        switch (m) {
            case LEFT:
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindRight.getKeyCode(), false);
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindLeft.getKeyCode(), true);
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.getKeyCode(), false);
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindBack.getKeyCode(), false);
                break;
            case RIGHT:
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindRight.getKeyCode(), true);
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindLeft.getKeyCode(), false);
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.getKeyCode(), false);
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindBack.getKeyCode(), false);
                break;
            case TOP:
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindRight.getKeyCode(), false);
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindLeft.getKeyCode(), false);
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.getKeyCode(), true);
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindBack.getKeyCode(), false);
                break;
            case BOTTOM:
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindRight.getKeyCode(), false);
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindLeft.getKeyCode(), false);
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.getKeyCode(), false);
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindBack.getKeyCode(), true);
                break;
            case LEFTTOP:
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindRight.getKeyCode(), false);
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindLeft.getKeyCode(), false);
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.getKeyCode(), true);
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindBack.getKeyCode(), true);
                break;
            case RIGHTTOP:
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindRight.getKeyCode(), true);
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindLeft.getKeyCode(), false);
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.getKeyCode(), true);
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindBack.getKeyCode(), false);
                break;
            case RIGHTBOTTOM:
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindRight.getKeyCode(), true);
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindLeft.getKeyCode(), false);
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.getKeyCode(), false);
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindBack.getKeyCode(), true);
                break;
            case LEFTBOTTOM:
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindRight.getKeyCode(), false);
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindLeft.getKeyCode(), true);
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.getKeyCode(), false);
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindBack.getKeyCode(), true);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + m);
        }
    }

    @Override
    public void onEnable(){
        super.onEnable();
        if (this.getKey() == 0) {
            chat.queueClientChatMessage("please set a keybinding!", chat.chatEnum.CHAT);
            this.setToggled(false);
            return;
        }
        if (!main.debug) {
            if (!io.github.kingstefan26.stefans_util.module.util.SBinfo.isOnPrivateIsland()) {
                chat.queueClientChatMessage("please join a your island!", chat.chatEnum.CHAT);
                this.setToggled(false);
                return;
            }
        }
        for(Module m : moduleRegistery.moduleRegistery_.loadedModules){
            if(Objects.equals(m.getName(), "Sprint")){
                m.setToggled(false);
            }
        }
//        if(lastLeftOff.getLastleftoffObject() != null) {
//            macroWalkStage = walkStates.getLastleftoffObject().getMacroStage();
//        }else{
//            macroWalkStage = walkStates.RIGHT;
//        }

        fallCounter = 0;
        wantedPitch = SettingsManager.getSettingsManager().getSettingByName("pitch", this).getValInt();
        wantedYaw = SettingsManager.getSettingsManager().getSettingByName("yaw", this).getValInt();
        experimentalGui = SettingsManager.getSettingsManager().getSettingByName("experimental gui", this).getValBoolean();
        perfectHeadRotation = SettingsManager.getSettingsManager().getSettingByName("perfect head rotation", this).getValBoolean();
        //reset the timer on enable
        this.playerSpeedCheckTimer = System.currentTimeMillis();
        this.YSpeedTimer = System.currentTimeMillis();

        if(experimentalGui){
            if(this.macroMenu == null) macroMenu = new macroMenu(this);

            guiCloseGrace = true;
        }

        mc.displayGuiScreen(null);
        chat.queueClientChatMessage("enabled wart macro", chat.chatEnum.CHAT);

    }

    @Override
    public void onDisable(){
        super.onDisable();
        mc.displayGuiScreen(null);

        for(Module m : moduleRegistery.moduleRegistery_.loadedModules){
            if(Objects.equals(m.getName(), "Sprint")){
                m.setToggled(true);
            }
        }

        chat.queueClientChatMessage("disabled wart macro", chat.chatEnum.CHAT);
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
        macroWalkStage = walkStates.BOTTOM;
//        lastLeftOff.getLastLeftOff().registerLastLeftOff(
//                new lastleftoffObject((float)mc.thePlayer.posX,
//                        (float) mc.thePlayer.posY,
//                        (float)mc.thePlayer.posZ,
//                        cropType.WART,
//                        walkStates,
//                        System.currentTimeMillis()));
    }

    @SubscribeEvent
    public void onPlayerFallEvent(stefan_utilEvents.playerFallEvent e) {
        if (isMacroingReady && !playerTeleported) {
            fallCounter++;
            chat.queueClientChatMessage("fallen for the " + fallCounter + " time", chat.chatEnum.CHAT);
            playerFallen = true;
        }
    }


    @SubscribeEvent
    public void onPlayerTeleportEvent(stefan_utilEvents.playerTeleportEvent event) {
        if (isMacroingReady) {
            chat.queueClientChatMessage("teleport detected!", chat.chatEnum.CHAT);
            playerTeleported = true;
        }
    }


    @SubscribeEvent
    public void onGuiOpen(GuiOpenEvent e) {
        if(experimentalGui){
            if (!(e.gui instanceof io.github.kingstefan26.stefans_util.module.macro.macroUtil.macroMenu) && !guiCloseGrace) {
                this.toggle();
            }
        }else{
            e.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onkey(stefan_utilEvents.clickedUnlockKeyEvent e){
        this.toggle();
    }


    @SubscribeEvent
    public void onUnloadWorld(WorldEvent.Unload event) {
        chat.queueClientChatMessage(this.getName() + " was unloaded because you switched worlds", chat.chatEnum.CHAT);
        super.setToggled(false);
    }
}
