package io.github.kingstefan26.stefans_util.module.render;

import io.github.kingstefan26.stefans_util.core.preRewrite.module.Module;
import io.github.kingstefan26.stefans_util.core.preRewrite.module.ModuleManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class noPlayer extends Module {
    public noPlayer(){
        super("noPlayer", "cock", ModuleManager.Category.RENDER);
    }
    @SubscribeEvent
    public void onPrePlyerReder(RenderLivingEvent.Pre a){
        if(a.entity instanceof EntityPlayer) {
            if(a.entity.isEntityAlive() && a.entity != mc.thePlayer){
                a.setCanceled(true);
            }
        }
    }

}
