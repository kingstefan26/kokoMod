/*
 * Copyright (c) 2022. All copyright reserved
 */

package io.github.kingstefan26.stefans_util.module.debug;

import io.github.kingstefan26.stefans_util.core.module.moduleFrames.prototypeModule;
import io.github.kingstefan26.stefans_util.module.macro.util.util;
import io.github.kingstefan26.stefans_util.module.macro.wart.helper.blocktype;
import io.github.kingstefan26.stefans_util.module.macro.wart.helper.diraction;
import io.github.kingstefan26.stefans_util.service.impl.chatService;
import io.github.kingstefan26.stefans_util.service.impl.keyControlService;
import io.github.kingstefan26.stefans_util.util.renderUtil.hehe;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Tuple;
import net.minecraftforge.client.event.RenderWorldLastEvent;


public class blockThings extends prototypeModule {
    static BlockPos renderingPos = new BlockPos(1, 1, 1);
    String toRenderText = "hi";

    public blockThings() {
        super("test of block thigns");
    }

    @Override
    protected void PROTOTYPETEST() {
        (new Thread(() -> {

            keyControlService.action.walk t = whichWayToGoMockUp(keyControlService.action.walk.back);

            chatService.queueCleanChatMessage("walk prediction " + t);

//            renderingPos = util.unlitaviseCordsWithDications(new BlockPos(1, 1, 0), util.getPlayerFeetBlockPos(), diractionZERO);

        })).start();

    }


    public static int spamCounter;

