/*
 * Copyright (c) 2021. All copyright reserved
 */

package io.github.kingstefan26.stefans_util.module.macro.wart.helper;

import io.github.kingstefan26.stefans_util.module.macro.util.util;
import io.github.kingstefan26.stefans_util.module.macro.wart.UniversalWartMacro;
import io.github.kingstefan26.stefans_util.service.impl.keyControlService;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.util.*;

import java.util.ArrayList;

import static io.github.kingstefan26.stefans_util.module.macro.util.util.getPlayerFeetBlockPos;
import static io.github.kingstefan26.stefans_util.module.macro.util.util.mc;

public class wartMacroUtil {
    private static wartMacroUtil instance;
    private static UniversalWartMacro parent;

    public static wartMacroUtil getHelper(UniversalWartMacro mparent) {
        if (instance == null) instance = new wartMacroUtil();
        if (parent == null) parent = mparent;
        return instance;
    }


    /**
     * this function makes it easy to gather blocks around the player in a 1d array
     * for later processing
     *
     * @return a 3d array of tuples that contain the position of the block and its registry name
     */
    public static ArrayList<Tuple<BlockPos, String>> checkBlocksAroundPlayerFLAT() {
        ArrayList<Tuple<BlockPos, String>> blocks = new ArrayList<>();
        int xShift = -1, yShift = -1, zShift = -2;
        BlockPos feet = getPlayerFeetBlockPos();
        int xOffset = feet.getX() < 0 ? -1 : 0;
        xShift += xOffset;


        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                for (int k = 0; k < 3; k++) {
                    BlockPos toCheck = util.unRelitaviseCords(feet, new BlockPos(i + xShift, j + yShift, k + zShift));
//                    System.out.println("relative cords " + new BlockPos(i + xShift, j +yShift , k +zShift).toString() + " block: " + mc.theWorld.getBlockState(toCheck).getBlock().getRegistryName() );

                    blocks.add(new Tuple<>(toCheck, mc.theWorld.getBlockState(toCheck).getBlock().getRegistryName()));
                }
            }
        }
        return blocks;
    }


    /**
     * this function makes it easy to gather blocks around the player in a 1d array
     * for later processing
     *
     * @return a 3d array of tuples that contain the position of the block and its registry name
     */
    public static ArrayList<Tuple<BlockPos, String>> checkBlocksAroundPlayerFlatRotatedToPlayerDirection(diraction dir) {
        ArrayList<Tuple<BlockPos, String>> blocks = new ArrayList<>();
        int xShift = -1, yShift = -1, zShift = -1;

        BlockPos feet = getPlayerFeetBlockPos();

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                for (int k = 0; k < 3; k++) {
                    BlockPos toCheck = util.unRelitaviseCords(feet, new BlockPos(i + xShift, j + yShift, k + zShift));
//                    System.out.println("relative cords " + new BlockPos(i + xShift, j +yShift , k +zShift).toString() + " block: " + mc.theWorld.getBlockState(toCheck).getBlock().getRegistryName() );

                    blocks.add(new Tuple<>(toCheck, mc.theWorld.getBlockState(toCheck).getBlock().getRegistryName()));
                }
            }
        }

        switch (dir) {
            case NORTH:
                blocks = rotateBlocks90degerescounterblockwise(blocks);
                break;
            case SOUTH:
                blocks = rotateBlocks90degeresFrom0Point(blocks);
                break;
            case WEST:
                blocks = rotateBlocks180degeresFrom0Point(blocks);
                break;
        }


        return blocks;
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


    public static boolean isInFrontTreeWart(Tuple<BlockPos, String>[] blocks) {
        // 0 = soutfh
        // 1 = west
        // 2 = noth
        // 3 = east
        int diraction = MathHelper.floor_double((double) (mc.thePlayer.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;

        // we get the numbers of the 3 blocks that are in front the player based on the direction they are facing
        int[] blocksToCheck;
        switch (diraction) {
            case 0:
                blocksToCheck = new int[]{11, 12, 13};
                break;
            case 1:
                blocksToCheck = new int[]{13, 14, 15};
                break;
            case 2:
                blocksToCheck = new int[]{15, 8, 9};
                break;
            case 3:
                blocksToCheck = new int[]{9, 10, 11};
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + diraction);
        }

        int threeWartInFrontOfFaceCounter = 0;
        for (int blocktoCheck : blocksToCheck) {
            if (blocks[blocktoCheck].getSecond().equals("minecraft:nether_wart")) threeWartInFrontOfFaceCounter++;
        }

        return threeWartInFrontOfFaceCounter == 3;
    }

    public static synchronized Tuple<Float, Float> faceBlock(float x, float y, float z) {
        double var4 = x - mc.thePlayer.posX + 0.5;
        double var8 = z - mc.thePlayer.posZ + 0.5;
        double var6 = y - mc.thePlayer.posY - 0.1 /*+ entity.height / 1.5*/;
        double var14 = MathHelper.sqrt_double(var4 * var4 + var8 * var8);
        float var12 = (float) (Math.atan2(var8, var4) * 180.0D / Math.PI) - 90.0F;
        float var13 = (float) (-(Math.atan2(var6, var14) * 180.0D / Math.PI));
        float pitch = mc.thePlayer.rotationPitch + MathHelper.wrapAngleTo180_float(var13 - mc.thePlayer.rotationPitch);
        float yaw = mc.thePlayer.rotationYaw + MathHelper.wrapAngleTo180_float(var12 - mc.thePlayer.rotationYaw);
        return new Tuple<>(yaw, pitch);
    }


    public static int findClosest(int[] arr, int target) {
        int idx = 0;
        int dist = Math.abs(arr[0] - target);

        for (int i = 1; i < arr.length; i++) {
            int cdist = Math.abs(arr[i] - target);

            if (cdist < dist) {
                idx = i;
                dist = cdist;
            }
        }

        return arr[idx];
    }

    public static boolean isPlayerLookingAtBlock(String blockRegistryName) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.objectMouseOver == null) return false;

        if (mc.objectMouseOver.typeOfHit != MovingObjectPosition.MovingObjectType.BLOCK) return false;

        Vec3 blockVector = mc.objectMouseOver.hitVec;

        double bX = blockVector.xCoord;
        double bY = blockVector.yCoord;
        double bZ = blockVector.zCoord;
        double pX = mc.thePlayer.posX;
        double pY = mc.thePlayer.posY;
        double pZ = mc.thePlayer.posZ;

        if (bX == Math.floor(bX) && bX <= pX) bX--;
        // +1 on Y to get y from player eyes instead of feet
        if (bY == Math.floor(bY) && bY <= pY + 1) bY--;
        if (bZ == Math.floor(bZ) && bZ <= pZ) bZ--;

        try {
            Block block = mc.theWorld.getBlockState(new BlockPos(bX, bY, bZ)).getBlock();
            return block.getRegistryName().equals(blockRegistryName);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return false;
    }


    public static float getYaw() {
        float yaw = mc.thePlayer.rotationYawHead;
        yaw %= 360;
        if (yaw < 0) yaw += 360;
        if (yaw > 180) yaw -= 360;
        return yaw;
    }

    public static float lerpAngle(float fromRadians, float toRadians, float progress) {
        float f = ((toRadians - fromRadians) % 360.0F + 540.0F) % 360.0F - 180.0F;
        return fromRadians + f * progress % 360.0F;
    }

    public static float altlerpAngle(float fromRadians, float toRadians, float progress) {
        float f = ((toRadians - fromRadians) % 360.0F + 540.0F) % 360.0F - 180.0F;
        return fromRadians + f - progress % 360.0F;
    }

    public void setPlayerRotations(float yaw, float pitch) {
        mc.thePlayer.rotationYaw = lerpAngle(mc.thePlayer.rotationYaw, yaw, parent.rateOfChange);
        mc.thePlayer.rotationPitch = lerpAngle(mc.thePlayer.rotationPitch, pitch, parent.rateOfChange);
    }


    public void altsetPlayerRotations(float yaw, float pitch) {
        mc.thePlayer.rotationYaw = altlerpAngle(mc.thePlayer.rotationYaw, yaw, parent.rateOfChange);
        mc.thePlayer.rotationPitch = altlerpAngle(mc.thePlayer.rotationPitch, pitch, parent.rateOfChange);
    }

    public void setPlayerYaw(float yaw) {
        mc.thePlayer.rotationYaw = lerpAngle(mc.thePlayer.rotationYaw, yaw, parent.rateOfChange);
    }

    public void altsetPlayerYaw(float yaw) {
        mc.thePlayer.rotationYaw = altlerpAngle(mc.thePlayer.rotationYaw, yaw, parent.rateOfChange);
    }

    public void setPlayerpitch(float pitch) {
        mc.thePlayer.rotationPitch = lerpAngle(mc.thePlayer.rotationPitch, pitch, parent.rateOfChange);
    }

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


