package io.github.kingstefan26.stefans_util.module.macro.wart;

import io.github.kingstefan26.stefans_util.core.module.Module;
import io.github.kingstefan26.stefans_util.core.module.ModuleManager;
import io.github.kingstefan26.stefans_util.core.module.moduleRegistery;
import io.github.kingstefan26.stefans_util.core.setting.Setting;
import io.github.kingstefan26.stefans_util.core.setting.SettingsManager;
import io.github.kingstefan26.stefans_util.main;
import io.github.kingstefan26.stefans_util.module.macro.macroUtil.macroMenu;
import io.github.kingstefan26.stefans_util.module.util.chat;
import io.github.kingstefan26.stefans_util.module.util.inputLocker;
import io.github.kingstefan26.stefans_util.util.renderUtil.drawCenterString;
import io.github.kingstefan26.stefans_util.util.stefan_utilEvents;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.Objects;

import static io.github.kingstefan26.stefans_util.util.renderUtil.draw3Dline.draw3DLine;

public class UniversalWartMacro extends Module {

    public UniversalWartMacro() {
        super("UniversalWartMacro", "macros wart", ModuleManager.Category.MACRO, true);
    }

    enum walkStates {
        LEFT, RIGHT, TOP, BOTTOM, HOLD, DEFAULT, LEFTTOP, RIGHTTOP, RIGHTBOTTOM, LEFTBOTTOM
    }

    private io.github.kingstefan26.stefans_util.module.macro.macroUtil.macroMenu macroMenu;

    private boolean isMacroingReady;
    private int wantedPitch;
    private int wantedYaw;
    private boolean perfectHeadRotation;
    private boolean experimentalGui;

    private walkStates macroWalkStage = walkStates.RIGHT;

    private boolean playerTeleported;
    private boolean playerFallen;
    private boolean guiCloseGrace;


    private long playerSpeedCheckTimer;
    private long YSpeedTimer;
    private int TickCounter = 1;


    private int playerYaw;
    private int playerPitch;
    private double playerSpeed;
    private int fallCounter;


    @Override
    public void onLoad(){
        SettingsManager.getSettingsManager().rSetting(new Setting("version", this,"vertical design",new ArrayList<String>() {{
            add("vertical design");
            add("horizontal with pads");
            add("horizontal with no pad");
        }}));
        SettingsManager.getSettingsManager().rSetting(new Setting("yaw", this, 90, 0, 180, true));
        SettingsManager.getSettingsManager().rSetting(new Setting("pitch", this, 9, 0, 90, true));
        SettingsManager.getSettingsManager().rSetting(new Setting("perfect head rotation", this, true));
        SettingsManager.getSettingsManager().rSetting(new Setting("experimental gui", this, false));
        super.onLoad();
    }

    private float getYaw(){
        float yaw = mc.thePlayer.rotationYawHead;
        yaw %= 360;
        if(yaw < 0) yaw += 360;
        if(yaw > 180) yaw -= 360;
        return yaw;
    }

