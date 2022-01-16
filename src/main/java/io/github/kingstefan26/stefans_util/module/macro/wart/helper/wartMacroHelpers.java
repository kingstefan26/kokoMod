/*
 * Copyright (c) 2021. All copyright reserved
 */

package io.github.kingstefan26.stefans_util.module.macro.wart.helper;

import io.github.kingstefan26.stefans_util.module.macro.macroHelpers;
import io.github.kingstefan26.stefans_util.module.macro.util.util;
import io.github.kingstefan26.stefans_util.module.macro.wart.UniversalWartMacro;
import io.github.kingstefan26.stefans_util.service.impl.keyControlService;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.init.Blocks;
import net.minecraft.util.*;

import java.util.*;
import java.util.stream.Collectors;

import static io.github.kingstefan26.stefans_util.module.macro.util.util.getPlayerFeetBlockPos;
import static io.github.kingstefan26.stefans_util.module.macro.util.util.mc;
import static io.github.kingstefan26.stefans_util.service.impl.keyControlService.action.walk.left;
import static io.github.kingstefan26.stefans_util.service.impl.keyControlService.action.walk.right;

public class wartMacroHelpers implements macroHelpers {
    private static wartMacroHelpers instance;
    private static UniversalWartMacro parent;

    public static wartMacroHelpers getHelper(UniversalWartMacro mparent) {
        if (instance == null) instance = new wartMacroHelpers();
        if (parent == null) parent = mparent;
        return instance;
    }

    /**
     * Transform a multidimensional array into a one-dimensional list.
     *
     * @param array Array (possibly multidimensional).
     * @return a list of all the {@code Object} instances contained in
     * {@code array}.
     */
    public static Object[] flatten(Object[] array) {
        final List<Object> list = new ArrayList<Object>();
        if (array != null) {
            for (Object o : array) {
                if (o instanceof Object[]) {
                    for (Object oR : flatten((Object[]) o)) {
                        list.add(oR);
                    }
                } else {
                    list.add(o);
                }
            }
        }
        return list.toArray();
    }

