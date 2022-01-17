package io.github.kingstefan26.stefans_util.module.macro.wart;

import io.github.kingstefan26.stefans_util.core.module.ModuleMenagers.moduleManager;
import io.github.kingstefan26.stefans_util.core.module.ModuleMenagers.moduleRegistery;
import io.github.kingstefan26.stefans_util.core.module.moduleDecorators.impl.keyBindDecorator;
import io.github.kingstefan26.stefans_util.core.module.moduleFrames.basicModule;
import io.github.kingstefan26.stefans_util.core.setting.impl.CheckSetting;
import io.github.kingstefan26.stefans_util.core.setting.impl.ChoseAKeySetting;
import io.github.kingstefan26.stefans_util.core.setting.impl.SliderNoDecimalSetting;
import io.github.kingstefan26.stefans_util.core.setting.impl.SliderSetting;
import io.github.kingstefan26.stefans_util.main;
import io.github.kingstefan26.stefans_util.module.macro.macro;
import io.github.kingstefan26.stefans_util.module.macro.util.util;
import io.github.kingstefan26.stefans_util.module.macro.wart.helper.macroStates;
import io.github.kingstefan26.stefans_util.module.macro.wart.helper.wartMacroUtil;
import io.github.kingstefan26.stefans_util.module.macro.wart.helper.wartState;
import io.github.kingstefan26.stefans_util.service.impl.WorldInfoService;
import io.github.kingstefan26.stefans_util.service.impl.chatService;
import io.github.kingstefan26.stefans_util.service.impl.inputLockerService;
import io.github.kingstefan26.stefans_util.service.impl.keyControlService;
import io.github.kingstefan26.stefans_util.util.renderUtil.draw3Dline;
import io.github.kingstefan26.stefans_util.util.renderUtil.drawCenterString;
import io.github.kingstefan26.stefans_util.util.stefan_utilEvents;
import lombok.Value;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;

import static io.github.kingstefan26.stefans_util.module.macro.wart.helper.macroStates.*;
import static io.github.kingstefan26.stefans_util.service.impl.keyControlService.action.walk.right;


public class UniversalWartMacro extends basicModule implements macro {
    public static final String chatprefix = "§d[Wart-Macro]§r ";

    public final wartMacroUtil helpers = wartMacroUtil.getHelper(this);

    public wartState macroState = new wartState(this);

    // values changed by settings
    public int unpauseKey;
    public boolean experimentalGuiFlag;
    public boolean verboseLogging;
    public int wantedPitch;
    public boolean perfectHeadRotation;
    public float rateOfChange = 0.01F;

    // values that change on runtime
    public int playerYaw;
    public int playerPitch;
    public double playerSpeed;
    public int fallCounter;
    public util.macroMenu macroMenu;
    public util.walkStates macroWalkStage = util.walkStates.DEFAULT; // TODO make a better state model (compatible with last left off)


    // runtime routine flags
    public boolean guiCloseGrace;
    public boolean playerTeleported;
    public boolean playerFallen;


    public UniversalWartMacro() {
        super("UniversalWartMacro", "macros wart", moduleManager.Category.MACRO,
                new keyBindDecorator("wartMacro")
        );
    }


    @Value
    public static class Truple{
        BlockPos key;
        BlockPos value;
        BlockPos thisthing;
    }


    @Override
    public void onLoad() {
        new SliderSetting("rate of change", this, 0.01, 0, 0.5F, (newvalue) -> {
            double temp = (Double) newvalue;
            rateOfChange = (float) temp;
        });
        new SliderNoDecimalSetting("pitch", this, 9, 0, 90, (newvalue) ->
                wantedPitch = (int) newvalue);
        new CheckSetting("perfect head rotation", this, true, (newvalue) ->
                perfectHeadRotation = (boolean) newvalue);
        new CheckSetting("experimental gui", this, false, (onUpdateCallbackValue) ->
                experimentalGuiFlag = (boolean) onUpdateCallbackValue);
        new CheckSetting("verbose logging", this, false, (onUpdateCallbackValue) ->
                verboseLogging = (boolean) onUpdateCallbackValue);
        new ChoseAKeySetting("unpause key", this, 0, (onUpdateCallbackValue) ->
                unpauseKey = (int) onUpdateCallbackValue);

        super.onLoad();
    }