    @SubscribeEvent
    public void render(RenderWorldLastEvent event){
        Entity viewer = Minecraft.getMinecraft().getRenderViewEntity();
        double viewerX = viewer.lastTickPosX + (viewer.posX - viewer.lastTickPosX) * event.partialTicks;
        double viewerY = viewer.lastTickPosY + (viewer.posY - viewer.lastTickPosY) * event.partialTicks;
        double viewerZ = viewer.lastTickPosZ + (viewer.posZ - viewer.lastTickPosZ) * event.partialTicks;



        double x = (int)mc.thePlayer.posX - viewerX;
        double y = (int)(mc.thePlayer.posY + 1 - viewerY);
        double z = (int)mc.thePlayer.posZ - - viewerZ;
//
        AxisAlignedBB bb = new AxisAlignedBB(x, y, z, x + 1, y + 1, z + 1);

//		GlStateManager.disableDepth();

        Vec3 pos1;
        Vec3 pos2;
        pos1 = new Vec3(mc.thePlayer.posX,mc.thePlayer.posY,mc.thePlayer.posZ);
        pos2 = new Vec3(mc.thePlayer.posX,mc.thePlayer.posY + 1,mc.thePlayer.posZ);


        Vec3 pos3;
        Vec3 pos4;
        pos3 = new Vec3(viewerX,viewerY,viewerZ);
        pos4 = new Vec3(viewerX,viewerY + 1,viewerZ);

        GlStateManager.disableDepth();
        GlStateManager.disableCull();
        GlStateManager.disableTexture2D();
        drawFilledBoundingBox(bb, 1F, 0xffffff);
        draw3DLine(pos3,pos4, 0xffffff, 5, false, event.partialTicks);
        draw3DLine(pos1,pos2, 0xffaaff, 5, false, event.partialTicks);



        GlStateManager.disableLighting();
        GlStateManager.enableDepth();
        GlStateManager.enableTexture2D();
    }


