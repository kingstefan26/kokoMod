package io.github.kingstefan26.stefans_util.module.debug;

import io.github.kingstefan26.stefans_util.core.module.ModuleManager;
import io.github.kingstefan26.stefans_util.core.module.Module;
import io.github.kingstefan26.stefans_util.module.util.chat;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.entity.Entity;
import net.minecraft.util.*;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import java.util.ArrayList;
import java.util.List;

import static io.github.kingstefan26.stefans_util.util.renderUtil.draw3Dline.draw3DLine;


/**
 * Stolen from Quantizr
 */
public class testRaytrace extends Module {

    public static final Logger logger = LogManager.getLogger("testRaytrace");

    public static final double DEG_TO_RAD = Math.PI / 180.0;
    public static final double RAD_TO_DEG = 180.0 / Math.PI;

    public testRaytrace() {
        super("looking at", "test", ModuleManager.Category.DEBUG);
    }

    @Override
    public void onEnable(){
        raytraceBlocks();
        //this.toggle();
    }

    @Override
    public void onWorldRender(RenderWorldLastEvent e){
        List<Vec3> vecList = vectorsToRaytrace(12);
        Vec3 eyes = new Vec3(mc.thePlayer.posX, mc.thePlayer.posY + (double)mc.thePlayer.getEyeHeight(), mc.thePlayer.posZ);
        for(Vec3 v : vecList){
            draw3DLine(eyes,v, 0xfcba03, 1, false, e.partialTicks);
        }
    }



    void raytraceBlocks() {
        logger.info("Raytracing visible blocks");
        //DungeonRooms.logger.debug("DungeonRooms: Raytracing visible blocks");
        long timeStart = System.nanoTime();

        EntityPlayerSP player = mc.thePlayer;

        List<Vec3> vecList = vectorsToRaytrace(24); //actually creates 24^2 = 576 raytrace vectors

        Vec3 eyes = new Vec3(player.posX, player.posY + (double)player.getEyeHeight(), player.posZ);

        ArrayList<MovingObjectPosition> raytraceResults = new ArrayList<>();

        for (Vec3 vec : vecList) {
            //The super fancy Minecraft built in raytracing function so that the mod only scan line of sight blocks!
            //This is the ONLY place where this mod accesses blocks in the physical map, and they are all within FOV
            MovingObjectPosition raytraceResult = player.getEntityWorld().rayTraceBlocks(eyes, vec, false,false, true);

            if (raytraceResult != null) {
                raytraceResults.add(raytraceResult);
                //the following is filtering out blocks which we don't want for detection, note that these blocks are also line of sight
                if (raytraceResult.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY) {
                    Entity hit = raytraceResult.entityHit;
                    chat.queueClientChatMessage(hit.getName(), chat.chatEnum.DEBUG);
                } else if (raytraceResult.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                    BlockPos raytracedBlockPos = raytraceResult.getBlockPos();
//                if (currentScannedBlocks.contains(raytracedBlockPos)) continue;
//                currentScannedBlocks.add(raytracedBlockPos);
//
//                if (!currentPhysicalSegments.contains(MapUtils.getClosestNWPhysicalCorner(raytracedBlockPos))) {
//                    continue; //scanned block is outside of current room
//                }
//
//                if (RoomDetectionUtils.blockPartOfDoorway(raytracedBlockPos)) {
//                    continue; //scanned block may be part of a corridor
//                }

                    IBlockState hitBlock = mc.theWorld.getBlockState(raytracedBlockPos);
                    int identifier = Block.getIdFromBlock(hitBlock.getBlock()) * 100 + hitBlock.getBlock().damageDropped(hitBlock);

                    Block blockClicked = mc.theWorld.getBlockState(raytracedBlockPos).getBlock();


                    chat.queueClientChatMessage(blockClicked, chat.chatEnum.DEBUG);

//                if (RoomDetectionUtils.whitelistedBlocks.contains(identifier)) {
//                    blocksToCheck.put(raytracedBlockPos, identifier); //will be checked  and filtered in getPossibleRooms()
//                }
                }
            }
        }
        //DungeonRooms.logger.debug("DungeonRooms: Finished raytracing, amount of blocks to check = " + blocksToCheck.size());
        long timeFinish = System.nanoTime();
        logger.info("Time to raytrace and filter: " + (timeFinish - timeStart)  + " nano seconds");
        chat.queueClientChatMessage("found " + raytraceResults.size() + " results", chat.chatEnum.DEBUG);
        raytraceResults.clear();

//        if (futureUpdatePossibleRooms == null && stage2Executor != null && !stage2Executor.isTerminated()) { //start processing in new thread to avoid lag in case of complex scan
//            DungeonRooms.logger.debug("DungeonRooms: Initializing Room Comparison Executor");
//            futureUpdatePossibleRooms = getPossibleRooms();
//        }
    }