    @Override
    public void onWorldRender(RenderWorldLastEvent e) {
        // TODO just dont do useless things please
        Entity viewer = mc.getRenderViewEntity();
        double viewerX = viewer.lastTickPosX + (viewer.posX - viewer.lastTickPosX) * e.partialTicks;
        double viewerY = viewer.lastTickPosY + (viewer.posY - viewer.lastTickPosY) * e.partialTicks;
        double viewerZ = viewer.lastTickPosZ + (viewer.posZ - viewer.lastTickPosZ) * e.partialTicks;

        GlStateManager.disableCull();

        draw3Dline.draw3DLine(new Vec3(viewerX, viewerY, viewerZ),
                new Vec3(viewerX, viewerY + 1, viewerZ),
                0xffffff,
                5,
                false,
                e.partialTicks);

        GlStateManager.enableCull();
    }

    @Override
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (Keyboard.isCreated()) {
            if (Keyboard.getEventKeyState()) {
                int keyCode = Keyboard.getEventKey();
                if (keyCode <= 0)
                    return;

                if (macroState.checkState(PAUSED)) {
                    if (keyCode == localDecoratorManager.keyBindDecorator.keybind.getKeyCode()) {
                        macroState.setState(STOPPING);
                        // TODO this needs major a face lift
                        mc.displayGuiScreen(null);

                        chatService.queueCleanChatMessage(chatprefix + "disabled wart macro");

                        playerYaw = 0;
                        playerPitch = 0;

                        KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.getKeyCode(), false);
                        KeyBinding.setKeyBindState(mc.gameSettings.keyBindBack.getKeyCode(), false);
                        KeyBinding.setKeyBindState(mc.gameSettings.keyBindRight.getKeyCode(), false);
                        KeyBinding.setKeyBindState(mc.gameSettings.keyBindLeft.getKeyCode(), false);
                        KeyBinding.setKeyBindState(mc.gameSettings.keyBindAttack.getKeyCode(), false);
                        macroWalkStage = util.walkStates.BOTTOM;

                        macroState.setDontSpamFlag(false);


                        for (basicModule m : moduleRegistery.getModuleRegistery().loadedModules) {
                            if ("autorecorrect".equals(m.getName())) {
                                if (!mc.isSingleplayer()) {
                                    m.setToggled(macroState.isAutorecconectStatus());
                                }
                            }
                            if ("Sprint".equals(m.getName())) {
                                m.setToggled(macroState.isSprintStatus());
                            }
                            if ("lastLeftOff".equals(m.getName())) {
                                m.setToggled(macroState.isLastLeftOffStaus());
                            }
                            if ("stolenoverlay!".equals(m.getName())) {
                                m.setToggled(macroState.isStolenFarmOverlayStatus());
                            }
                        }


//        lastLeftOff.getLastLeftOff().registerLastLeftOff(
//                new lastleftoffObject((float)mc.thePlayer.posX,
//                        (float) mc.thePlayer.posY,
//                        (float)mc.thePlayer.posZ,
//                        cropType.WART,
//                        walkStates,
//                        System.currentTimeMillis()));

                        macroState.setState(IDLE);
                        onDisable();
                    }
                    if (keyCode == unpauseKey) macroState.setState(RECALIBRATING);
                }
                if (macroState.checkState(RECALIBRATING)) {
                    if (keyCode == localDecoratorManager.keyBindDecorator.keybind.getKeyCode())
                        macroState.setState(STOPPING);

                }
            }
        }
    }


    @Override
    public void onRenderTick(TickEvent.RenderTickEvent e) {
        switch (macroState.getState()) {
            case IDLE:
                break;
            case PAUSED:
                // TODO create rendering on pause
                if (mc.currentScreen == null) {
                    drawCenterString.GuiNotif(mc, "macro paused");
                    drawCenterString.drawCenterStringOnScreenLittleToDown(mc, "press key "
                            + Keyboard.getKeyName(localDecoratorManager.keyBindDecorator.keybind.getKeyCode()) +
                            " again to disable the macro, press " + Keyboard.getKeyName(unpauseKey) + " to start again", "ff002f");
                }
                break;
            case MACROING:
                // TODO: draw "pathfind" to find urself a way tru the farm (very far fetched)
                drawCenterString.GuiNotif(mc, "Whats good korea");
                drawCenterString.drawCenterStringOnScreenLittleToDown(mc, "press key "
                        + Keyboard.getKeyName(localDecoratorManager.keyBindDecorator.keybind.getKeyCode()) +
                        " to stop", "ff002f");
                if (mc.theWorld == null || mc.thePlayer == null) {
                    macroState.setState(AUTONOMOUS_RECALIBRATING);
                } else {// TODO: update walk routine CHECK IF THE PLAYER IS GETTING CROPS AND IF NOT RECALIBRATE
                    if (!wartMacroUtil.isPlayerLookingAtBlock("minecraft:nether_wart")) {
                        macroState.setState(AUTONOMOUS_RECALIBRATING);
                    }
                    if (false /* here we check if we are still collecting wart TODO: imprement checing if player is still collectiign wart with a resonable temporal shift */) {
                        macroState.setState(AUTONOMOUS_RECALIBRATING);
                    }
                    macroState.setState(MACROING);
                }


                break;
            case RECALIBRATING:
                if (mc.thePlayer != null && mc.theWorld != null) {// TODO lets break this up into smaller pieces
                    // TODO implement verbose logging support
                    drawCenterString.GuiNotif(mc,
                            "macro will start when you lock your head position on the right angle");
                    long start = System.nanoTime();

                    Tuple<BlockPos, String>[] blocks = wartMacroUtil.checkBlocksRoundPlayer();

                    wartMacroUtil.turnHeadToWart();// WE DISPLAY THE STPUDID STRING
                    drawCenterString.drawCenterStringOnScreenLittleToDown(mc,
                            " Current Pitch: " + playerPitch + " / " + wantedPitch +
                                    "Is mouse over wart " + wartMacroUtil.isPlayerLookingAtBlock("minecraft:nether_wart") + " is facing wart " + wartMacroUtil.isInFrontTreeWart(blocks),
                            "ff002f");
                    if (verboseLogging)
                        logger.info("finished raytracing/getting bloksc in " + (System.nanoTime() - start));
                }


                break;
            case AUTONOMOUS_RECALIBRATING:
                drawCenterString.GuiNotif(mc, "autonomous recalibration...");
                if (macroState.isAutonomous_recalibration_moveheadtowartflag()) {
                    wartMacroUtil.turnHeadToWart();
                }
                break;
            case STARTING:

                break;
            case STOPPING:
                break;
        }

    }

    @Override
    public void onHighestClientTick(TickEvent.ClientTickEvent event) {
        if (mc == null || mc.theWorld == null || mc.thePlayer == null) {
            macroState.setState(AUTONOMOUS_RECALIBRATING);
            return;
        }

        switch (macroState.getState()) {
            case IDLE:
                break;
            case PAUSED:
                break;
            case MACROING:
                if (event.phase == TickEvent.Phase.START) {
                    KeyBinding.setKeyBindState(mc.gameSettings.keyBindAttack.getKeyCode(), true);
                }
                if (macroState.isDontSpamFlag()) {
                    macroState.setDontSpamFlag(false);
                    keyControlService.submitCommandASYNC(new keyControlService.simpleCommand(macroState.getCurrentWalkAction(), () -> {
                        final keyControlService.action.walk result = wartMacroUtil.whichWayToGoMockUp(macroState.getCurrentWalkAction());

                        macroState.setCurrentWalkAction(result == null ? right : result);

                        macroState.setDontSpamFlag(true);
                    }));
                }
                break;
            case RECALIBRATING:
                // TODO OLD OUTDATED CHECKS
                playerYaw = Math.round(wartMacroUtil.getYaw());
                playerPitch = Math.round(mc.thePlayer.rotationPitch);


                if (playerPitch == wantedPitch) macroState.setState(macroStates.MACROING);
                if (macroState.checkState(MACROING)) {
                    //            shouldLoop = true;

                    chatService.queueCleanChatMessage(chatprefix + "started macring");
                    macroState.setDontSpamFlag(true);
                    //lastLeftOff.nullLastLeftOff();
                    if (experimentalGuiFlag) {
                        guiCloseGrace = false;
                        mc.displayGuiScreen(macroMenu); // TODO fix the macro menu not clicking things
                    }
                    // TODO make so this thing pauses the game
                    inputLockerService.lock(localDecoratorManager.keyBindDecorator.keybind.getKeyCode(), () -> {
                        macroState.setState(PAUSED);
                        macroState.setDontSpamFlag(false);
                        KeyBinding.unPressAllKeys();
                        keyControlService.clearCommandQueue();
                    });
                }
                break;
            case AUTONOMOUS_RECALIBRATING:
                if (main.debug) {
                    if (mc != null && mc.theWorld != null && !mc.isSingleplayer()) {
                        if (mc.getCurrentServerData() != null) {
                            chatService.queueCleanChatMessage("in this part we /warp skyblock and shii");
                            macroState.setState(MACROING);
                        }
                    }
                    // #not debug #not my problem #annoying
                } else {
                    if (macroState.getExpireTimeSpamp() != 0) {
                        if (System.currentTimeMillis() >= macroState.getExpireTimeSpamp()) {
                            macroState.setExpireTimeSpamp(0);
                        }
                    }

                    if (macroState.getExpireTimeSpamp() == 0 && mc != null && mc.theWorld != null && !mc.isSingleplayer() && WorldInfoService.isOnHypixel()) {
                        if (mc.getCurrentServerData() != null) {

                            boolean inSB = WorldInfoService.isInSkyblock(), onIS = WorldInfoService.isOnPrivateIsland();

                            if (!onIS && !inSB) {
                                chatService.sendMessage("/play skyblock");

                                macroState.setExpireTimeSpamp(System.currentTimeMillis() + 5000);
                            }

                            if (inSB && !onIS) {
                                chatService.sendMessage("/warp island");

                                macroState.setExpireTimeSpamp(System.currentTimeMillis() + 5000);
                            }

                            if (inSB && onIS) {
                                // we use the fact that this is set once to not spam chat smh
                                if (macroState.isAutonomous_recalibration_moveheadtowartflag()) {
                                    chatService.queueCleanChatMessage(chatprefix + "horray we got the the private island");
                                    chatService.queueCleanChatMessage(chatprefix + "now time to turn our head to the wart smh");
                                }

                                macroState.setAutonomous_recalibration_moveheadtowartflag(true);

                                if (wartMacroUtil.isInFrontTreeWart(wartMacroUtil.checkBlocksRoundPlayer()) && wartMacroUtil.isPlayerLookingAtBlock("minecraft:nether_wart")) {
                                    chatService.queueCleanChatMessage(chatprefix + "yay we are facing 3 wart and out mouse is over wart time to macro again");
                                    macroState.setAutonomous_recalibration_moveheadtowartflag(false);
                                    macroState.setState(MACROING);
                                }

                            }

                        }
                    }
                }
                // locate urself

                break;
            case STARTING:
                boolean finished = false;
// TODO this also needs a face lift
                if (localDecoratorManager.keyBindDecorator.keybind.getKeyCode() == 0) {
                    chatService.queueCleanChatMessage(chatprefix + "please set a keybinding!");
                    toggle();
                } else {
                    if (!main.debug) {
                        if (!WorldInfoService.isOnPrivateIsland()) {
                            chatService.queueClientChatMessage("please join a your island!", chatService.chatEnum.CHATPREFIX);
                            setToggled(false);
                            finished = true;
                        }
                    }
                    if (!finished) {
                        for (basicModule m : moduleRegistery.getModuleRegistery().loadedModules) {
                            if ("autorecorrect".equals(m.getName())) {
                                if (!mc.isSingleplayer()) {
                                    macroState.setAutorecconectStatus(m.isToggled());
                                    m.setToggled(true);
                                }
                            }
                            if ("Sprint".equals(m.getName())) {
                                macroState.setSprintStatus(m.isToggled());
                                m.setToggled(true);
                            }
                            if ("lastLeftOff".equals(m.getName())) {
                                macroState.setLastLeftOffStaus(m.isToggled());
                                m.setToggled(true);
                            }
                            if ("stolenoverlay!".equals(m.getName())) {
                                macroState.setStolenFarmOverlayStatus(m.isToggled());
                                m.setToggled(true);
                            }
                        }
                        mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("stefan_util:annoying"), 1.0F));
                        fallCounter = 0;//        String serverId = UUID.randomUUID().toString().replace("-", "");
                        //
                        //        try {
                        //            mc.getSessionService().joinServer(mc.getSession().getProfile(), mc.getSession().getToken(), serverId);
                        //        } catch (Exception var5) {
                        //            return;
                        //        }
                        if (experimentalGuiFlag) {
                            if (macroMenu == null) macroMenu = new util.macroMenu(this);
                            guiCloseGrace = true;
                        }
                        mc.displayGuiScreen(null);
                        chatService.queueCleanChatMessage(chatprefix + "enabled wart macro");
                        chatService.queueCleanChatMessage(chatprefix + "note: please move the the start of your farm");
                        macroState.setState(RECALIBRATING);
                    }
                }


                break;
            case STOPPING:
                macroState.setState(STOPPING);
                // TODO this needs major a face lift
                mc.displayGuiScreen(null);

                chatService.queueCleanChatMessage(chatprefix + "disabled wart macro");

                playerYaw = 0;
                playerPitch = 0;

                KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.getKeyCode(), false);
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindBack.getKeyCode(), false);
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindRight.getKeyCode(), false);
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindLeft.getKeyCode(), false);
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindAttack.getKeyCode(), false);
                macroWalkStage = util.walkStates.BOTTOM;

                macroState.setDontSpamFlag(false);


                for (basicModule m : moduleRegistery.getModuleRegistery().loadedModules) {
                    if ("autorecorrect".equals(m.getName())) {
                        if (!mc.isSingleplayer()) {
                            m.setToggled(macroState.isAutorecconectStatus());
                        }
                    }
                    if ("Sprint".equals(m.getName())) {
                        m.setToggled(macroState.isSprintStatus());
                    }
                    if ("lastLeftOff".equals(m.getName())) {
                        m.setToggled(macroState.isLastLeftOffStaus());
                    }
                    if ("stolenoverlay!".equals(m.getName())) {
                        m.setToggled(macroState.isStolenFarmOverlayStatus());
                    }
                }