    @SubscribeEvent
    public void onRenderLast(TickEvent.RenderTickEvent a){
        renderTickRoutine(isMacroingReady);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if(isMacroingReady){
            if(event.phase == TickEvent.Phase.START) {
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindAttack.getKeyCode(), true);
            }
        }
    }

    @Override
    public void onTick(TickEvent.ClientTickEvent e){
        if(e.phase == TickEvent.Phase.START){
            TickCounter++;
            if (TickCounter % 20 == 0) {
                if (mc.theWorld == null || mc.thePlayer == null) return;

                //chat.queueClientChatMessage("X:" + mc.thePlayer.posX + " Y:" + mc.thePlayer.posY + " Z:" + mc.thePlayer.posZ, chat.chatEnum.DEBUG);

                TickCounter = 0;
            }
        }
        if (mc == null || mc.theWorld == null || mc.thePlayer == null) return;


        OnTickRoutine();
        if (isMacroingReady) {
            MacroRoutine();
        } else {
            JustEnabledRoutine();
        }
    }

    private static class checkBlock {
        //the xyz is reative to player feet
        public int x, y, z;
        public String name;
    }


    private void checkForBlocksRoundPlayer(){

        WorldClient world = mc.theWorld;

        Block block = world.getBlockState(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1.5, mc.thePlayer.posZ)).getBlock();
    }


    private void renderTickRoutine(Boolean macroReadyStatus){
        if(macroReadyStatus){
            drawCenterString.GuiNotif(mc, "Whats good korea");
            drawCenterString.drawCenterStringOnScreenLittleToDown(mc, "press key "
                    + Keyboard.getKeyName(this.getKeyBindingObj().getKeyCode()) +
                    " to stop", "ff002f");
        }else{

            drawCenterString.GuiNotif(mc,
                    "macro will start when you lock your head position on the right angle");
            drawCenterString.drawCenterStringOnScreenLittleToDown(mc,
                    "Current Yaw: " + playerYaw + " Current Pitch: " + playerPitch,
                    "ff002f");
//            drawCenterString.drawCenterStringOnScreenLittleToDown(mc,
//                    "press key " + Keyboard.getKeyName(this.getKeyBindingObj().getKeyCode()) + " to stop",
//                    "ff002f");
        }
    }

    public static void drawFilledBoundingBox(AxisAlignedBB p_181561_0_, float alpha, int rgb) {
        Color c = new Color(rgb);

        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.disableTexture2D();
//		GL11.glEnable(GL11.GL_DEPTH_TEST);
//		GlStateManager.depthMask(true);

        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();

        GlStateManager.color(c.getRed() / 255f, c.getGreen() / 255f, c.getBlue() / 255f, c.getAlpha() / 255f * alpha);

        //vertical
        worldrenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
        worldrenderer.pos(p_181561_0_.minX, p_181561_0_.minY, p_181561_0_.minZ).endVertex();
        worldrenderer.pos(p_181561_0_.maxX, p_181561_0_.minY, p_181561_0_.minZ).endVertex();
        worldrenderer.pos(p_181561_0_.maxX, p_181561_0_.minY, p_181561_0_.maxZ).endVertex();
        worldrenderer.pos(p_181561_0_.minX, p_181561_0_.minY, p_181561_0_.maxZ).endVertex();
        tessellator.draw();
        worldrenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
        worldrenderer.pos(p_181561_0_.minX, p_181561_0_.maxY, p_181561_0_.maxZ).endVertex();
        worldrenderer.pos(p_181561_0_.maxX, p_181561_0_.maxY, p_181561_0_.maxZ).endVertex();
        worldrenderer.pos(p_181561_0_.maxX, p_181561_0_.maxY, p_181561_0_.minZ).endVertex();
        worldrenderer.pos(p_181561_0_.minX, p_181561_0_.maxY, p_181561_0_.minZ).endVertex();
        tessellator.draw();


        GlStateManager.color(c.getRed() / 255f * 0.8f, c.getGreen() / 255f * 0.8f, c.getBlue() / 255f * 0.8f, c.getAlpha() / 255f * alpha);

        //x
        worldrenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
        worldrenderer.pos(p_181561_0_.minX, p_181561_0_.minY, p_181561_0_.maxZ).endVertex();
        worldrenderer.pos(p_181561_0_.minX, p_181561_0_.maxY, p_181561_0_.maxZ).endVertex();
        worldrenderer.pos(p_181561_0_.minX, p_181561_0_.maxY, p_181561_0_.minZ).endVertex();
        worldrenderer.pos(p_181561_0_.minX, p_181561_0_.minY, p_181561_0_.minZ).endVertex();
        tessellator.draw();
        worldrenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
        worldrenderer.pos(p_181561_0_.maxX, p_181561_0_.minY, p_181561_0_.minZ).endVertex();
        worldrenderer.pos(p_181561_0_.maxX, p_181561_0_.maxY, p_181561_0_.minZ).endVertex();
        worldrenderer.pos(p_181561_0_.maxX, p_181561_0_.maxY, p_181561_0_.maxZ).endVertex();
        worldrenderer.pos(p_181561_0_.maxX, p_181561_0_.minY, p_181561_0_.maxZ).endVertex();
        tessellator.draw();


        GlStateManager.color(c.getRed() / 255f * 0.9f, c.getGreen() / 255f * 0.9f, c.getBlue() / 255f * 0.9f, c.getAlpha() / 255f * alpha);
        //z
        worldrenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
        worldrenderer.pos(p_181561_0_.minX, p_181561_0_.maxY, p_181561_0_.minZ).endVertex();
        worldrenderer.pos(p_181561_0_.maxX, p_181561_0_.maxY, p_181561_0_.minZ).endVertex();
        worldrenderer.pos(p_181561_0_.maxX, p_181561_0_.minY, p_181561_0_.minZ).endVertex();
        worldrenderer.pos(p_181561_0_.minX, p_181561_0_.minY, p_181561_0_.minZ).endVertex();
        tessellator.draw();
        worldrenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
        worldrenderer.pos(p_181561_0_.minX, p_181561_0_.minY, p_181561_0_.maxZ).endVertex();
        worldrenderer.pos(p_181561_0_.maxX, p_181561_0_.minY, p_181561_0_.maxZ).endVertex();
        worldrenderer.pos(p_181561_0_.maxX, p_181561_0_.maxY, p_181561_0_.maxZ).endVertex();
        worldrenderer.pos(p_181561_0_.minX, p_181561_0_.maxY, p_181561_0_.maxZ).endVertex();
        tessellator.draw();
    }

    private void OnTickRoutine() {
        EntityPlayerSP player = mc.thePlayer; // re making the player cuz we accessing fields

        //get player speed to see if we reached the end of the farm already
        playerSpeed = player.getDistance(player.lastTickPosX, player.lastTickPosY, player.lastTickPosZ);

        if (System.currentTimeMillis() - YSpeedTimer > 500) {
            YSpeedTimer = System.currentTimeMillis();
            if (player.posY - player.lastTickPosY < 0) {
                MinecraftForge.EVENT_BUS.post(new stefan_utilEvents.playerFallEvent());
            }
        }
    }

