/*
 * Copyright (c) 2022. All copyright reserved
 */

package io.github.kingstefan26.stefans_util.module.debug;

import io.github.kingstefan26.stefans_util.core.module.moduleFrames.prototypeModule;
import io.github.kingstefan26.stefans_util.module.wip.wart.helper.pathCrumsHelper;
import io.github.kingstefan26.stefans_util.service.impl.chatService;
import io.github.kingstefan26.stefans_util.util.renderUtil.draw3Dline;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class pathDebug extends prototypeModule {
    boolean isRecording = false;

    public pathDebug() {
        super("pathDebug");
    }

    @Override
    protected void PROTOTYPETEST() {
        isRecording = !isRecording;
        if (!isRecording) {
            chatService.queueCleanChatMessage("currently going " + pathCrumsHelper.getInstance().getPathPoints().size() + " points strong");
            pathCrumsHelper.getInstance().simplify();
            chatService.queueCleanChatMessage("after optimasing " + pathCrumsHelper.getInstance().getPathPoints().size());
            if (pathCrumsHelper.getInstance().pathPoints.size() >= 150) {
                chatService.queueCleanChatMessage("more then 150 CLEARING");
                pathCrumsHelper.getInstance().clear();
            }
        }
    }

    @Override
    public void onTick(TickEvent.ClientTickEvent e) {
        if (isRecording && e.phase == TickEvent.Phase.START) pathCrumsHelper.getInstance().update();
    }


    @Override
    public void onWorldRender(RenderWorldLastEvent e) {
        if (pathCrumsHelper.getInstance().pathPoints == null) return;
        for (int i = 0; i < (pathCrumsHelper.getInstance().getPathPoints().size() - 1); i++) {
            Vec3 vertex = pathCrumsHelper.getInstance().getPathPoints().get(i);
            Vec3 vertex1 = pathCrumsHelper.getInstance().getPathPoints().get(i + 1);

//            GlStateManager.disableDepth();
//            GlStateManager.disableCull();
//            GlStateManager.disableTexture2D();
//
//            hehe.drawFilledBoundingBox(new AxisAlignedBB(vertex.xCoord - (mc.getRenderViewEntity().lastTickPosX + (mc.getRenderViewEntity().posX - mc.getRenderViewEntity().lastTickPosX) * e.partialTicks), vertex.yCoord - (mc.getRenderViewEntity().lastTickPosY + (mc.getRenderViewEntity().posY - mc.getRenderViewEntity().lastTickPosY) * e.partialTicks), vertex.zCoord - (mc.getRenderViewEntity().lastTickPosZ + (mc.getRenderViewEntity().posZ - mc.getRenderViewEntity().lastTickPosZ) * e.partialTicks), vertex.xCoord - (mc.getRenderViewEntity().lastTickPosX + (mc.getRenderViewEntity().posX - mc.getRenderViewEntity().lastTickPosX) * e.partialTicks) + 1, vertex.yCoord - (mc.getRenderViewEntity().lastTickPosY + (mc.getRenderViewEntity().posY - mc.getRenderViewEntity().lastTickPosY) * e.partialTicks) + 1, vertex.zCoord - (mc.getRenderViewEntity().lastTickPosZ + (mc.getRenderViewEntity().posZ - mc.getRenderViewEntity().lastTickPosZ) * e.partialTicks) + 1), 0.5f, 0x3b0c0d);
//
//            GlStateManager.disableLighting();
//            GlStateManager.enableDepth();
//            GlStateManager.enableTexture2D();


            if (vertex != null && vertex1 != null) {
                draw3Dline.draw3DLine(vertex,
                        vertex1,
                        0xffffff,
                        5,
                        false,
                        e.partialTicks);
            }
        }
    }
}
