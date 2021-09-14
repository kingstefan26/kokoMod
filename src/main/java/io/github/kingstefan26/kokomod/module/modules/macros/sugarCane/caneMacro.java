package io.github.kingstefan26.kokomod.module.modules.macros.sugarCane;

import io.github.kingstefan26.kokomod.module.moduleUtil.module.Category;
import io.github.kingstefan26.kokomod.module.moduleUtil.module.Module;
import io.github.kingstefan26.kokomod.module.modules.macros.macroUtil.macroStages;
import io.github.kingstefan26.kokomod.util.forgeEventClasses.playerTeleportEvent;
import io.github.kingstefan26.kokomod.util.renderUtil.drawCenterString;
import io.github.kingstefan26.kokomod.util.sendChatMessage;
import io.github.kingstefan26.kokomod.util.stolenBs.KeyboardLocker;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class caneMacro extends Module {

    private drawCenterString drawCenterStringOBJ = drawCenterString.getdrawCenterString();

    private KeyboardLocker keyboardLocker = KeyboardLocker.getKeyboardLocker();


    private EntityPlayerSP player;

    private boolean ismacroingReady;
    private macroStages macroWalkStage = macroStages.DEFAULT;
    private macroStages lastmacroWalkStage = macroWalkStage.DEFAULT;

    private boolean playerTeleported;
    private long playerSpeedCheckTimer;
    private long walkForwardTimer;
    private int playerYaw;
    private int playerPitch;
    private double playerSpeed;
    private double distanceWalked;

    public caneMacro(){
        super("cane macro", "macros cane!", Category.MACRO, true," cane macro enabed"," cane macro disabled");
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.RenderTickEvent event) {
        if( mc == null || mc.theWorld == null || mc.thePlayer == null ) return;


        /*
        prevents you form using the mod without a keybind
         */
        if(this.getKey() == 0){
            drawCenterStringOBJ.GuiNotif(mc, "please set a keybind!");
            return;
        }


        player = mc.thePlayer; // re making the player cuz we accessing fields

        //get player speed to see if we reached the end of the farm already
        playerSpeed = player.getDistance(player.lastTickPosX, player.lastTickPosY, player.lastTickPosZ);

        //that runs if we just stared the module, checks if we can start moving and breaking
        if(!ismacroingReady){
            //notify the user what we want them to do
            drawCenterStringOBJ.GuiNotif(mc, "macro will start when you lock your head postion on the right angle");

            //update player pitch and yaw with up to date info
            playerYaw = Math.round(mc.thePlayer.rotationYaw);
            playerPitch = Math.round(mc.thePlayer.rotationPitch);

            //if the yaw if at a 45o angle and pitch is 0, so we can start macroing
            boolean temp = playerYaw % 45 == 0 && playerPitch == 0;
            //assign the results to ismacroingready, so we can start
            ismacroingReady = temp;
            //signals that macro is not deafult but not ready yet
            if(temp) macroWalkStage = macroStages.HOLD;

        }else{
            //locks mouse and keyboard
            Mouse.getDX();
            Mouse.getDY();
            mc.mouseHelper.deltaX = mc.mouseHelper.deltaY = 0;
            Mouse.setGrabbed(false);
            keyboardLocker.lockKeyboard();



            //checks if we just started
            if(macroWalkStage == macroStages.HOLD){

                if(lastmacroWalkStage == macroStages.DEFAULT){
                    //if no walk stages available default to right
                    macroWalkStage = macroStages.RIGHT;
                }else{
                    //restores the last macro walk stage if one is available
                    macroWalkStage = lastmacroWalkStage;
                    lastmacroWalkStage = macroStages.DEFAULT;
                }
            }

            //the text :)
            drawCenterStringOBJ.GuiNotif(mc, "macroing ur life away!");

            //show the release message
            ScaledResolution scaled = new ScaledResolution(mc);
            int width = scaled.getScaledWidth();
            int height = scaled.getScaledHeight();
            drawCenterStringOBJ.drawCenterStringOnScreenLittleToDown(mc,"press key "+Keyboard.getKeyName(this.getKeyBindingObj().getKeyCode()) +" to stop","ff002f");

            //just holds the attack key down
            KeyBinding.setKeyBindState(mc.gameSettings.keyBindAttack.getKeyCode(), true);

            //checks the speed every half second so we dont spam the variable
            if(System.currentTimeMillis() - this.playerSpeedCheckTimer > 500){
                //reset the timer
                this.playerSpeedCheckTimer = System.currentTimeMillis();

//                //if player is not moving
//                if(playerSpeed <= 0.1F){
//
//                    //rules to what happen if the player stopped moving
//                    switch (macroWalkStage){
//                        case RIGHT: //if player was moving right now we move down
//                            macroWalkStage = macroStages.BOTTOM;
//                            break;
//                        case BOTTOM: //if player was moving down now we move right
//                            macroWalkStage = RIGHT;
//                            break;
//                    }
//
//                }
                if(!playerTeleported){
                    macroStages t = nextWalkStage(macroWalkStage, playerSpeed);
                    if(t != null) {
                        macroWalkStage = t;
                    }
                    macroWalk(macroWalkStage);
                }
                if(playerTeleported){
                    playerTeleported = false;
                    macroWalkStage = macroStages.TOP;
                    walkForwardTimer = System.currentTimeMillis();
                }

            }


        }

    }

    private macroStages nextWalkStage(macroStages currentWalkStage, double PlayerSpeed) {
        macroStages temp = null;
        if(PlayerSpeed <= 0.1F){
            //rules to what happen if the player stopped moving
            switch (currentWalkStage){
                case RIGHT: //if player was moving right now we move down
                    temp = macroStages.BOTTOM;
                    break;
                case BOTTOM: //if player was moving down now we move right
                    temp = macroStages.RIGHT;
                    break;
                case TOP:
                    if(System.currentTimeMillis() - walkForwardTimer > 200){
                        temp = macroStages.RIGHT;
                    }else{
                        temp = macroStages.TOP;
                    }
                    break;
            }
        }
        return temp;
    }

    /*
    clicks and releases buttons based on input
     */
    private void macroWalk(macroStages m){
         switch (m){
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
        //reset the timer on enable
        this.playerSpeedCheckTimer = System.currentTimeMillis();
    }

    @Override
    public void onDisable(){
        super.onDisable();
        lastmacroWalkStage = macroWalkStage; //saves the last macro walk stage

        /*
        reset every variable & unpress every key on disable
         */
        ismacroingReady = false;
        distanceWalked = 0;
        macroWalkStage = macroStages.DEFAULT;
        playerYaw = 0;
        playerPitch = 0;
        playerSpeed = 0;
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindBack.getKeyCode(), false);
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindAttack.getKeyCode(), false);
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindRight.getKeyCode(), false);

        //unlocks the keyboard
        keyboardLocker.unlockKeyboard();
    }

    @SubscribeEvent
    public void onPlayerTeleportEvent(playerTeleportEvent event) {
        if(ismacroingReady){
            sendChatMessage.sendClientMessage(" teleport detected, changing walk stage to right!", true);
            playerTeleported = true;
        }
    }

    /*
    if the macro is running prevent any gui from being open
     */
    @SubscribeEvent
    public void onGuiOpen(GuiOpenEvent e){
        if(macroWalkStage != macroStages.DEFAULT){
            e.setCanceled(true);
        }
    }

    /*
    unloads the mod on world change to avoid captcha being spamed
     */
    @SubscribeEvent
    public void onUnloadWorld(WorldEvent.Unload event) {
        super.setToggled(false);
        sendChatMessage.sendClientMessage(" cane macro was unloaded because you switched worlds", true);
    }
}