    public static List<Vec3> vectorsToRaytrace (int vectorQuantity) {
        //real # of vectors is vectorQuantity^2
        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayerSP player = mc.thePlayer;
        List<Vec3> vectorList = new ArrayList<>();
        //get vector location of player's eyes
        Vec3 eyes = new Vec3(player.posX, player.posY + (double)player.getEyeHeight(), player.posZ);
        float aspectRatio = (float) mc.displayWidth / (float) mc.displayHeight;

        //Vertical FOV: Minecraft FOV setting multiplied by FOV modifier (sprinting, speed effect, etc)
        double fovV = mc.gameSettings.fovSetting * mc.thePlayer.getFovModifier();
        //Horizontal FOV: Thanks Minecraft for being weird and making it this complicated to calculate
        double fovH = Math.atan(aspectRatio * Math.tan(fovV * DEG_TO_RAD / 2)) * 2 * RAD_TO_DEG;

        float verticalSpacing = (float) (fovV * 0.8 / vectorQuantity); // * 0.8 to leave some boundary space
        float horizontalSpacing = (float) (fovH * 0.9 / vectorQuantity); // * 0.9 to leave some boundary space

        float playerYaw = player.rotationYaw;
        float playerPitch = player.rotationPitch;

        if (mc.gameSettings.thirdPersonView == 2) {
            //dumb but easy method of modifying vector direction if player is in reverse 3rd person
            //does not account for the increased 3rd person FOV, but all vectors are within player view so who cares
            playerYaw = playerYaw + 180.0F;
            playerPitch = -playerPitch;
        }

        for (float h = (float) -(vectorQuantity - 1) / 2; h <= (float) (vectorQuantity - 1) / 2; h++) {
            for (float v = (float) -(vectorQuantity - 1) / 2; v <= (float) (vectorQuantity - 1) / 2; v++) {
                float yaw = h * horizontalSpacing;
                float pitch = v * verticalSpacing;

                /*
                yaw and pitch are evenly spread out, but yaw needs to be scaled because MC FOV stretching weird.
                "* ((playerPitch*playerPitch/8100)+1)" because yaw otherwise doesn't get complete scan at higher pitches.
                "/ (Math.abs(v/(vectorQuantity))+1)" because Higher FOVs do not stretch out the corners of the screen as
                much as the rest of the screen, which would otherwise cause corner vectors to be outside FOV
                */
                float yawScaled = yaw  * ((playerPitch*playerPitch/8100)+1) / (Math.abs(v/(vectorQuantity))+1);

                //turn rotation into vector
                Vec3 direction = getVectorFromRotation(yawScaled + playerYaw, pitch + playerPitch);

                //add the new direction vector * 64 (meaning when the vector is raytraced, it will return the first
                // block up to 64 blocks away) to the eyes vector to create the vector which will be raytraced
                vectorList.add(eyes.addVector(direction.xCoord * 64, direction.yCoord * 64, direction.zCoord * 64));
            }
        }
        return vectorList;
    }

    public static Vec3 getVectorFromRotation(float yaw, float pitch) {
        float f = MathHelper.cos(-yaw * (float) DEG_TO_RAD - (float) Math.PI);
        float f1 = MathHelper.sin(-yaw * (float) DEG_TO_RAD - (float) Math.PI);
        float f2 = -MathHelper.cos(-pitch * (float) DEG_TO_RAD);
        float f3 = MathHelper.sin(-pitch * (float) DEG_TO_RAD);
        return new Vec3( f1 * f2, f3, f * f2);
    }
}