//    Block[] aa = new Block[40];
    private void JustEnabledRoutine() {
        //update player pitch and yaw with up to date info
        playerYaw = Math.round(getYaw());
        playerPitch = Math.round(mc.thePlayer.rotationPitch);

//        long start = System.nanoTime();
//
//        WorldClient world = mc.theWorld;
//
//        Block block = world.getBlockState(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1.5, mc.thePlayer.posZ)).getBlock();
//
//        long stop = System.nanoTime();
//
//        this.logger.info("found block: " + block.getRegistryName() + " in: " + (stop-start) + " nano seconds");

        isMacroingReady = playerYaw == wantedYaw && playerPitch == wantedPitch;
        //isMacroingReady = true;

        if(isMacroingReady){
            preMacroRoutine();
        }
    }

    public void setPlayerRotations(float yaw, float pitch) {
        mc.thePlayer.rotationYaw = lerpAngle(mc.thePlayer.rotationYaw, yaw, 0.01F);
        mc.thePlayer.rotationPitch = lerpAngle(mc.thePlayer.rotationPitch, pitch, 0.01F);
    }

    public static float lerpAngle(float fromRadians, float toRadians, float progress) {
        float f = ((toRadians - fromRadians) % 360.0F + 540.0F) % 360.0F - 180.0F;
        return fromRadians + f * progress % 360.0F;
    }

    private void preMacroRoutine(){
        //lastLeftOff.nullLastLeftOff();
        if(perfectHeadRotation){
            setPlayerRotations(wantedYaw, wantedPitch);
        }
        if(experimentalGui){
            guiCloseGrace = false;
            mc.displayGuiScreen(macroMenu);
        }
        inputLocker.enable();
        inputLocker.unlockkey = this.getKeyBindingObj().getKeyCode();
    }

    private void MacroRoutine() {
        //checks the speed every half second so we don't spam the variable
        if (System.currentTimeMillis() - this.playerSpeedCheckTimer > 600) {
            //reset the timer
            this.playerSpeedCheckTimer = System.currentTimeMillis();

            if (playerTeleported) {
                playerTeleported = false;
            }
            if (playerFallen) {
                playerFallen = false;
            }
            switch (macroWalkStage){
                case LEFT:
                    macroWalkStage = walkStates.TOP;
                    break;
                case RIGHT:
                    macroWalkStage = walkStates.BOTTOM;
                    break;
                case TOP:
                    macroWalkStage = walkStates.RIGHT;
                    break;
                case BOTTOM:
                    macroWalkStage = walkStates.LEFT;
                    break;
            }
            this.logger.info(macroWalkStage);

        }
        macroWalk(macroWalkStage);
    }


    private void macroWalk(walkStates m) {
        if (m == walkStates.DEFAULT) {
            return;
        }

        switch (m) {
            case LEFT:
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindRight.getKeyCode(), false);
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindLeft.getKeyCode(), true);
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.getKeyCode(), false);
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindBack.getKeyCode(), false);
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

    @Override
    public void onEnable(){
        super.onEnable();
        if (this.getKey() == 0) {
            chat.queueClientChatMessage("please set a keybinding!", chat.chatEnum.CHATPREFIX);
            this.setToggled(false);
            return;
        }
        if (!main.debug) {
            if (!io.github.kingstefan26.stefans_util.module.util.SBinfo.isOnPrivateIsland()) {
                chat.queueClientChatMessage("please join a your island!", chat.chatEnum.CHATPREFIX);
                this.setToggled(false);
                return;
            }
        }
        for(Module m : moduleRegistery.moduleRegistery_.loadedModules){
            if(Objects.equals(m.getName(), "Sprint")){
                m.setToggled(false);
            }
        }
        mc.thePlayer.playSound("stefan_util:whatsgoodkorea", 1F, 1F);
//        if(lastLeftOff.getLastleftoffObject() != null) {
//            macroWalkStage = walkStates.getLastleftoffObject().getMacroStage();
//        }else{
//            macroWalkStage = walkStates.RIGHT;
//        }

        fallCounter = 0;
        wantedPitch = SettingsManager.getSettingsManager().getSettingByName("pitch", this).getValInt();
        wantedYaw = SettingsManager.getSettingsManager().getSettingByName("yaw", this).getValInt();
        experimentalGui = SettingsManager.getSettingsManager().getSettingByName("experimental gui", this).getValBoolean();
        perfectHeadRotation = SettingsManager.getSettingsManager().getSettingByName("perfect head rotation", this).getValBoolean();
        //reset the timer on enable
        this.playerSpeedCheckTimer = System.currentTimeMillis();
        this.YSpeedTimer = System.currentTimeMillis();

        if(experimentalGui){
            if(this.macroMenu == null) macroMenu = new macroMenu(this);

            guiCloseGrace = true;
        }

        mc.displayGuiScreen((GuiScreen) null);
        chat.queueClientChatMessage("enabled wart macro", chat.chatEnum.CHATPREFIX);

    }

    @Override
    public void onDisable(){
        super.onDisable();
        mc.displayGuiScreen(null);

        for(Module m : moduleRegistery.moduleRegistery_.loadedModules){
            if(Objects.equals(m.getName(), "Sprint")){
                m.setToggled(true);
            }
        }

        chat.queueClientChatMessage("disabled wart macro", chat.chatEnum.CHATPREFIX);
        /*
        reset every variable & unpress every key on disable
         */
        //mc.setIngameFocus();
        playerTeleported = false;
        isMacroingReady = false;
        playerFallen = false;

        playerYaw = 0;
        playerPitch = 0;
        playerSpeed = 0;

        KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.getKeyCode(), false);
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindBack.getKeyCode(), false);
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindRight.getKeyCode(), false);
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindLeft.getKeyCode(), false);
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindAttack.getKeyCode(), false);
        macroWalkStage = walkStates.BOTTOM;
