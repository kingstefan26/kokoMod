/*
 * Copyright (c) 2022. All copyright reserved
 */

package io.github.kingstefan26.stefans_util.module.debug;

import io.github.kingstefan26.stefans_util.core.module.moduleFrames.prototypeModule;
import io.github.kingstefan26.stefans_util.module.macro.wart.helper.diraction;
import io.github.kingstefan26.stefans_util.service.impl.chatService;
import io.github.kingstefan26.stefans_util.util.renderUtil.hehe;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Tuple;
import net.minecraftforge.client.event.RenderWorldLastEvent;

import java.util.ArrayList;


public class blockThings extends prototypeModule {
    BlockPos renderingPos = new BlockPos(1, 1, 1);
    String toRenderText = "hi";

    public blockThings() {
        super("test of block thigns");
    }

    public static BlockPos unRelitaviseCords(BlockPos relativeCords, BlockPos playerPos) {
        return new BlockPos(
                playerPos.getX() + relativeCords.getX(),
                playerPos.getY() + relativeCords.getY(),
                playerPos.getZ() + relativeCords.getZ());
    }

    /**
     * @param source the input arraylist
     * @return the source rotated 90 degrees clockwise relative to zeroPoint
     */
    public static ArrayList<Tuple<BlockPos, String>> rotateBlocks90degeresFrom0Point(final ArrayList<Tuple<BlockPos, String>> source) {
        // we iterate tru every block in source
        for (int i = 0, sourceSize = source.size(); i < sourceSize; i++) {
            // get the block
            Tuple<BlockPos, String> block = source.get(i);

            // this could be inlined but better code readability ya know
            int x = block.getFirst().getX() * -1, z = block.getFirst().getZ(), y = block.getFirst().getY();

            // we switch x and z place essentially rotating it 90d in 3d space
            source.set(i, new Tuple<>(new BlockPos(z, y, x), block.getSecond()));
        }
        return source;
    }

    /**
     * @param source the input arraylist
     * @return the source rotated 90 degrees clockwise relative to zeroPoint
     */
    public static ArrayList<Tuple<BlockPos, String>> rotateBlocks90degerescounterblockwise(final ArrayList<Tuple<BlockPos, String>> source) {
        // we iterate tru every block in source
        for (int i = 0, sourceSize = source.size(); i < sourceSize; i++) {
            // get the block
            Tuple<BlockPos, String> block = source.get(i);

            // this could be inlined but better code readability ya know
            int x = block.getFirst().getX(), z = block.getFirst().getZ() * -1, y = block.getFirst().getY();

            // we switch x and z place essentially rotating it 90d in 3d space
            source.set(i, new Tuple<>(new BlockPos(z, y, x), block.getSecond()));
        }
        return source;
    }

    /**
     * @param source the input arraylist
     * @return the source rotated 180 degrees clockwise relative to zeroPoint
     */
    public static ArrayList<Tuple<BlockPos, String>> rotateBlocks180degeresFrom0Point(final ArrayList<Tuple<BlockPos, String>> source) {
        // we iterate tru every block in source
        for (int i = 0, sourceSize = source.size(); i < sourceSize; i++) {
            // get the block
            Tuple<BlockPos, String> block = source.get(i);

            // this could be inlined but better code readability ya know
            int x = block.getFirst().getX() * -1, z = block.getFirst().getZ() * -1, y = block.getFirst().getY();

            // we multiply x and z by -1 essentially rotating it 180d in 3d space
            source.set(i, new Tuple<>(new BlockPos(x, y, z), block.getSecond()));
        }
        return source;
    }

    public static BlockPos getPlayerFeetBlockPos() {
        return new BlockPos((int) mc.thePlayer.posX, (int) mc.thePlayer.posY, (int) mc.thePlayer.posZ);
    }

