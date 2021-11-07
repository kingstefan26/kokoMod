package io.github.kingstefan26.stefans_util.module.macro.wart;

import io.github.kingstefan26.stefans_util.core.rewrite.module.ModuleMenagers.moduleManager;
import io.github.kingstefan26.stefans_util.core.rewrite.module.moduleDecorators.impl.keyBindDecorator;
import io.github.kingstefan26.stefans_util.core.rewrite.module.moduleFrames.basicModule;
import io.github.kingstefan26.stefans_util.core.rewrite.setting.impl.CheckSetting;
import io.github.kingstefan26.stefans_util.core.rewrite.setting.impl.MultichoiseSetting;
import io.github.kingstefan26.stefans_util.core.rewrite.setting.impl.SliderNoDecimalSetting;
import io.github.kingstefan26.stefans_util.main;
import io.github.kingstefan26.stefans_util.module.macro.util.util;
import io.github.kingstefan26.stefans_util.service.impl.WorldInfoService;
import io.github.kingstefan26.stefans_util.service.impl.chatService;
import io.github.kingstefan26.stefans_util.service.impl.inputLockerService;
import io.github.kingstefan26.stefans_util.service.impl.keyControlService;
import io.github.kingstefan26.stefans_util.util.renderUtil.draw3Dline;
import io.github.kingstefan26.stefans_util.util.renderUtil.drawCenterString;
import io.github.kingstefan26.stefans_util.util.stefan_utilEvents;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class UniversalWartMacro extends basicModule {
    public static final String chatprefix = "§d[Wart-Macro]§r ";
    double playerSpeed;
    ScheduledExecutorService scheduledExecutorService;
    private util.macroMenu macroMenu;
    private boolean isMacroingReady;
    private int wantedPitch;
    private int wantedYaw;
    private boolean perfectHeadRotation;
    private boolean experimentalGuiFlag;

    private util.walkStates macroWalkStage = util.walkStates.RIGHT;

    private boolean playerTeleported;

    private boolean playerFallen;

    public UniversalWartMacro() {
        super("UniversalWartMacro", "macros wart", moduleManager.Category.MACRO,
                new keyBindDecorator("wartMacro")
        );
    }

    Runnable reCheckMacroSteps = (() -> {
        if (playerTeleported) {
            playerTeleported = false;
        }
        if (playerFallen) {
            playerFallen = false;
        }
        switch (macroWalkStage) {
            case LEFT:
                macroWalkStage = util.walkStates.TOP;
                break;
            case RIGHT:
                macroWalkStage = util.walkStates.BOTTOM;
                break;
            case TOP:
                macroWalkStage = util.walkStates.RIGHT;
                break;
            case BOTTOM:
                macroWalkStage = util.walkStates.LEFT;
                break;
        }
        this.logger.info(macroWalkStage);
    });

    private boolean guiCloseGrace;
    private int playerYaw;
    private int playerPitch;
    private int fallCounter;

    Runnable reCheckSpeed = (() -> {
        playerSpeed = mc.thePlayer.getDistance(mc.thePlayer.lastTickPosX, mc.thePlayer.lastTickPosY, mc.thePlayer.lastTickPosZ);
        if (mc.thePlayer.posY - mc.thePlayer.lastTickPosY < 0) {
            playerfallCallBack();
        }
    });

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

    @Override
    public void onLoad() {
        new MultichoiseSetting("version", this, "vertical design", new ArrayList<String>() {{
            add("vertical design");
            add("horizontal with pads");
            add("horizontal with no pad");
        }}, (newvalue) -> {

        });
        new SliderNoDecimalSetting("yaw", this, 90, 0, 180, (newvalue) -> wantedYaw = (int) newvalue);
        new SliderNoDecimalSetting("pitch", this, 9, 0, 90, (newvalue) -> wantedPitch = (int) newvalue);
        new CheckSetting("perfect head rotation", this, true, (newvalue) -> perfectHeadRotation = (boolean) newvalue);
        new CheckSetting("experimental gui", this, false, (onUpdateCallbackValue) -> experimentalGuiFlag = (boolean) onUpdateCallbackValue);

        super.onLoad();
    }

    @SubscribeEvent
    public void render(RenderWorldLastEvent event) {
        Entity viewer = mc.getRenderViewEntity();
        double viewerX = viewer.lastTickPosX + (viewer.posX - viewer.lastTickPosX) * event.partialTicks;
        double viewerY = viewer.lastTickPosY + (viewer.posY - viewer.lastTickPosY) * event.partialTicks;
        double viewerZ = viewer.lastTickPosZ + (viewer.posZ - viewer.lastTickPosZ) * event.partialTicks;

        GlStateManager.disableCull();

        draw3Dline.draw3DLine(new Vec3(viewerX, viewerY, viewerZ),
                new Vec3(viewerX, viewerY + 1, viewerZ),
                0xffffff,
                5,
                false,
                event.partialTicks);

        GlStateManager.enableCull();
    }

    @SubscribeEvent
    public void onRenderLast(TickEvent.RenderTickEvent a) {
        renderTickRoutine(isMacroingReady);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (mc == null || mc.theWorld == null || mc.thePlayer == null) return;
        if (isMacroingReady) {
            if (event.phase == TickEvent.Phase.START) {
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindAttack.getKeyCode(), true);
            }
            MacroRoutine();
        } else {
            JustEnabledRoutine();
        }
    }

    private void renderTickRoutine(Boolean macroReadyStatus) {
        if (macroReadyStatus) {
            drawCenterString.GuiNotif(mc, "Whats good korea");
            drawCenterString.drawCenterStringOnScreenLittleToDown(mc, "press key "
                    + Keyboard.getKeyName(localDecoratorManager.keyBindDecorator.keybind.getKeyCode()) +
                    " to stop", "ff002f");
        } else {
            drawCenterString.GuiNotif(mc,
                    "macro will start when you lock your head position on the right angle");
            drawCenterString.drawCenterStringOnScreenLittleToDown(mc,
                    "Current Yaw: " + playerYaw + "/" + wantedYaw + " Current Pitch: " + playerPitch + "/" + wantedPitch,
                    "ff002f");
        }
    }

    //    Block[] aa = new Block[40];
    private void JustEnabledRoutine() {
        //update player pitch and yaw with up to date info
        playerYaw = Math.round(getYaw());
        playerPitch = Math.round(mc.thePlayer.rotationPitch);

/*
        long start = System.nanoTime();

        WorldClient world = mc.theWorld;

        Block block = world.getBlockState(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1.5, mc.thePlayer.posZ)).getBlock();

        long stop = System.nanoTime();

        this.logger.info("found block: " + block.getRegistryName() + " in: " + (stop-start) + " nano seconds");
*/

        isMacroingReady = playerYaw == wantedYaw && playerPitch == wantedPitch;

        if (isMacroingReady) {
            preMacroRoutine();
        }
    }

    public void setPlayerRotations(float yaw, float pitch) {
        mc.thePlayer.rotationYaw = lerpAngle(mc.thePlayer.rotationYaw, yaw, 0.01F);
        mc.thePlayer.rotationPitch = lerpAngle(mc.thePlayer.rotationPitch, pitch, 0.01F);
    }

    private void preMacroRoutine() {
        //lastLeftOff.nullLastLeftOff();
        if (perfectHeadRotation) {
            setPlayerRotations(wantedYaw, wantedPitch);
        }
        if (experimentalGuiFlag) {
            guiCloseGrace = false;
            mc.displayGuiScreen(macroMenu);
        }
        inputLockerService.lock(localDecoratorManager.keyBindDecorator.keybind.getKeyCode(), this::toggle);
    }

    private void MacroRoutine() {
        macroWalk(macroWalkStage);
    }

    private void macroWalk(util.walkStates m) {
        if (m == util.walkStates.DEFAULT) {
            return;
        }

        switch (m) {
            case LEFT:
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindRight.getKeyCode(), false);
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindLeft.getKeyCode(), true);
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.getKeyCode(), false);
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindBack.getKeyCode(), false);
                keyControlService.submitCommandASYNC(new keyControlService.command(200, false,keyControlService.action.walk.left));
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

    public void disableFromGui() {
        this.toggle();
    }

    @Override
    public void onEnable() {
        super.onEnable();
        if (localDecoratorManager.keyBindDecorator.keybind.getKeyCode() == 0) {
            chatService.queueCleanChatMessage(chatprefix + "please set a keybinding!");
            this.toggle();
            return;
        }
        if (!main.debug) {
            if (!WorldInfoService.isOnPrivateIsland()) {
                chatService.queueClientChatMessage("please join a your island!", chatService.chatEnum.CHATPREFIX);
                this.setToggled(false);
                return;
            }
        }
        scheduledExecutorService = Executors.newScheduledThreadPool(4);

        scheduledExecutorService.scheduleAtFixedRate(reCheckSpeed, 0, 500, TimeUnit.MILLISECONDS);
        scheduledExecutorService.scheduleAtFixedRate(reCheckMacroSteps, 0, 600, TimeUnit.MILLISECONDS);


//        mc.thePlayer.playSound("stefan_util:whatsgoodkorea", 1F, 1F);
//        if(lastLeftOff.getLastleftoffObject() != null) {
//            macroWalkStage = walkStates.getLastleftoffObject().getMacroStage();
//        }else{
//            macroWalkStage = walkStates.RIGHT;
//        }

        fallCounter = 0;



        if (experimentalGuiFlag) {
            if (this.macroMenu == null) macroMenu = new util.macroMenu(this);
            guiCloseGrace = true;
        }

        mc.displayGuiScreen(null);
        chatService.queueClientChatMessage("enabled wart macro", chatService.chatEnum.CHATPREFIX);

    }

    @Override
    public void onDisable() {
        super.onDisable();
        mc.displayGuiScreen(null);
        if (scheduledExecutorService != null) scheduledExecutorService.shutdown();

        chatService.queueClientChatMessage("disabled wart macro", chatService.chatEnum.CHATPREFIX);
        playerTeleported = false;
        isMacroingReady = false;
        playerFallen = false;

        playerYaw = 0;
        playerPitch = 0;

        KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.getKeyCode(), false);
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindBack.getKeyCode(), false);
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindRight.getKeyCode(), false);
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindLeft.getKeyCode(), false);
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindAttack.getKeyCode(), false);
        macroWalkStage = util.walkStates.BOTTOM;
