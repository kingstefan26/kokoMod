/*
 * Copyright (c) 2021-2022. All copyright reserved
 */

package io.github.kingstefan26.stefans_util.module.misc;

import io.github.kingstefan26.stefans_util.core.module.ModuleManager;
import io.github.kingstefan26.stefans_util.core.module.moduleDecorators.impl.keyBindDecorator;
import io.github.kingstefan26.stefans_util.core.module.moduleframes.BasicModule;
import io.github.kingstefan26.stefans_util.core.setting.impl.MultichoiseSetting;
import io.github.kingstefan26.stefans_util.core.setting.impl.SliderNoDecimalSetting;
import io.github.kingstefan26.stefans_util.core.setting.impl.SliderSetting;
import io.github.kingstefan26.stefans_util.service.impl.headrotation.HeadControlService;
import io.github.kingstefan26.stefans_util.service.impl.headrotation.interpolator.impl.LogarithmicInterpolator;
import io.github.kingstefan26.stefans_util.service.serviceMenager;
import net.minecraftforge.client.event.RenderWorldLastEvent;

public class headPosLock extends BasicModule {
    static double yaw;

    public headPosLock() {
        super("headPosLock", "you can chose a angle to lock ur head at", ModuleManager.Category.MISC, new keyBindDecorator("headLockPos"));
    }

    static double pitch;
    private String mode;
    static float rateOfChange = 0.01F;

    @Override
    public void onLoad() {
//        new MultichoiseSetting("yaw", this, "none", new String[]{"180", "90", "45", "0", "-45", "-90", "-180", "none"}, (newvalue) -> {
//            yaw = (String) newvalue;
//        });
//        new MultichoiseSetting("pitch", this, "none", new String[]{"90", "45", "0", "-45", "-90", "none"}, (newvalue) -> {
//            pitch = (String) newvalue;
//        });

        new SliderNoDecimalSetting("yaw", this, 0, -180, 180, (newvalue) -> yaw = newvalue);
        new SliderNoDecimalSetting("pitch", this, 0, -90, 90, (newvalue) -> pitch = newvalue);

        new MultichoiseSetting("mode", this, "both", new String[]{"both", "yaw", "pitch"}, mval -> mode = mval);

        new SliderSetting("rate of change", this, 0.01, 0, 1, (newvalue) -> rateOfChange = (float) (double) newvalue);

        super.onLoad();
    }


    @Override
    public void onWorldRender(RenderWorldLastEvent event) {
        if (mc.currentScreen == null && !mc.isGamePaused()) {
            HeadControlService contol = (HeadControlService) serviceMenager.getService(HeadControlService.class.getName());
            if (contol == null) return;

            contol.setInterpolator(new LogarithmicInterpolator(rateOfChange));

            switch (mode) {
                case "yaw":
                    if (mc.thePlayer.rotationYaw != yaw) {
                        contol.setPlayerYaw((float) yaw);
                    }
                    break;
                case "pitch":
                    if (mc.thePlayer.rotationPitch != pitch) {
                        contol.setPlayerPitch((float) pitch);
                    }
                    break;
                case "both":
                    if (mc.thePlayer.rotationPitch != pitch && mc.thePlayer.rotationYaw != yaw) {
                        contol.setPlayerRotations((float) yaw, (float) pitch);
                    }
                    break;
                default:
                    throw new IllegalArgumentException("mode cannot be: \"" + mode + "\"");
            }
        }
    }
}
