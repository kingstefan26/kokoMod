/*
 * Copyright (c) 2022. All copyright reserved
 */

package io.github.kingstefan26.stefans_util.module.wip;

import io.github.kingstefan26.stefans_util.core.module.moduleDecorators.impl.keyBindDecorator;
import io.github.kingstefan26.stefans_util.core.module.moduleframes.BasicModule;
import io.github.kingstefan26.stefans_util.core.module.modulemenagers.ModuleManager;
import io.github.kingstefan26.stefans_util.service.impl.chatService;
import io.github.kingstefan26.stefans_util.util.StefanutilUtil;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.List;

public class GhostMacro extends BasicModule {
    EntityPlayerSP theWatcher;
    Entity closestEntity;
    List<EntityCreeper> clpier;
    int tick;

    public GhostMacro() {
        super("GhostMacro", "lol", ModuleManager.Category.WIP, new keyBindDecorator("GhostMacro"));
    }

    @Override
    public void onTick(TickEvent.ClientTickEvent e) {

        tick = StefanutilUtil.every20Ticks(e, tick, () -> {
            if (mc.thePlayer == null) return;

            if (theWatcher == null) theWatcher = mc.thePlayer;

            this.closestEntity = this.theWatcher.worldObj.findNearestEntityWithinAABB(EntityCreeper.class, this.theWatcher.getEntityBoundingBox().expand(10, 3.0D, 10), this.theWatcher);

            this.clpier = this.theWatcher.worldObj.getEntitiesWithinAABB(EntityCreeper.class, this.theWatcher.getEntityBoundingBox().expand(10, 3.0D, 10));


            if (closestEntity.isInvisible() && closestEntity instanceof EntityCreeper && ((EntityCreeper) closestEntity).getPowered()) {
                logger.info("YASSS");
            }
            chatService.queueCleanChatMessage(closestEntity == null ? "none" : closestEntity.toString());
        });

    }


    @Override
    public void onRenderTick(TickEvent.RenderTickEvent e) {

    }
}
