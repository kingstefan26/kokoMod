/*
 * Copyright (c) 2021. All copyright reserved
 */

package io.github.kingstefan26.stefans_util.module.macro.wart.routines;

import io.github.kingstefan26.stefans_util.core.module.ModuleMenagers.moduleRegistery;
import io.github.kingstefan26.stefans_util.core.module.moduleFrames.basicModule;
import io.github.kingstefan26.stefans_util.main;
import io.github.kingstefan26.stefans_util.module.macro.macroRoutines;
import io.github.kingstefan26.stefans_util.module.macro.macroStates;
import io.github.kingstefan26.stefans_util.module.macro.util.util;
import io.github.kingstefan26.stefans_util.module.macro.wart.UniversalWartMacro;
import io.github.kingstefan26.stefans_util.module.macro.wart.helper.wartMacroHelpers;
import io.github.kingstefan26.stefans_util.service.impl.WorldInfoService;
import io.github.kingstefan26.stefans_util.service.impl.chatService;
import io.github.kingstefan26.stefans_util.service.impl.inputLockerService;
import io.github.kingstefan26.stefans_util.service.impl.keyControlService;
import io.github.kingstefan26.stefans_util.util.renderUtil.draw3Dline;
import io.github.kingstefan26.stefans_util.util.renderUtil.drawCenterString;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraft.util.Vec3;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;

import static io.github.kingstefan26.stefans_util.module.macro.macroStates.*;
import static io.github.kingstefan26.stefans_util.service.impl.keyControlService.action.walk.right;

public class wartMacroRoutines implements macroRoutines {
    private static wartMacroRoutines instance;
    private static UniversalWartMacro parent;

    public static wartMacroRoutines getRoutines(UniversalWartMacro mparent) {
        if(instance == null) instance = new wartMacroRoutines();
        if(parent == null) parent = mparent;
        return instance;
    }

    static Minecraft mc = Minecraft.getMinecraft();
    Logger logger = LogManager.getLogger("wartMacroRoutines");


    boolean sprintStatus;
    boolean lastLeftOffStaus;
    boolean stolenFarmOverlayStatus;
    boolean autorecconectStatus;


    @Override
    public void enableMacroRoutine() {
        // TODO this also needs a face lift
        if (parent.localDecoratorManager.keyBindDecorator.keybind.getKeyCode() == 0) {
            chatService.queueCleanChatMessage(UniversalWartMacro.chatprefix + "please set a keybinding!");
            parent.toggle();
            return;
        }
        if (!main.debug) {
            if (!WorldInfoService.isOnPrivateIsland()) {
                chatService.queueClientChatMessage("please join a your island!", chatService.chatEnum.CHATPREFIX);
                parent.setToggled(false);
                return;
            }
        }
        for (basicModule m : moduleRegistery.getModuleRegistery().loadedModules) {
            if ("autorecorrect".equals(m.getName())) {
                if(!mc.isSingleplayer()){
                    autorecconectStatus = m.isToggled();
                    m.setToggled(true);
                }
            }
            if ("Sprint".equals(m.getName())) {
                sprintStatus = m.isToggled();
                m.setToggled(true);
            }
            if ("lastLeftOff".equals(m.getName())) {
                lastLeftOffStaus = m.isToggled();
                m.setToggled(true);
            }
            if ("stolenoverlay!".equals(m.getName())) {
                stolenFarmOverlayStatus = m.isToggled();
                m.setToggled(true);
            }
        }


        mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("stefan_util:annoying"), 1.0F));

        parent.fallCounter = 0;


