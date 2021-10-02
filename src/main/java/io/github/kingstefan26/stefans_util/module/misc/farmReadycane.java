package io.github.kingstefan26.stefans_util.module.misc;

import io.github.kingstefan26.stefans_util.core.module.ModuleManager;
import io.github.kingstefan26.stefans_util.core.module.blueprints.Module;
import io.github.kingstefan26.stefans_util.util.renderUtil.drawCenterString;
import io.github.kingstefan26.stefans_util.util.sendChatMessage;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Mouse;

public class farmReadycane extends Module {

    public farmReadycane(){
        super("farmReady", "gets you ready to farm cane", ModuleManager.Category.MISC, true);
        this.enableMessage = "farmReady-cane enabled";
        this.disableMessage = "farmReady-cane disabled";
    }
    public boolean toggled;

    private drawCenterString drawCenterStringOBJ = drawCenterString.getdrawCenterString();
    private long temptime;


    private boolean headlockCondition = false;

    private int playerYaw;
    private int playerPitch;

    @SubscribeEvent
    public void onTick(TickEvent.RenderTickEvent e) {
        if( mc == null || mc.theWorld == null || mc.thePlayer == null ){
            return;
        }

            if(System.currentTimeMillis() - temptime < 7 * 1000){
                drawCenterStringOBJ.GuiNotif(mc, "farm helper will lock your head postion on the right angle");
            }

            if(!headlockCondition){
                //updatePitchAndYaw();
                playerYaw = Math.round(mc.thePlayer.rotationYaw);
                playerPitch = Math.round(mc.thePlayer.rotationPitch);
                headlockCondition = playerYaw % 45 == 0 && playerPitch == 0;
                //headlockCondition = checkHeadCondition(playerPitch, playerYaw);
            }else{
                Mouse.getDX();
                Mouse.getDY();
                mc.mouseHelper.deltaX = mc.mouseHelper.deltaY = 0;
            }

    }

    private void updatePitchAndYaw(){
        //gets the player yaw and pitch
        playerYaw = Math.round(mc.thePlayer.rotationYaw);
        playerPitch = Math.round(mc.thePlayer.rotationPitch);
    }

    private boolean checkHeadCondition(int playerPitch, int playerYaw){
        return playerYaw % 45 == 0 && playerPitch == 0;
    }

    @Override
    public void onEnable() {
        super.onEnable();
        temptime = System.currentTimeMillis();
    }

    @Override
    public void onDisable() {
        super.onDisable();
        headlockCondition = false;
        playerYaw = playerPitch = 0;
    }
    @SubscribeEvent
    public void onUnloadWorld(WorldEvent.Unload event) {
        super.setToggled(false);
        sendChatMessage.sendClientMessage(" farmReady-cane was unloaded because you switched worlds", true);
    }
}
