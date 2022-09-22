package io.github.kingstefan26.stefans_util.module.debug.test;

import io.github.kingstefan26.stefans_util.core.module.moduleDecorators.impl.keyBindDecorator;
import io.github.kingstefan26.stefans_util.core.module.moduleframes.BasicModule;
import io.github.kingstefan26.stefans_util.module.render.LastLeftOff;
import io.github.kingstefan26.stefans_util.util.renderUtil.hehe;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import static io.github.kingstefan26.stefans_util.core.module.modulemenagers.ModuleManager.Category.DEBUG;
import static io.github.kingstefan26.stefans_util.util.renderUtil.draw3Dline.draw3DLine;


public class test extends BasicModule {

    public test() {
        super("test", "test", DEBUG, new keyBindDecorator("test123"));
        //        SettingsManager.getSettingsManager().rSetting(new Setting("interpolate steps", this, 10, 1, 60, true));
//        SettingsManager.getSettingsManager().rSetting(new Setting("wanted Yaw", this, 90, 1, 90, false));
//        SettingsManager.getSettingsManager().rSetting(new Setting("wanted Pitch", this, 1, 1, 90, false));

    }
//    boolean firstTime = false;
//    long interpolateTimer;
//    int stepCount = 100;
//    int currentStep = 1;
//    float wantedPitch, wantedYaw;
//    float enableYaw, enablePitch;
//    float diffranceYaw, diffrancePitch;

    float wantedX = 0, wantedY = 10 , wantedZ = 0;


    @SubscribeEvent
    public void onWorldRender(RenderWorldLastEvent e) {
        hehe.drawTextAtWorld("hehe", 0, 10, 0, Integer.parseInt("47a8ed", 16), 0.10F, false, true, e.partialTicks);
        BlockPos desiredlocation = new BlockPos(wantedX,wantedY,wantedZ);
        BlockPos playerlocation = mc.thePlayer.getPosition();
        //line one
        Vec3 pos1;
        Vec3 pos2;
        //line two
        Vec3 pos3;
        Vec3 pos4;

        pos1 = new Vec3(playerlocation.getX(),playerlocation.getY(),playerlocation.getZ());
        pos2 = new Vec3(desiredlocation.getX(),playerlocation.getY(),desiredlocation.getZ());

        pos3 = new Vec3(desiredlocation.getX(),playerlocation.getY(),desiredlocation.getZ());
        pos4 = new Vec3(desiredlocation.getX(),desiredlocation.getY(),desiredlocation.getZ());

        draw3DLine(pos1,pos2, 0x47a8ed, 5, false, e.partialTicks);

        draw3DLine(pos3,pos4, 0x47a8ed, 5, false, e.partialTicks);



    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent e ){
        if(mc.thePlayer == null) return;
//        float yaw = Minecraft.getMinecraft().thePlayer.rotationYawHead;
//        yaw %= 360;
//        if(yaw < 0) yaw += 360;
//        if(yaw > 180) yaw -= 360;



        //    final GuiScreen b = new GuiScreen() {
//        @Override
//        public void drawScreen(int par1, int par2, float par3) {
//            super.drawScreen(par1, par2, par3);
//            for (int i = 0; i < GuiScreenText.length; ++i) {
//                drawCenteredString(fontRendererObj, GuiScreenText[i], width / 2, height / 3 + 12 * i, 0xFFFFFFFF);
//            }
//        }
//
//        @Override
//        public void initGui() {
//            super.initGui();
//            //this.buttonList.clear();
//            //this.buttonList.add(new GuiButton(0, width / 2 - 50, height - 50, 100,20, "close"));
//        }
//
//        @Override
//        protected void actionPerformed(GuiButton button) throws IOException {
//            //FMLCommonHandler.instance().exitJava(-1,true);
//        }
//
//        @Override
//        public boolean doesGuiPauseGame() {
//            return false;
//        }
//    };



//        EntityPlayerSP ab = mc.thePlayer;
//        ab.rotationYaw = wantedYaw;
//        ab.rotationPitch = wantedPitch;
//        this.toggle();
//
//            if(System.currentTimeMillis() - interpolateTimer > 500){
//                interpolateTimer = System.currentTimeMillis();
//                if(currentStep == stepCount){
//                    sendChatMessage.sendClientMessage("hehe nooB", false);
//                    this.toggle();
//                }else{
//                    EntityPlayerSP a = mc.thePlayer;
//                    float currentYaw;
//                    float currentPitch;
//
//                    currentYaw = enableYaw + stepCount /currentStep * diffranceYaw;
//
//                    currentPitch = enablePitch + stepCount / currentStep * diffrancePitch;
//                    a.rotationYaw = currentYaw;
//                    a.rotationPitch = currentPitch;
//                    currentStep++;
//                }
//            }
//
    }

    @Override
    public void onEnable() {
        super.onEnable();




        //Display d = new Display();

//        sendChatMessage.sendClientMessage("bye reaload!!!", true);
//        lastLeftOff.getLastLeftOff().registerLastLeftOff(new lastleftoffObject(10, 10, 10, cropType.WART, macroStages.DEFAULT));
        newergui n = new newergui();
        mc.displayGuiScreen(n);
//        KeyBinding.setKeyBindState(mc.gameSettings.keyBindAttack.getKeyCode(), true);

//        firstTime = true;
//        interpolateTimer = System.currentTimeMillis();
//        stepCount = SettingsManager.getSettingsManager().getSettingByName("interpolate steps", this).getValInt();
//        wantedPitch = (float)SettingsManager.getSettingsManager().getSettingByName("wanted Yaw", this).getValDouble();
//        wantedYaw = (float)SettingsManager.getSettingsManager().getSettingByName("wanted Pitch", this).getValDouble();
//
//        System.out.println("we are going to:" + wantedYaw);
//
//        EntityPlayerSP a = mc.thePlayer;
//        enableYaw = a.rotationYawHead;
//        enablePitch = a.rotationPitch;
//        System.out.println("enable Yaw:" + enableYaw);
//        System.out.println("enable Pitch:" + enablePitch);
//
////        float translatedYaw_temp = enableYaw % 180;
//        float translatedYaw_temp = a.getRotationYawHead();
//        System.out.println("transleted yaw:" + translatedYaw_temp);
//        float YawMultiplier = wantedYaw / translatedYaw_temp;
//        System.out.println("yaw multip[irer:" + YawMultiplier);
//        float finalYaw_temp = enableYaw * YawMultiplier;
//        System.out.println("finial yaw that is going to:" + finalYaw_temp);
//
//        diffranceYaw = enableYaw - finalYaw_temp;
//        System.out.println("diffrence bettwen yaw and yaw that we are going to:" + diffranceYaw);
//
//
//        diffrancePitch = enablePitch - wantedPitch;
    }
    @Override
    public void onDisable() {
        super.onDisable();
        LastLeftOff.nullLastLeftOff();
//        currentStep = 1;
    }


}
