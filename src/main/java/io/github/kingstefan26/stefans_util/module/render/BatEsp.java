package io.github.kingstefan26.stefans_util.module.render;

import io.github.kingstefan26.stefans_util.core.module.ModuleManager;
import io.github.kingstefan26.stefans_util.core.module.moduleDecorators.impl.keyBindDecorator;
import io.github.kingstefan26.stefans_util.core.module.moduleDecorators.impl.onoffMessageDecorator;
import io.github.kingstefan26.stefans_util.core.module.moduleframes.BasicModule;
import io.github.kingstefan26.stefans_util.util.renderUtil.renderEsp;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityBat;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class BatEsp extends BasicModule {

    public BatEsp() {
        super("BatEsp", "shows location of bats", ModuleManager.Category.RENDER, new keyBindDecorator("batesp"), new onoffMessageDecorator());
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
