/*
 * Copyright (c) 2022. All copyright reserved
 */

package io.github.kingstefan26.stefans_util.module.macro.oldshit;

import io.github.kingstefan26.stefans_util.core.module.moduleDecorators.impl.keyBindDecorator;
import io.github.kingstefan26.stefans_util.core.module.moduleDecorators.impl.onoffMessageDecorator;
import io.github.kingstefan26.stefans_util.core.module.moduleFrames.basicModule;
import io.github.kingstefan26.stefans_util.service.impl.chatService;
import io.github.kingstefan26.stefans_util.util.renderUtil.drawCenterString;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Mouse;

import static io.github.kingstefan26.stefans_util.core.module.ModuleMenagers.moduleManager.Category.MISC;

public class farmReadycane extends basicModule {

    public farmReadycane() {
        super("farmReady", "gets you ready to farm cane", MISC, new keyBindDecorator("farmReadyCane"),
                new onoffMessageDecorator());
    }

    private long temptime;


    private boolean headlockCondition = false;

    private int playerYaw;
    private int playerPitch;

    @SubscribeEvent
    public void onTick(TickEvent.RenderTickEvent e) {
        if (mc == null || mc.theWorld == null || mc.thePlayer == null) {
            return;
        }

        if (System.currentTimeMillis() - temptime < 7 * 1000) {
            drawCenterString.GuiNotif(mc, "farm helper will lock your head position on the right angle");
        }

        if (!headlockCondition) {
            //updatePitchAndYaw();
            playerYaw = Math.round(mc.thePlayer.rotationYaw);
            playerPitch = Math.round(mc.thePlayer.rotationPitch);
            headlockCondition = playerYaw % 45 == 0 && playerPitch == 0;
            //headlockCondition = checkHeadCondition(playerPitch, playerYaw);
        } else {
            Mouse.getDX();
            Mouse.getDY();
            mc.mouseHelper.deltaX = mc.mouseHelper.deltaY = 0;
        }

    }

    private void updatePitchAndYaw() {
        //gets the player yaw and pitch
        playerYaw = Math.round(mc.thePlayer.rotationYaw);
        playerPitch = Math.round(mc.thePlayer.rotationPitch);
    }

    private boolean checkHeadCondition(int playerPitch, int playerYaw) {
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
        chatService.queueClientChatMessage("farmReady-cane was unloaded because you switched worlds", chatService.chatEnum.CHATPREFIX);
    }
}
