package io.github.kingstefan26.stefans_util.module.render;

import io.github.kingstefan26.stefans_util.core.module.Category;
import io.github.kingstefan26.stefans_util.core.module.blueprints.Module;
import io.github.kingstefan26.stefans_util.util.renderUtil.renderEsp;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityBat;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class BatEsp extends Module {

    public BatEsp(){
        super("BatEsp", "shows location of bats", Category.RENDER, true);
        this.enableMessage = "enabled batEsp";
        this.disableMessage = "disabled batEsp";
    }

    @SubscribeEvent
    public void RenderLivingEvent(RenderWorldLastEvent event) {
        for (Entity ent : Minecraft.getMinecraft().theWorld.loadedEntityList) {
            if (ent instanceof EntityBat) {
                renderEsp.drawbox(ent.posX, ent.posY, ent.posZ, event);
            }
        }
    }
}