    public static BlockPos relitaviseCordsWithDications(BlockPos notrelativeCords, BlockPos playerPos, diraction Playerdir) {
        // the relative cords always have east(real) as north(relative) and we turn it around to have it match
        switch (Playerdir) {
            case NORTH:
                return new BlockPos(
                        notrelativeCords.getZ() - playerPos.getZ(),
                        notrelativeCords.getY() - playerPos.getY(),
                        (notrelativeCords.getX() - playerPos.getX()) * -1);
            case SOUTH:
                return new BlockPos(
                        (notrelativeCords.getZ() - playerPos.getZ()) * -1,
                        notrelativeCords.getY() - playerPos.getY(),
                        notrelativeCords.getX() - playerPos.getX());
            case WEST:
                return new BlockPos(
                        (notrelativeCords.getX() - playerPos.getX()) * -1,
                        notrelativeCords.getY() - playerPos.getY(),
                        (notrelativeCords.getZ() - playerPos.getZ()) * -1);
            case EAST:
                return new BlockPos(
                        notrelativeCords.getX() - playerPos.getX(),
                        notrelativeCords.getY() - playerPos.getY(),
                        notrelativeCords.getZ() - playerPos.getZ());
        }
        return null;
    }

    @Override
    protected void PROTOTYPETEST() {
        (new Thread(() -> {
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
                case 2:
                    diractionZERO = diraction.NORTH;
                    break;
                case 3:
                    diractionZERO = diraction.EAST;
                    break;
            }


            ArrayList<Tuple<BlockPos, String>> blocks1 = new ArrayList<>();
            int xShift = -1, yShift = -1, zShift = -2;

            BlockPos feet = getPlayerFeetBlockPos();

            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 4; j++) {
                    for (int k = 0; k < 3; k++) {
                        BlockPos toCheck = unRelitaviseCords(feet, new BlockPos(i + xShift, j + yShift, k + zShift));
                        renderingPos = toCheck;
                        toRenderText = (new BlockPos(i + xShift, j + yShift, k + zShift)).toString();
                        try {
                            Thread.sleep(800);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
//                    System.out.println("relative cords " + new BlockPos(i + xShift, j +yShift , k +zShift).toString() + " block: " + mc.theWorld.getBlockState(toCheck).getBlock().getRegistryName() );

                        blocks1.add(new Tuple<>(toCheck, mc.theWorld.getBlockState(toCheck).getBlock().getRegistryName()));
                    }
                }
            }

            switch (diractionZERO) {
                case NORTH:
                    blocks1 = rotateBlocks90degerescounterblockwise(blocks1);
                    break;
                case SOUTH:
                    blocks1 = rotateBlocks90degeresFrom0Point(blocks1);
                    break;
                case WEST:
                    blocks1 = rotateBlocks180degeresFrom0Point(blocks1);
                    break;
            }


            ArrayList<Tuple<BlockPos, String>> blocks = blocks1;


            for (Tuple<BlockPos, String> block : blocks) {
                if (block.getSecond().equals("minecraft:nether_wart")) {
                    BlockPos therelativecords = relitaviseCordsWithDications(block.getFirst(), getPlayerFeetBlockPos(), diractionZERO);
                    logger.info("relative cords {} unrelative cords {}", therelativecords, block.getFirst());
                    logger.info("found wart at {}", therelativecords);
                    if (therelativecords.getX() == 1 && therelativecords.getY() == 1 && therelativecords.getZ() == -1) {
                        chatService.queueCleanChatMessage("WE HAVE DONE IT GUYS POGGGGG");
                        toRenderText = therelativecords.toString();
                        renderingPos = block.getFirst();
                    }
                }
            }
        })).start();

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

        if (toRenderText != null) {

            hehe.drawTextAtWorld(toRenderText,
                    renderingPos.getX() + 0.5F,
                    renderingPos.getY() + 1,
                    renderingPos.getZ() + 0.5F,
                    0xffffff, 3F,
                    true, true, e.partialTicks);
        }


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
