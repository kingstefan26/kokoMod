/*
 * Copyright (c) 2022. All copyright reserved
 */

package io.github.kingstefan26.stefans_util.module.wip;

import io.github.kingstefan26.stefans_util.core.module.ModuleMenagers.moduleManager;
import io.github.kingstefan26.stefans_util.core.module.moduleDecorators.impl.keyBindDecorator;
import io.github.kingstefan26.stefans_util.core.module.moduleFrames.basicModule;
import io.github.kingstefan26.stefans_util.service.impl.chatService;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.List;

public class GhostMacro extends basicModule {
    EntityPlayerSP theWatcher;
    Entity closestEntity;
    List<EntityCreeper> clpier;
    int tick;

    public GhostMacro() {
        super("GhostMacro", "lol", moduleManager.Category.WIP, new keyBindDecorator("GhostMacro"));
    }

    @Override
    public void onTick(TickEvent.ClientTickEvent e) {
        if (e.phase != TickEvent.Phase.START) return;
        tick++;
        if (tick % 20 != 0) return;

        tick = 0;
        if (mc.thePlayer == null) return;

        if (theWatcher == null) theWatcher = mc.thePlayer;

        this.closestEntity = this.theWatcher.worldObj.findNearestEntityWithinAABB(EntityCreeper.class, this.theWatcher.getEntityBoundingBox().expand(10, 3.0D, 10), this.theWatcher);

        this.clpier = this.theWatcher.worldObj.getEntitiesWithinAABB(EntityCreeper.class, this.theWatcher.getEntityBoundingBox().expand(10, 3.0D, 10));

        chatService.queueCleanChatMessage(closestEntity == null ? "none" : closestEntity.toString());
    }


    @Override
    public void onRenderTick(TickEvent.RenderTickEvent e) {

    }
}
