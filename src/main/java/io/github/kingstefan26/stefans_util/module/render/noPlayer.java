package io.github.kingstefan26.stefans_util.module.render;

import io.github.kingstefan26.stefans_util.core.module.ModuleManager;
import io.github.kingstefan26.stefans_util.core.module.moduleframes.BasicModule;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class noPlayer extends BasicModule {
    public noPlayer() {
        super("noPlayer", "cock", ModuleManager.Category.RENDER);
    }

    /**
     * Checks if the given entity is an NPC
     *
     * @param entity the entity to check
     * @return {@code true} if the entity is an NPC, {@code false} otherwise
     */
    public static boolean isNPC(Entity entity) {
        if (!(entity instanceof EntityOtherPlayerMP)) {
            return false;
        }

        EntityLivingBase entityLivingBase = (EntityLivingBase) entity;

        return entity.getUniqueID().version() == 2 && entityLivingBase.getHealth() == 20.0F && !entityLivingBase.isPlayerSleeping();
    }

    // https://github.com/BiscuitDevelopment/SkyblockAddons/blob/34b907fc44003ad150ea0681d85b3851f63b51a7/src/main/java/codes/biscuit/skyblockaddons/core/npc/NPCUtils.java#L91-L105

    @SubscribeEvent
    public void onPrePlyerReder(RenderLivingEvent.Pre a) {
        if (a.entity instanceof EntityPlayer && !isNPC(a.entity) && a.entity != mc.thePlayer) {
            a.setCanceled(true);
        }
    }
}