//        lastLeftOff.getLastLeftOff().registerLastLeftOff(
//                new lastleftoffObject((float)mc.thePlayer.posX,
//                        (float) mc.thePlayer.posY,
//                        (float)mc.thePlayer.posZ,
//                        cropType.WART,
//                        walkStates,
//                        System.currentTimeMillis()));

                macroState.setState(IDLE);
                onDisable();
                break;
        }

    }


    @SubscribeEvent
    public void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
        logger.info("player dissconected from server turning on AUTONOMOUS_RECALIBRATING");
        macroState.setState(AUTONOMOUS_RECALIBRATING);
    }

    @SubscribeEvent
    public void onPlayerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        chatService.queueCleanChatMessage(chatprefix + "you swiched worlds, turning on autonomous recalibration");
        if (macroState.checkState(MACROING)) {
            macroState.setState(AUTONOMOUS_RECALIBRATING);
        }
    }

    int tickcounter = 0;

    @SubscribeEvent
    public void onTickClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            tickcounter++;
        }
        if (tickcounter % 20 == 0) {
            if (mc != null && mc.theWorld != null && mc.thePlayer != null) {
                playerSpeed = mc.thePlayer.getDistance(mc.thePlayer.lastTickPosX, mc.thePlayer.lastTickPosY, mc.thePlayer.lastTickPosZ);
                if (mc.thePlayer.posY - mc.thePlayer.lastTickPosY < 0) {
                    playerFallCallBack();
                }
            }
            tickcounter = 0;
        }
    }


    public void startingFromLastLeftOffRoutine() {
        // TODO IMPREMENT THIS GOD DAMMIT
//        if(lastLeftOff.getLastleftoffObject() != null) {
//            macroWalkStage = walkStates.getLastleftoffObject().getMacroStage();
//        }else{
//            macroWalkStage = walkStates.RIGHT;
//        }
    }


    public void disableFromGui() {
        this.toggle();
    }


    @Override
    public void onEnable() {
        macroState.setState(STARTING);
        super.onEnable();
    }


    public void playerFallCallBack() {
        if (macroState.checkState(MACROING) && !playerTeleported) {
            fallCounter++;
            chatService.queueCleanChatMessage(chatprefix + "fallen for the " + fallCounter + " time");
            playerFallen = true;
        }
    }

    @Override
    public void onPlayerTeleportEvent(stefan_utilEvents.playerTeleportEvent event) {
        if (macroState.checkState(MACROING)) {
            chatService.queueCleanChatMessage(chatprefix + "teleport detected!");
            playerTeleported = true;
        }
    }

    @Override
    public void onGuiOpen(GuiOpenEvent e) {
        if (mc == null || mc.theWorld == null || mc.thePlayer == null) return;
        if (macroState.checkState(MACROING) || macroState.checkState(RECALIBRATING)) {
            e.setCanceled(true);
        }
    }
}
