package me.kokoniara.kokoMod.module.modules.misc;

import me.kokoniara.kokoMod.core.kokoMod;
import me.kokoniara.kokoMod.module.moduleUtil.module.Category;
import me.kokoniara.kokoMod.module.moduleUtil.module.Module;
import me.kokoniara.kokoMod.module.moduleUtil.settings.Setting;
import me.kokoniara.kokoMod.module.moduleUtil.settings.SettingsManager;
import me.kokoniara.kokoMod.module.modules.macros.macroUtil.cropType;
import me.kokoniara.kokoMod.module.modules.macros.macroUtil.lastLeftOff.lastLeftOff;
import me.kokoniara.kokoMod.module.modules.macros.macroUtil.lastLeftOff.lastleftoffObject;
import me.kokoniara.kokoMod.module.modules.macros.macroUtil.macroStages;
import me.kokoniara.kokoMod.util.renderUtil.hehe;
import me.kokoniara.kokoMod.util.sendChatMessage;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;


public class test extends Module {
    public test() {
        super("test", "test", Category.MISC, true, " hello test", " rip test");
        SettingsManager.getSettingsManager().rSetting(new Setting("interpolate steps", this, 10, 1, 60, true));
        SettingsManager.getSettingsManager().rSetting(new Setting("wanted Yaw", this, 90, 1, 90, false));
        SettingsManager.getSettingsManager().rSetting(new Setting("wanted Pitch", this, 1, 1, 90, false));
    }
boolean firstTime = false;
    long interpolateTimer;
    int stepCount = 100;
    int currentStep = 1;
    float wantedPitch, wantedYaw;
    float enableYaw, enablePitch;
    float diffranceYaw, diffrancePitch;


    @SubscribeEvent
    public void onWorldRender(RenderWorldLastEvent e) {
        hehe.drawTextAtWorld("hehe", 0, 10, 0, Integer.parseInt("47a8ed", 16), 0.10F, false, true, e.partialTicks);

    }

//    @SubscribeEvent
//    public void onTick(TickEvent.ClientTickEvent e ){
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
//    }

    @Override
    public void onEnable() {
        super.onEnable();
        lastLeftOff.registerLastLeftOff(new lastleftoffObject(10, 10, 10, cropType.WART, macroStages.DEFAULT));

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
        lastLeftOff.nullLastLeftOff();
//        currentStep = 1;
    }


}