//        String serverId = UUID.randomUUID().toString().replace("-", "");
//
//        try {
//            Minecraft.getMinecraft().getSessionService().joinServer(mc.getSession().getProfile(), mc.getSession().getToken(), serverId);
//        } catch (Exception var5) {
//            return;
//        }


        if (parent.experimentalGuiFlag) {
            if (parent.macroMenu == null) parent.macroMenu = new util.macroMenu(parent);
            parent.guiCloseGrace = true;
        }

        mc.displayGuiScreen(null);
        chatService.queueCleanChatMessage(UniversalWartMacro.chatprefix + "enabled wart macro");
        chatService.queueCleanChatMessage(UniversalWartMacro.chatprefix + "note: please move the the start of your farm");
        parent.state.setState(RECALIBRATING);
    }


    @Override
    public void startingFromLastLeftOffRoutine() {
        // TODO IMPREMENT THIS GOD DAMMIT
//        if(lastLeftOff.getLastleftoffObject() != null) {
//            macroWalkStage = walkStates.getLastleftoffObject().getMacroStage();
//        }else{
//            macroWalkStage = walkStates.RIGHT;
//        }
    }

    @Override
    public void continuousChecks() {
        if (mc == null || mc.theWorld == null || mc.thePlayer == null) return;
        parent.playerSpeed = mc.thePlayer.getDistance(mc.thePlayer.lastTickPosX, mc.thePlayer.lastTickPosY, mc.thePlayer.lastTickPosZ);
        if (mc.thePlayer.posY - mc.thePlayer.lastTickPosY < 0) {
            parent.playerFallCallBack();
        }
    }

    @Override
    public void continuousRender(Float partialTicks) {
        // TODO just dont do useless things please
        Entity viewer = mc.getRenderViewEntity();
        double viewerX = viewer.lastTickPosX + (viewer.posX - viewer.lastTickPosX) * partialTicks;
        double viewerY = viewer.lastTickPosY + (viewer.posY - viewer.lastTickPosY) * partialTicks;
        double viewerZ = viewer.lastTickPosZ + (viewer.posZ - viewer.lastTickPosZ) * partialTicks;

        GlStateManager.disableCull();

        draw3Dline.draw3DLine(new Vec3(viewerX, viewerY, viewerZ),
                new Vec3(viewerX, viewerY + 1, viewerZ),
                0xffffff,
                5,
                false,
                partialTicks);

        GlStateManager.enableCull();
    }

    public keyControlService.action.walk currentWalkAction = right;

    @Override
    public void checkMacroConditionsRoutine() {
        // TODO OLD OUTDATED CHECKS
        parent.playerYaw = Math.round(wartMacroHelpers.getYaw());
        parent.playerPitch = Math.round(mc.thePlayer.rotationPitch);


        if (parent.playerPitch == parent.wantedPitch) parent.state.setState(macroStates.MACROING);
        if(parent.state.checkState(MACROING)){
//            shouldLoop = true;
            parent.routines.preMacroingRoutine();
        }
    }

    @Override
    public void recalibratingDisableKeyLisiner(int key) {
        if(key == parent.localDecoratorManager.keyBindDecorator.keybind.getKeyCode()) parent.state.setState(STOPPING);
    }

    @Override
    public void preMacroingRoutine() {
        chatService.queueClientChatMessage(UniversalWartMacro.chatprefix + "started macring");
        //lastLeftOff.nullLastLeftOff();
        if (parent.experimentalGuiFlag) {
            parent.guiCloseGrace = false;
            mc.displayGuiScreen(parent.macroMenu); // TODO fix the macro menu not clicking things
        }
        // TODO make so this thing pauses the game
        inputLockerService.lock(parent.localDecoratorManager.keyBindDecorator.keybind.getKeyCode(), () -> parent.routines.togglePause());
    }

    @Override
    public void doMacroRenderRoutine() {
        // TODO: add "pathfind" to find urself a way tru the farm (very far fetched)
        drawCenterString.GuiNotif(Minecraft.getMinecraft(), "Whats good korea");
        drawCenterString.drawCenterStringOnScreenLittleToDown(Minecraft.getMinecraft(), "press key "
                + Keyboard.getKeyName(parent.localDecoratorManager.keyBindDecorator.keybind.getKeyCode()) +
                " to stop", "ff002f");
    }

    @Override
    public void checkMacroConditionsRenderRoutine(float partialTicks) {
        if (mc.thePlayer == null || mc.theWorld == null) return;
        // TODO lets break this up into smaller pieces
        // TODO implement verbose logging support
        drawCenterString.GuiNotif(mc,
                "macro will start when you lock your head position on the right angle");

        long start = System.nanoTime();

        // WE GET THE BLOCKS AROUND PLAYER


//        // block 0 = north 180 yaw
//        // block 2 = east -90 yaw
//        // block 4 = south 0 yaw
//        // block 6 = west 90
//        // add number*8*n to get n layer
//        //←↑→↓
        Tuple<BlockPos, String>[] blocks = parent.helpers.checkBlocksRoundPlayer();
//
//
//
//        // WE MOVE FACE IN FRONT OF FACE
//
//        // make sure that if there are move then one wart crops dont try to face them all
//
//        for (int i = 0; i < 16; i++) {
//
//            Tuple<BlockPos, String> temp = blocks[i];
//
//            ArrayList<BlockPos> wartBlocks = new ArrayList<>();
//
//            if(temp.getSecond().equals("minecraft:nether_wart")){
//
//                if(parent.verboseLogging) logger.info("block " + i + " is wart");
//
//                wartBlocks.add(temp.getFirst());
//            }
//
//
//            // here we select the wart block closest to the player the focus on it
//
//            if(!wartBlocks.isEmpty()){
//                BlockPos selectedPosition = null;
//                // we set smallest to some large value, then we check if the current block is smaller then the current one
//                // if yes replace the selected postion. by nature the smallest postition will win
//
//
//                Entity viewer = Minecraft.getMinecraft().getRenderViewEntity();
//                double viewerX = viewer.lastTickPosX + (viewer.posX - viewer.lastTickPosX) * partialTicks;
//                double viewerY = viewer.lastTickPosY + (viewer.posY - viewer.lastTickPosY) * partialTicks;
//                double viewerZ = viewer.lastTickPosZ + (viewer.posZ - viewer.lastTickPosZ) * partialTicks;
//
//                float smallest = 0F;
//
//                for(BlockPos blockpos: wartBlocks){
//                    float xx = blockpos.getX() -(float) viewerX;
//                    float yy = blockpos.getY() -(float) viewerY;
//                    float zz = blockpos.getZ() -(float) viewerZ;
//
//                    float distSq = xx*xx+ yy*yy + zz*zz;
//                    if(distSq > smallest){
//                        selectedPosition = blockpos;
//                    }
//                }
//
//                if(selectedPosition != null){
//                    float x = (float) ((float) selectedPosition.getX() + 0.5);
//                    float y = ((float) selectedPosition.getY() - 0.75F);
//                    float z = (float) ((float) selectedPosition.getZ() + 0.5);
//
//
//                    Tuple<Float, Float> t = parent.helpers.faceBlock(x, y, z);
//
//                    //TODO: YAW RATE SAFE GUARD
//                    parent.helpers.altsetPlayerYaw(t.getFirst());
//                }
//            }
//
//
//        }

        parent.helpers.turnHeadToWart();


        // WE DISPLAY THE STPUDID STRING

        drawCenterString.drawCenterStringOnScreenLittleToDown(mc,
                " Current Pitch: " + parent.playerPitch + " / " + parent.wantedPitch +
                        "Is mouse over wart " + wartMacroHelpers.isPlayerLookingAtBlock("minecraft:nether_wart") + " is facing wart " + parent.helpers.isInFrontTreeWart(blocks),
                "ff002f");


        if (parent.verboseLogging) logger.info("finished raytracing/getting bloksc in " + (System.nanoTime() - start));
    }

    boolean dontSpamFlag = true;

    @Override
    public void doMacroRoutine() {
        if (dontSpamFlag) {
            dontSpamFlag = false;
            keyControlService.submitCommandASYNC(new keyControlService.command(currentWalkAction, () -> {
                final keyControlService.action.walk result = parent.helpers.whichWayToGoMockUp(parent, currentWalkAction);
                if (result != null) {
                    currentWalkAction = result;
                } else {
                    currentWalkAction = right;
                }
                dontSpamFlag = true;
            }));
        }
    }

    @Override
    public void doContinuousMacroChecks() {
        if(mc.theWorld == null || mc.thePlayer == null){
            parent.state.setState(AUTONOMOUS_RECALIBRATING);
        }

        // TODO: update walk routine CHECK IF THE PLAYER IS GETTING CROPS AND IF NOT RECALIBRATE

        if(!wartMacroHelpers.isPlayerLookingAtBlock("minecraft:nether_wart")){
            parent.state.setState(AUTONOMOUS_RECALIBRATING);
        }
        if(false /* here we check if we are still collecting wart TODO: imprement checing if player is still collectiign wart with a resonable temporal shift */ ){
            parent.state.setState(AUTONOMOUS_RECALIBRATING);
        }


        parent.state.setState(MACROING);
    }


    long ExpireTimeSpamp = 0;

    boolean autonomous_recalibration_moveheadtowartflag;

    @Override
    public void recalibrateMacroRoutine() {
        if(main.debug){
            if (mc != null && mc.theWorld != null && !mc.isSingleplayer()) {
                if(mc.getCurrentServerData() != null){
                    chatService.queueCleanChatMessage("in this part we /warp skyblock and shii");
                    parent.state.setState(MACROING);
                }
            }
        // #not debug #not my problem #annoying
        } else {
            if(ExpireTimeSpamp != 0){
                if(System.currentTimeMillis() >= ExpireTimeSpamp) {
                    ExpireTimeSpamp = 0;
                }
            }

            if (ExpireTimeSpamp == 0 && mc != null && mc.theWorld != null && !mc.isSingleplayer() && WorldInfoService.isOnHypixel() ) {
                if(mc.getCurrentServerData() != null){

                    boolean inSB = WorldInfoService.isInSkyblock(), onIS = WorldInfoService.isOnPrivateIsland();

                    if(!onIS && !inSB){
                        chatService.sendMessage("/play skyblock");

                        ExpireTimeSpamp = System.currentTimeMillis() + 5000;
                    }

                    if(inSB && !onIS){
                        chatService.sendMessage("/warp island");

                        ExpireTimeSpamp = System.currentTimeMillis() + 5000;
                    }

                    if(inSB && onIS){
                        // we use the fact that this is set once to not spam chat smh
                        if(autonomous_recalibration_moveheadtowartflag){
                            chatService.queueCleanChatMessage(UniversalWartMacro.chatprefix + "horray we got the the private island");
                            chatService.queueCleanChatMessage(UniversalWartMacro.chatprefix + "now time to turn our head to the wart smh");
                        }


                        autonomous_recalibration_moveheadtowartflag = true;

                        if(parent.helpers.isInFrontTreeWart(parent.helpers.checkBlocksRoundPlayer()) && parent.helpers.isPlayerLookingAtBlock("minecraft:nether_wart")){
                            chatService.queueCleanChatMessage(UniversalWartMacro.chatprefix + "yay we are facing 3 wart and out mouse is over wart time to macro again");
                            autonomous_recalibration_moveheadtowartflag = false;
                            parent.state.setState(MACROING);
                        }

                    }

                }
            }
        }
        // locate urself

    }

    @Override
    public void recalibrateMacroRenderRoutine() {
        drawCenterString.GuiNotif(Minecraft.getMinecraft(), "autonomous recalibration...");
        if(autonomous_recalibration_moveheadtowartflag){
            parent.helpers.turnHeadToWart();
        }
    }

    @Override
    public void togglePause() {
        parent.state.setState(PAUSED);
        this.dontSpamFlag = true;
        parent.routines.macroPauseRoutine();
    }

    @Override
    public void macroPauseRoutine() {
        // TODO create pause in macro
        KeyBinding.unPressAllKeys();

        keyControlService.clearCommandQueue();
    }

    @Override
    public void macroPauseRenderRoutine() {
        // TODO create rendering on pause
        if (mc.currentScreen != null) return;
        drawCenterString.GuiNotif(Minecraft.getMinecraft(), "macro paused");
        drawCenterString.drawCenterStringOnScreenLittleToDown(Minecraft.getMinecraft(), "press key "
                + Keyboard.getKeyName(parent.localDecoratorManager.keyBindDecorator.keybind.getKeyCode()) +
                " again to disable the macro, press "+ Keyboard.getKeyName(parent.unpauseKey)+" to start again", "ff002f");
    }

    @Override
    public void doContinuousPauseChecks(int key) {
        if(key == parent.localDecoratorManager.keyBindDecorator.keybind.getKeyCode()) parent.routines.disableMacroRoutine();
        if(key == parent.unpauseKey) parent.state.setState(RECALIBRATING);
    }


    @Override
    public void disableMacroRoutine() {
        parent.state.setState(STOPPING);
        // TODO this needs major a face lift
        mc.displayGuiScreen(null);

        chatService.queueCleanChatMessage(UniversalWartMacro.chatprefix + "disabled wart macro");

        parent.playerYaw = 0;
        parent.playerPitch = 0;

        KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.getKeyCode(), false);
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindBack.getKeyCode(), false);
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindRight.getKeyCode(), false);
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindLeft.getKeyCode(), false);
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindAttack.getKeyCode(), false);
        parent.macroWalkStage = util.walkStates.BOTTOM;
        this.dontSpamFlag = true;


        for (basicModule m : moduleRegistery.getModuleRegistery().loadedModules) {
            if ("autorecorrect".equals(m.getName())) {
                if(!mc.isSingleplayer()){
                    m.setToggled(autorecconectStatus);
                }
            }
            if ("Sprint".equals(m.getName())) {
                m.setToggled(sprintStatus);
            }
            if ("lastLeftOff".equals(m.getName())) {
                m.setToggled(lastLeftOffStaus);
            }
            if ("stolenoverlay!".equals(m.getName())) {
                m.setToggled(stolenFarmOverlayStatus);
            }
        }



//        lastLeftOff.getLastLeftOff().registerLastLeftOff(
//                new lastleftoffObject((float)mc.thePlayer.posX,
//                        (float) mc.thePlayer.posY,
//                        (float)mc.thePlayer.posZ,
//                        cropType.WART,
//                        walkStates,
//                        System.currentTimeMillis()));
        parent.state.setState(IDLE);
        parent.onDisable();
    }
}
