/*
 * Copyright (c) 2022. All copyright reserved
 */

package io.github.kingstefan26.stefans_util.module.debug;

import io.github.kingstefan26.stefans_util.core.module.moduleframes.prototypeModule;
import io.github.kingstefan26.stefans_util.core.setting.impl.ChoseAKeySetting;
import io.github.kingstefan26.stefans_util.module.wip.wart.helper.pathCrumsHelper;
import io.github.kingstefan26.stefans_util.service.impl.chatService;
import io.github.kingstefan26.stefans_util.service.impl.inputLockerService;
import io.github.kingstefan26.stefans_util.util.renderUtil.draw3Dline;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;

import java.util.List;

public class pathDebug extends prototypeModule {


    public static int LIMIT = 1500;
    boolean isRecording = false;
    Vec3 recordStartPos;
    int keyKey;
    boolean shouldReplay = false;
    int stepsLeft;
    List<Vec3> stepsLefts;
    private int replayKey;


    public pathDebug() {
        super("pathDebug");
        new ChoseAKeySetting("clear path", this, Keyboard.KEY_0, (newvalue) -> keyKey = (int) newvalue);
        new ChoseAKeySetting("replay", this, Keyboard.KEY_0, (newvalue) -> replayKey = (int) newvalue);
    }

    @Override
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (Keyboard.isCreated() && Keyboard.getEventKeyState()) {

            int keyCode = Keyboard.getEventKey();
            if (keyCode <= 0)
                return;

            if (keyCode == keyKey) {
                chatService.queueCleanChatMessage("Clearing");
                clear();
            }

            if (keyCode == replayKey) {
                replay();
            }

        }
        super.onKeyInput(event);
    }

    private void clear() {
        recordStartPos = null;
        pathCrumsHelper.getInstance().clear();
    }

    void replay() {
        if (shouldReplay || recordStartPos == null || pathCrumsHelper.getInstance().pathPoints == null || pathCrumsHelper.getInstance().pathPoints.size() == 0) {
            chatService.queueCleanChatMessage("Ehh you cant replay");
            return;
        }
        mc.thePlayer.setPosition(recordStartPos.xCoord, recordStartPos.yCoord, recordStartPos.zCoord);
        shouldReplay = true;
        stepsLefts = pathCrumsHelper.getInstance().pathPoints;
        stepsLeft = stepsLefts.size();
        inputLockerService.lock();
    }

    @Override
    protected void PROTOTYPETEST() {
        isRecording = !isRecording;
        if (!isRecording) {
            chatService.queueCleanChatMessage("currently going " + pathCrumsHelper.getInstance().getPathPoints().size() + " points strong");
            pathCrumsHelper.getInstance().simplify();
            chatService.queueCleanChatMessage("after optimasing " + pathCrumsHelper.getInstance().getPathPoints().size());
            if (pathCrumsHelper.getInstance().pathPoints.size() >= LIMIT) {
                chatService.queueCleanChatMessage("more then " + LIMIT + " CLEARING");
                clear();
            }
        } else {
            recordStartPos = mc.thePlayer.getLookVec();
        }
    }

    @Override
    public void onTick(TickEvent.ClientTickEvent e) {
        if (shouldReplay && e.phase == TickEvent.Phase.START) {

            if (stepsLeft == 0) {
                shouldReplay = false;
                inputLockerService.unlock();
                stepsLefts = null;
                stepsLeft = 0;
            }

            Vec3 tomove = stepsLefts.get(stepsLeft - 1);

            mc.thePlayer.setPositionAndUpdate(tomove.xCoord, tomove.yCoord, tomove.zCoord);


            stepsLeft = stepsLefts.size() - 1;


        }
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
