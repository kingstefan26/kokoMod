/*
 * Copyright (c) 2021. All copyright reserved
 */

package io.github.kingstefan26.stefans_util.module.premium;

import io.github.kingstefan26.stefans_util.core.rewrite.module.ModuleMenagers.moduleManager;
import io.github.kingstefan26.stefans_util.core.rewrite.module.moduleDecorators.impl.keyBindDecorator;
import io.github.kingstefan26.stefans_util.core.rewrite.module.moduleFrames.basicModule;
import io.github.kingstefan26.stefans_util.core.rewrite.setting.impl.MultichoiseSetting;
import io.github.kingstefan26.stefans_util.core.rewrite.setting.impl.SliderSetting;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.Objects;

public class headPosLock extends basicModule {
    public headPosLock() {
        super("headPosLock", "you can chose a angle to lock ur head at", moduleManager.Category.PLAYER, new keyBindDecorator("headLockPos"));
    }

    static String yaw, pitch;
    static float rateOfChange = 0.01F;

    @Override
    public void onLoad() {
        new MultichoiseSetting("yaw", this, "none", new String[]{"180", "90", "45", "0", "-45", "-90", "-180", "none"}, (newvalue) -> {
            yaw = (String) newvalue;
        });
        new MultichoiseSetting("pitch", this, "none", new String[]{"90", "45", "0", "-45", "-90", "none"}, (newvalue) -> {
            pitch = (String) newvalue;
        });
        new SliderSetting("rate of change", this, 0.01, 0, 1, (newvalue)->{
            double temp = (Double) newvalue;
            rateOfChange = (float) temp;
        });
        super.onLoad();
    }

    public static float lerpAngle(float fromRadians, float toRadians, float progress) {
        float f = ((toRadians - fromRadians) % 360.0F + 540.0F) % 360.0F - 180.0F;
        return fromRadians + f * progress % 360.0F;
    }

    public void setPlayerRotations(float yaw, float pitch) {
        mc.thePlayer.rotationYaw = lerpAngle(mc.thePlayer.rotationYaw, yaw, rateOfChange);
        mc.thePlayer.rotationPitch = lerpAngle(mc.thePlayer.rotationPitch, pitch, rateOfChange);
    }

    public void setPlayerYaw(float yaw) {
        mc.thePlayer.rotationYaw = lerpAngle(mc.thePlayer.rotationYaw, yaw, rateOfChange);
    }
    public void setPlayerpitch(float pitch) {
        mc.thePlayer.rotationPitch = lerpAngle(mc.thePlayer.rotationPitch, pitch, rateOfChange);
    }


    @Override
    public void onTick(TickEvent.ClientTickEvent event) {
        if (mc.currentScreen == null && !mc.isGamePaused()) {
            // only setting yaw
            if (!Objects.equals(yaw, "none") && Objects.equals(pitch, "none")) {
                try {
                    int yawValue = Integer.parseInt(yaw);
                    if (mc.thePlayer.rotationYaw != yawValue) {
                        setPlayerYaw(yawValue);
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

            // only setting pitch
            } else if (Objects.equals(yaw, "none") && !Objects.equals(pitch, "none")) {
                try {
                    int pitchValue = Integer.parseInt(pitch);
                    if (mc.thePlayer.rotationPitch != pitchValue) {
                        setPlayerpitch(pitchValue);
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            // setting both yaw and pitch
            } else if (!Objects.equals(yaw, "none") && !Objects.equals(pitch, "none")) {
                try {
                    int pitchValue = Integer.parseInt(pitch);
                    int yawValue = Integer.parseInt(yaw);
                    if (mc.thePlayer.rotationPitch != pitchValue && mc.thePlayer.rotationYaw != yawValue) {
                        setPlayerRotations(yawValue, pitchValue);
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        }

        super.onTick(event);
    }
}
