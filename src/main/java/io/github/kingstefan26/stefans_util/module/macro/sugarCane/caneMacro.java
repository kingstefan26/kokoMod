package io.github.kingstefan26.stefans_util.module.macro.sugarCane;

import io.github.kingstefan26.stefans_util.core.module.moduleDecorators.impl.keyBindDecorator;
import io.github.kingstefan26.stefans_util.core.module.moduleframes.BasicModule;
import io.github.kingstefan26.stefans_util.module.macro.util.macroStages;
import io.github.kingstefan26.stefans_util.service.impl.chatService;
import io.github.kingstefan26.stefans_util.util.StefanutilEvents;
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

import static io.github.kingstefan26.stefans_util.core.module.ModuleManager.Category.MACRO;

public class caneMacro extends BasicModule {

    private boolean ismacroingReady;
    private macroStages macroWalkStage = macroStages.DEFAULT;
    private macroStages lastmacroWalkStage = macroStages.DEFAULT;

    private boolean playerTeleported;
    private long playerSpeedCheckTimer;
    private long walkForwardTimer;
    private int playerYaw;
    private int playerPitch;
    private double playerSpeed;

    public caneMacro(){
        super("cane macro", "macros cane!", MACRO, new keyBindDecorator("caneMacro"));
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.RenderTickEvent event) {
        if( mc == null || mc.theWorld == null || mc.thePlayer == null ) return;


        /*
        prevents you form using the mod without a keybind
         */
        if (getLocalDecoratorManager().keyBindDecorator.keybind.getKeyCode() == 0) {
            drawCenterString.GuiNotif(mc, "please set a keybind!");
            return;
        }


        EntityPlayerSP player = mc.thePlayer; // re making the player cuz we accessing fields

        //get player speed to see if we reached the end of the farm already
        playerSpeed = player.getDistance(player.lastTickPosX, player.lastTickPosY, player.lastTickPosZ);

        //that runs if we just stared the module, checks if we can start moving and breaking
        if(!ismacroingReady){
            //notify the user what we want them to do
            drawCenterString.GuiNotif(mc, "macro will start when you lock your head postion on the right angle");

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
            drawCenterString.GuiNotif(mc, "macroing ur life away!");

            //show the release message
            ScaledResolution scaled = new ScaledResolution(mc);
            int width = scaled.getScaledWidth();
            int height = scaled.getScaledHeight();
            drawCenterString.drawCenterStringOnScreenLittleToDown(mc, "press key " + Keyboard.getKeyName(getLocalDecoratorManager().keyBindDecorator.keybind.getKeyCode()) + " to stop", "ff002f");

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
        double distanceWalked = 0;
        macroWalkStage = macroStages.DEFAULT;
        playerYaw = 0;
        playerPitch = 0;
        playerSpeed = 0;
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindBack.getKeyCode(), false);
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindAttack.getKeyCode(), false);
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindRight.getKeyCode(), false);


    }

    @SubscribeEvent
    public void onPlayerTeleportEvent(StefanutilEvents.playerTeleportEvent event) {
        if (ismacroingReady) {
            chatService.queueClientChatMessage("teleport detected, changing walk stage to right!", chatService.chatEnum.PREFIX);
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
        chatService.queueClientChatMessage("cane macro was unloaded because you switched worlds", chatService.chatEnum.PREFIX);
    }
}