//        lastLeftOff.getLastLeftOff().registerLastLeftOff(
//                new lastleftoffObject((float)mc.thePlayer.posX,
//                        (float) mc.thePlayer.posY,
//                        (float)mc.thePlayer.posZ,
//                        cropType.WART,
//                        walkStates,
//                        System.currentTimeMillis()));
    }

    public void playerfallCallBack() {
        if (isMacroingReady && !playerTeleported) {
            fallCounter++;
            chatService.queueCleanChatMessage(chatprefix + "fallen for the " + fallCounter + " time");
            playerFallen = true;
        }
    }

    @SubscribeEvent
    public void onPlayerTeleportEvent(stefan_utilEvents.playerTeleportEvent event) {
        if (isMacroingReady) {
            chatService.queueCleanChatMessage(chatprefix + "teleport detected!");
            playerTeleported = true;
        }
    }

    @SubscribeEvent
    public void onGuiOpen(GuiOpenEvent e) {
        if (experimentalGuiFlag) {
            if (!(e.gui instanceof util.macroMenu) && !guiCloseGrace) {
                this.toggle();
            }
        } else {
            e.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onkey(stefan_utilEvents.clickedUnlockKeyEvent e) {
        this.toggle();
    }

    @SubscribeEvent
    public void onUnloadWorld(WorldEvent.Unload event) {
        chatService.queueCleanChatMessage(chatprefix + "unloaded because you switched worlds");
        super.setToggled(false);
    }
}
