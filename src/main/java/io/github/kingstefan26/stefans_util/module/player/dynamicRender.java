package io.github.kingstefan26.stefans_util.module.player;

import io.github.kingstefan26.stefans_util.core.preRewrite.module.ModuleManager;
import io.github.kingstefan26.stefans_util.core.preRewrite.module.Module;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class dynamicRender extends Module {
    public dynamicRender(){
        super("mgm", "mhm", ModuleManager.Category.DEBUG);
    }

    int savedRenderDistance;
    long changeTimer;

    @Override
    public void onEnable(){
        int a = mc.gameSettings.renderDistanceChunks;
        System.out.println("enabled MODULE!!!!");
        //float a = mc.gameSettings.getOptionFloatValue(GameSettings.Options.RENDER_DISTANCE);
        //sendChatMessage.sendClientDebugMessage("render distance: " + a);
    }
                                //this.gameSettings.setOptionValue(GameSettings.Options.RENDER_DISTANCE, GuiScreen.isShiftKeyDown() ? -1 : 1);

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent e){
        if(savedRenderDistance != 0){
            if(System.currentTimeMillis() - changeTimer > 2000){
                mc.gameSettings.renderDistanceChunks = savedRenderDistance;
                //sendChatMessage.sendClientDebugMessage("changin render distance to: " + (int)savedRenderDistance);
                System.out.println("changing render distance to: " + savedRenderDistance);
                savedRenderDistance = 0;
                changeTimer = 0;
            }
        }
    }


    @SubscribeEvent
    public void onwordSwitch(WorldEvent.Unload event){
        changeTimer = System.currentTimeMillis();
        savedRenderDistance = mc.gameSettings.renderDistanceChunks;
        mc.gameSettings.renderDistanceChunks = 2;
        System.out.println("changing render distance to: 2");
        //sendChatMessage.sendClientDebugMessage("changing render distance to: 2");
    }

}
