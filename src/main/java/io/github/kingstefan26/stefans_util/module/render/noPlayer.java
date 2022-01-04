package io.github.kingstefan26.stefans_util.module.render;

import io.github.kingstefan26.stefans_util.core.module.ModuleMenagers.moduleManager;
import io.github.kingstefan26.stefans_util.core.module.moduleFrames.basicModule;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class noPlayer extends basicModule {
    public noPlayer(){
        super("noPlayer", "cock", moduleManager.Category.RENDER);
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