//        lastLeftOff.getLastLeftOff().registerLastLeftOff(
//                new lastleftoffObject((float)mc.thePlayer.posX,
//                        (float) mc.thePlayer.posY,
//                        (float)mc.thePlayer.posZ,
//                        cropType.WART,
//                        walkStates,
//                        System.currentTimeMillis()));
    }

    @SubscribeEvent
    public void onPlayerFallEvent(stefan_utilEvents.playerFallEvent e) {
        if (isMacroingReady && !playerTeleported) {
            fallCounter++;
            chat.queueClientChatMessage("fallen for the " + fallCounter + " time", chat.chatEnum.CHATPREFIX);
            playerFallen = true;
        }
    }


    @SubscribeEvent
    public void onPlayerTeleportEvent(stefan_utilEvents.playerTeleportEvent event) {
        if (isMacroingReady) {
            chat.queueClientChatMessage("teleport detected!", chat.chatEnum.CHATPREFIX);
            playerTeleported = true;
        }
    }


    @SubscribeEvent
    public void onGuiOpen(GuiOpenEvent e) {
        if(experimentalGui){
            if (!(e.gui instanceof io.github.kingstefan26.stefans_util.module.macro.macroUtil.macroMenu) && !guiCloseGrace) {
                this.toggle();
            }
        }else{
            e.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onkey(stefan_utilEvents.clickedUnlockKeyEvent e){
        this.toggle();
    }


    @SubscribeEvent
    public void onUnloadWorld(WorldEvent.Unload event) {
        chat.queueClientChatMessage(this.getName() + " was unloaded because you switched worlds", chat.chatEnum.CHATPREFIX);
        super.setToggled(false);
    }
}