//        chatService.queueCleanChatMessage(isWart(leftfront));
//
//        chatService.queueCleanChatMessage(isWart(front));
//        chatService.queueCleanChatMessage(isWart(rightfront));
//        chatService.queueCleanChatMessage(isWart(right));
//        chatService.queueCleanChatMessage(isWart(left));
//        chatService.queueCleanChatMessage(isWart(back));

        // vertical farms always have wart in front of face
        if (front.getSecond().equals(blocktype.WART)) {
            if (leftfront.getSecond().equals(blocktype.WART)) {
                if (left.getSecond().equals(blocktype.NONE)) {
                    result = keyControlService.action.walk.left;
                }
            } else if (rightfront.getSecond().equals(blocktype.WART)) {
                if (right.getSecond().equals(blocktype.NONE)) {
                    result = keyControlService.action.walk.right;
                }
            }
        } else if (front.getSecond().equals(blocktype.NONE)) {
            // if the block in front is none then there is a 99% chance that this is a horisontal
            if (left.getSecond().equals(blocktype.NONE)) {
                if (leftfront.getSecond().equals(blocktype.WART)) {
                    result = keyControlService.action.walk.forwardRight;
                } else {
                    result = keyControlService.action.walk.forward;
                }
            }
            if (right.getSecond().equals(blocktype.NONE)) {
                if (rightfront.getSecond().equals(blocktype.WART)) {
                    result = keyControlService.action.walk.forwardLeft;
                } else {
                    result = keyControlService.action.walk.forward;
                }
            }
        }


        return result;
    }


    private static diraction getCurrentDirection() {
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
        return diractionZERO;
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

    public static Tuple<BlockPos, String>[] checkBlocksRoundPlayer() {
        Tuple<BlockPos, String>[] blocks = new Tuple[16];

        // this could be replaced with iteration in a 3 dimension space but im not doing that to myself
        for (int i = 0; i < 16; i++) {
            BlockPos toCheck;
            switch (i) {
                case 0:
                    toCheck = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ - 1);
                    break;
                case 1:
                    toCheck = new BlockPos(mc.thePlayer.posX + 1, mc.thePlayer.posY, mc.thePlayer.posZ - 1);
                    break;
                case 2:
                    toCheck = new BlockPos(mc.thePlayer.posX + 1, mc.thePlayer.posY, mc.thePlayer.posZ);
                    break;

                case 3:
                    toCheck = new BlockPos(mc.thePlayer.posX + 1, mc.thePlayer.posY, mc.thePlayer.posZ + 1);
                    break;

                case 4:
                    toCheck = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ + 1);
                    break;

                case 5:
                    toCheck = new BlockPos(mc.thePlayer.posX - 1, mc.thePlayer.posY, mc.thePlayer.posZ + 1);
                    break;

                case 6:
                    toCheck = new BlockPos(mc.thePlayer.posX - 1, mc.thePlayer.posY, mc.thePlayer.posZ);
                    break;

                case 7:
                    toCheck = new BlockPos(mc.thePlayer.posX - 1, mc.thePlayer.posY, mc.thePlayer.posZ - 1);
                    break;

                // here we start the second layer of blocks
                case 8:
                    toCheck = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY + 1, mc.thePlayer.posZ - 1);
                    break;
                case 9:
                    toCheck = new BlockPos(mc.thePlayer.posX + 1, mc.thePlayer.posY + 1, mc.thePlayer.posZ - 1);
                    break;
                case 10:
                    toCheck = new BlockPos(mc.thePlayer.posX + 1, mc.thePlayer.posY + 1, mc.thePlayer.posZ);
                    break;
                case 11:
                    toCheck = new BlockPos(mc.thePlayer.posX + 1, mc.thePlayer.posY + 1, mc.thePlayer.posZ + 1);
                    break;
                case 12:
                    toCheck = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY + 1, mc.thePlayer.posZ + 1);
                    break;
                case 13:
                    toCheck = new BlockPos(mc.thePlayer.posX - 1, mc.thePlayer.posY + 1, mc.thePlayer.posZ + 1);
                    break;
                case 14:
                    toCheck = new BlockPos(mc.thePlayer.posX - 1, mc.thePlayer.posY + 1, mc.thePlayer.posZ);
                    break;
                case 15:
                    toCheck = new BlockPos(mc.thePlayer.posX - 1, mc.thePlayer.posY + 1, mc.thePlayer.posZ - 1);
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + i);
            }
            blocks[i] = new Tuple<>(toCheck, mc.theWorld.getBlockState(toCheck).getBlock().getRegistryName());
        }

        return blocks;
    }

    public static void turnHeadToWart() {
        ArrayList<Tuple<BlockPos, String>> klocks = checkBlocksAroundPlayerFLAT();

        int shaneYaw = -1;

        for (int i = 0, klocksSize = klocks.size(); i < klocksSize; i++) {
            Tuple<BlockPos, String> klock = klocks.get(i);
            if (klock.getSecond().equals("minecraft:nether_wart")) {
                System.out.println("klock " + i + " is wart");
                if (i == 20) shaneYaw = 0;
                if (i == 18) shaneYaw = 180;
                if (i == 31) shaneYaw = -90;
                if (i == 7) shaneYaw = 90;
            }
        }

        int[] yawValues = new int[]{1, 90, 180, -180, -90};

        if (shaneYaw != -1) {
            parent.helpers.setPlayerYaw(shaneYaw);

        }
    }


    /**
     * this function makes it easy to gather blocks around the player in a 3 array
     * for later processing
     *
     * @return a 3d array of tuples that contain the position of the block and its registry name
     */
    public static Tuple<BlockPos, String>[][][] checkBlocksAroundPlayer() {
        Tuple<BlockPos, String>[][][] blocks = new Tuple[3][4][3];
        int xShift = -1, yShift = -1, zShift = -1;

        BlockPos feet = getPlayerFeetBlockPos();

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                for (int k = 0; k < 3; k++) {
                    BlockPos toCheck = util.unRelitaviseCords(feet, new BlockPos(i + xShift, j +yShift , k +zShift));
                    System.out.println("relative cords " + new BlockPos(i + xShift, j +yShift , k +zShift).toString() + " block: " + mc.theWorld.getBlockState(toCheck).getBlock().getRegistryName());

                    blocks[i][j][k] = new Tuple<>(toCheck, mc.theWorld.getBlockState(toCheck).getBlock().getRegistryName());
                }
            }
        }


        return blocks;
    }

    private String isWart(Tuple<BlockPos, blocktype> leftfront) {
        return leftfront.getSecond().equals(blocktype.WART) ? String.format("%s is wart", leftfront.getFirst()) : "is not wart";
    }




}