    /**
     * this function makes it easy to gather blocks around the player in a 1d array
     * for later processing
     *
     * @return a 3d array of tuples that contain the position of the block and its registry name
     */
    public static ArrayList<Tuple<BlockPos, String>> checkBlocksAroundPlayerFLAT() {
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


    public boolean isInFrontTreeWart(Tuple<BlockPos, String>[] blocks) {
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

    public synchronized Tuple<Float, Float> faceBlock(float x, float y, float z) {
        double var4 = x - mc.thePlayer.posX;
        double var8 = z - mc.thePlayer.posZ;
        double var6 = y - mc.thePlayer.posY - 0.5 /*+ entity.height / 1.5*/;
        double var14 = MathHelper.sqrt_double(var4 * var4 + var8 * var8);
        float var12 = (float) (Math.atan2(var8, var4) * 180.0D / Math.PI) - 90.0F;
        float var13 = (float) (-(Math.atan2(var6, var14) * 180.0D / Math.PI));
        float pitch = mc.thePlayer.rotationPitch + MathHelper.wrapAngleTo180_float(var13 - mc.thePlayer.rotationPitch);
        float yaw = mc.thePlayer.rotationYaw + MathHelper.wrapAngleTo180_float(var12 - mc.thePlayer.rotationYaw);
        return new Tuple<>(yaw, pitch);
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

    public keyControlService.action.walk whichWayToGoMockUp(UniversalWartMacro parent, keyControlService.action.walk currentWalkAcction) {
        keyControlService.action.walk result = null;

        switch (currentWalkAcction) {
            case forward:
                break;
            case right:
                result = left;
                break;
            case left:
                result = right;
                break;
            case back:
                break;
            case forwardLeft:
                break;
            case forwardRight:
                break;
            case backLeft:
                break;
            case backRight:
                break;
        }


        return result;
    }

    public keyControlService.action.walk whichWayToGo(UniversalWartMacro parent, keyControlService.action.walk currentWalkAcction) {
        final keyControlService.action.walk result;
        // here we decide what kind of farm this is,
        // there are a two most common types: vertical, horizontal
        // horizontal has two sub types: with tp pad and without, we are gonna target with
        // i only know one vertical design in which we fall so that's a good indicator on macroing
        if (parent.playerFallen && !parent.playerTeleported) {
            // this is called only when the player has sttoped moving and has fallen which indicates two desins: vertical with pad and horisontal
            parent.playerFallen = false; // reset this variable, cuz i dont feel like using a event bus

            // here we check blocks around and see where we can go

            // here is the legend to blocks outputted from the function
            // block 0 = 180 yaw north
            // block 2 = -90 yaw east
            // block 4 =   0 yaw south
            // block 6 =  90 yaw west
            // add number + (8*n) to get n layer
            Tuple<BlockPos, String>[] blocks = parent.helpers.checkBlocksRoundPlayer();

            // legend to this magic piece of math, says what direction we are facing
            // 0 = south
            // 1 = west
            // 2 = north
            // 3 = east
            int direction = MathHelper.floor_double((double)(mc.thePlayer.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;


            // here we take the direction the player is looking, and check if the block in front of us is wart
            // if so we check left (west) and right (east) are solid blocks or air.
            // In this part we know that this is vertical and we go the other way. We also need to take into account that the "player stopped" event
            // could be triggered by a lag spike etc. this is why we also take into consideration the last walk action
            // so if both blocks are air we chose the last walk action
            switch (direction){
                case 0:
                    if(blocks[8].getSecond().equals(Blocks.nether_wart.getRegistryName())
                            && blocks[2].getSecond().equals(Blocks.air.getRegistryName())
                            && !blocks[4].getSecond().equals(Blocks.air.getRegistryName())){
                        // in this scenario we are facing south so the block on the right (east 2) is air and on the left (west 4) is solid,
                        // so we walk right
                        return keyControlService.action.walk.right;
                    } else if (blocks[8].getSecond().equals(Blocks.nether_wart.getRegistryName())
                            && !blocks[2].getSecond().equals(Blocks.air.getRegistryName())
                            && blocks[4].getSecond().equals(Blocks.air.getRegistryName())){
                        // here both are air, so we walk in the direction we used to walk
                        return left;
                    } else if (blocks[8].getSecond().equals(Blocks.nether_wart.getRegistryName())
                            && blocks[2].getSecond().equals(Blocks.air.getRegistryName())
                            && blocks[4].getSecond().equals(Blocks.air.getRegistryName())){
                        // here both are air, so we walk in the direction we used to walk
                        return currentWalkAcction;
                    }
                    break;
                case 1:
                    if(blocks[6].getSecond().equals(Blocks.nether_wart.getRegistryName())
                            && blocks[2].getSecond().equals(Blocks.air.getRegistryName())
                            && !blocks[4].getSecond().equals(Blocks.air.getRegistryName())){
                        // in this scenario we are facing south so the block on the right (east 2) is air and on the left (west 4) is solid,
                        // so we walk right
                        return keyControlService.action.walk.right;
                    } else if(blocks[8].getSecond().equals(Blocks.nether_wart.getRegistryName())
                            && blocks[2].getSecond().equals(Blocks.air.getRegistryName())
                            && blocks[4].getSecond().equals(Blocks.air.getRegistryName())){
                        // here both are air, so we walk in the direction we used to walk
                        return currentWalkAcction;
                    }
                    break;
                case 2:
                    break;
                case 3:
                    break;
            }



        }else if (parent.playerTeleported && parent.playerFallen){
            // this is called only when the player has stopped moving and teleported which indicates vertical with tp pad
            parent.playerTeleported = parent.playerFallen = false;


        }



        return null;
    }

    public void macroWalk(util.walkStates m) {
        if (m == util.walkStates.DEFAULT) {
            return;
        }

        switch (m) {
            case LEFT:
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindRight.getKeyCode(), false);
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindLeft.getKeyCode(), true);
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.getKeyCode(), false);
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindBack.getKeyCode(), false);
                keyControlService.submitCommandASYNC(new keyControlService.simpleCommand(200, left));
                break;
            case RIGHT:
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindRight.getKeyCode(), true);
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindLeft.getKeyCode(), false);
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.getKeyCode(), false);
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindBack.getKeyCode(), false);
                break;
            case TOP:
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindRight.getKeyCode(), false);
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindLeft.getKeyCode(), false);
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.getKeyCode(), true);
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindBack.getKeyCode(), false);
                break;
            case BOTTOM:
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindRight.getKeyCode(), false);
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindLeft.getKeyCode(), false);
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.getKeyCode(), false);
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindBack.getKeyCode(), true);
                break;
            case LEFTTOP:
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindRight.getKeyCode(), false);
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindLeft.getKeyCode(), false);
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.getKeyCode(), true);
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindBack.getKeyCode(), true);
                break;
            case RIGHTTOP:
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindRight.getKeyCode(), true);
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindLeft.getKeyCode(), false);
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.getKeyCode(), true);
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindBack.getKeyCode(), false);
                break;
            case RIGHTBOTTOM:
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindRight.getKeyCode(), true);
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindLeft.getKeyCode(), false);
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.getKeyCode(), false);
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindBack.getKeyCode(), true);
                break;
            case LEFTBOTTOM:
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindRight.getKeyCode(), false);
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindLeft.getKeyCode(), true);
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.getKeyCode(), false);
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindBack.getKeyCode(), true);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + m);
        }
    }


    public Tuple<BlockPos, String>[] checkBlocksRoundPlayer(){
        Tuple<BlockPos, String>[] blocks = new Tuple[16];

        // this could be replaced with iteration in a 3 dimension space but im not doing that to myself
        for (int i = 0; i < 16; i++) {
            BlockPos toCheck;
            switch (i){
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


    /**
     * this function makes it easy to gather blocks around the player in a 3 array
     * for later processing
     * <pre>{@code
     *          for(Tuple<BlockPos, String>[][] arr1 : wartMacroHelpers.checkBlocksAroundPlayer()){
     *             for(Tuple<BlockPos, String>[] arr : arr1){
     *                 for(Tuple<BlockPos, String> block: arr){
     *                     block.getSecond(); // the blocks name
     *                     block.getFirst().getZ(); // the blocks z cord
     *                 }
     *             }
     *         }
     * }</pre>
     * @return a 3d array of tuples that contain the position of the block and its registry name
     */
    public static Tuple<BlockPos, String>[][][] checkBlocksAroundPlayer(){
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

    public void turnHeadToWart() {
        // block 0 = north 180 yaw
        // block 2 = east -90 yaw
        // block 4 = south 0 yaw
        // block 6 = west 90
        // add number*8*n to get n layer
        //←↑→↓


        // WE MOVE FACE IN FRONT OF FACE

        // make sure that if there are move then one wart crops dont try to face them all

        ArrayList<BlockPos> wartBlocks = new ArrayList<>();


        ArrayList<Tuple<BlockPos, String>> flatInt = checkBlocksAroundPlayerFLAT();

        for (Tuple<BlockPos, String> temp : flatInt) {
            if (temp.getSecond().equals("minecraft:nether_wart")) {
                wartBlocks.add(temp.getFirst());
            }
        }

        // here we select the wart block closest to the player the focus on it

        // 0, 90, 180 -90

        // yaw values that face all 4 diractions
        int[] yawValues = new int[]{0, 90, -180, -90};

        if (!wartBlocks.isEmpty()) {
            BlockPos selectedPosition = null;
            // we set smallest to some large value, then we check if the current block is smaller than the
            // current one if yes replace the selected postion.
            // by nature of the loop the smallest position will win
            float smallest = Float.MAX_VALUE;

            BlockPos playerPos = mc.thePlayer.getPosition();

            System.out.println("starting the race");

            for (BlockPos blockpos : wartBlocks) {
                System.out.println("distance bettwen the player and block " + playerPos.distanceSq(blockpos));
                System.out.println("current block pos " + blockpos);
                System.out.println("current smallest " + smallest);

                double value = Math.sqrt(Math.pow((blockpos.getX() - playerPos.getX()), 2) + Math.pow((blockpos.getY() - playerPos.getY()), 2) + Math.pow((blockpos.getZ() - playerPos.getZ()), 2));

                if (value < smallest) {
                    selectedPosition = blockpos;
                }
            }


            System.out.println("selected " + selectedPosition);

            if (selectedPosition != null) {
                float x = (float) ((float) selectedPosition.getX() + 0.5);
                float y = ((float) selectedPosition.getY() - 0.75F);
                float z = (float) ((float) selectedPosition.getZ() + 0.5);


                Tuple<Float, Float> t = parent.helpers.faceBlock(x, y, z);


                // here we find a value closest to the yaw we got from face block
                // we do this so there are only 4 directions that we can face
                List<Integer> list = Arrays.stream(yawValues).boxed().collect(Collectors.toList());

                Float n = t.getFirst();

                int c = list.stream()
                        .min(Comparator.comparingInt(val -> (int) Math.abs(val - n)))
                        .orElseThrow(() -> new NoSuchElementException("No value present"));

                //TODO: YAW RATE SAFE GUARD
                parent.helpers.setPlayerYaw(c);
            }
        }

    }


    //    /**
//     * this hashmap links my blocks that are relative to the player to yaw values to face those blocks
//     * block 0 is the in direct north
//     * block 2 is west
//     * block 4 is south
//     * block 6 is east
//     * inbettwen values are corners
//     * later blocks are the same just diff y values, note we start form bottom down
//     */
//    static HashMap<Integer, Integer> yawToBlockMap = new HashMap<>();
//    static {
//        yawToBlockMap.put(0, 180);
//        yawToBlockMap.put(1, -135);
//        yawToBlockMap.put(2, -90);
//        yawToBlockMap.put(3, -45);
//        yawToBlockMap.put(4, 0);
//        yawToBlockMap.put(5, 45);
//        yawToBlockMap.put(6, 90);
//        yawToBlockMap.put(7, 135);
//    }


}