    public static keyControlService.action.walk whichWayToGoMockUp(keyControlService.action.walk currentWalkAcction) {
        keyControlService.action.walk result = null;

        // 0 = soutfh
        // 1 = west
        // 2 = noth
        // 3 = east
        int dir = MathHelper.floor_double((double) (mc.thePlayer.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
        diraction diractionZERO = diraction.NORTH;

        switch (dir) {
            case 0:
                diractionZERO = diraction.SOUTH;
                break;
            case 1:
                diractionZERO = diraction.WEST;
                break;
            case 3:
                diractionZERO = diraction.EAST;
                break;
        }

        BlockPos leftFrontRELATIVEBLOCK = new BlockPos(1, 1, -1);
        BlockPos frontRELATIVEBLOCK = new BlockPos(1, 1, 0);
        BlockPos rightFrontRELATIVEBLOCK = new BlockPos(1, 1, 1);
        BlockPos rightRELATIVEBLOCK = new BlockPos(0, 1, 1);
        BlockPos leftRELATIVEBLOCK = new BlockPos(0, 1, -1);
        BlockPos backRELATIVEBLOCK = new BlockPos(-1, 1, 0);


        Tuple<BlockPos, blocktype> leftfront = getblahblah(leftFrontRELATIVEBLOCK, diractionZERO);
        Tuple<BlockPos, blocktype> front = getblahblah(frontRELATIVEBLOCK, diractionZERO);
        Tuple<BlockPos, blocktype> rightfront = getblahblah(rightFrontRELATIVEBLOCK, diractionZERO);
        Tuple<BlockPos, blocktype> right = getblahblah(rightRELATIVEBLOCK, diractionZERO);
        Tuple<BlockPos, blocktype> left = getblahblah(leftRELATIVEBLOCK, diractionZERO);
        Tuple<BlockPos, blocktype> back = getblahblah(backRELATIVEBLOCK, diractionZERO);

        int timeout = 400;
        try {
            renderingPos = util.unlitaviseCordsWithDications(leftFrontRELATIVEBLOCK, util.getPlayerFeetBlockPos(), diractionZERO);
            chatService.queueCleanChatMessage("leftfront " + String.valueOf(leftfront.getSecond()));
            Thread.sleep(timeout);
            renderingPos = util.unlitaviseCordsWithDications(frontRELATIVEBLOCK, util.getPlayerFeetBlockPos(), diractionZERO);
            chatService.queueCleanChatMessage("front " + String.valueOf(front.getSecond()));
            Thread.sleep(timeout);
            renderingPos = util.unlitaviseCordsWithDications(rightFrontRELATIVEBLOCK, util.getPlayerFeetBlockPos(), diractionZERO);
            chatService.queueCleanChatMessage("rightfront " + String.valueOf(rightfront.getSecond()));
            Thread.sleep(timeout);
            renderingPos = util.unlitaviseCordsWithDications(rightRELATIVEBLOCK, util.getPlayerFeetBlockPos(), diractionZERO);
            chatService.queueCleanChatMessage("right " + String.valueOf(right.getSecond()));
            Thread.sleep(timeout);
            renderingPos = util.unlitaviseCordsWithDications(leftRELATIVEBLOCK, util.getPlayerFeetBlockPos(), diractionZERO);
            chatService.queueCleanChatMessage("left " + String.valueOf(left.getSecond()));
            Thread.sleep(timeout);
            renderingPos = util.unlitaviseCordsWithDications(backRELATIVEBLOCK, util.getPlayerFeetBlockPos(), diractionZERO);
            chatService.queueCleanChatMessage("back " + String.valueOf(back.getSecond()));
            Thread.sleep(timeout);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        chatService.queueCleanChatMessage(isWart(leftfront));
//
//        chatService.queueCleanChatMessage(isWart(front));
//        chatService.queueCleanChatMessage(isWart(rightfront));
//        chatService.queueCleanChatMessage(isWart(right));
//        chatService.queueCleanChatMessage(isWart(left));
//        chatService.queueCleanChatMessage(isWart(back));


        return result;
    }


    private String isWart(Tuple<BlockPos, blocktype> leftfront) {
        return leftfront.getSecond().equals(blocktype.WART) ? String.format("%s is wart", leftfront.getFirst()) : "is not wart";
    }


    static Tuple<BlockPos, blocktype> getblahblah(BlockPos relative, diraction dir) {
        BlockPos real = util.unlitaviseCordsWithDications(relative, util.getPlayerFeetBlockPos(), dir);
        return new Tuple<>(real, getblockatCords(real));
    }

    private static blocktype getblockatCords(BlockPos real) {
        Block block = mc.theWorld.getBlockState(real).getBlock();
        String blockname = block.getRegistryName();
        return blockname.equals("minecraft:nether_wart") ? blocktype.WART : blockname.equals("minecraft:air") ? blocktype.NONE : blockname.equals("minecraft:soul_sand") ? blocktype.SOULSAND : blocktype.SOLID;
    }

    @Override
    public void onWorldRender(RenderWorldLastEvent e) {
        if (renderingPos == null) return;

        Entity viewer = Minecraft.getMinecraft().getRenderViewEntity();
        double viewerX = viewer.lastTickPosX + (viewer.posX - viewer.lastTickPosX) * e.partialTicks;
        double viewerY = viewer.lastTickPosY + (viewer.posY - viewer.lastTickPosY) * e.partialTicks;
        double viewerZ = viewer.lastTickPosZ + (viewer.posZ - viewer.lastTickPosZ) * e.partialTicks;

        int textrgb = 0x3b0c0d;

        GlStateManager.disableDepth();
        GlStateManager.disableCull();
        GlStateManager.disableTexture2D();

//        if (toRenderText != null) {
//
//            hehe.drawTextAtWorld(toRenderText,
//                    renderingPos.getX() + 0.5F,
//                    renderingPos.getY() + 1,
//                    renderingPos.getZ() + 0.5F,
//                    0xffffff, 3F,
//                    true, true, e.partialTicks);
//        }


        double x = renderingPos.getX() - viewerX;
        double y = renderingPos.getY() - viewerY;
        double z = renderingPos.getZ() - viewerZ;
        AxisAlignedBB bb = new AxisAlignedBB(x, y, z, x + 1, y + 1, z + 1);

        hehe.drawFilledBoundingBox(bb, 0.5f, textrgb);


        GlStateManager.disableLighting();
        GlStateManager.enableDepth();
        GlStateManager.enableTexture2D();
    }


}
