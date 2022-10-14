package io.github.kingstefan26.stefans_util.module.debug;

import io.github.kingstefan26.stefans_util.core.module.moduleframes.prototypeModule;
import io.github.kingstefan26.stefans_util.core.setting.impl.EnumSetting;
import io.github.kingstefan26.stefans_util.core.setting.impl.SliderNoDecimalSetting;
import io.github.kingstefan26.stefans_util.core.setting.impl.SliderSetting;
import io.github.kingstefan26.stefans_util.service.impl.chatService;
import io.github.kingstefan26.stefans_util.service.impl.headrotation.HeadControlService;
import io.github.kingstefan26.stefans_util.service.impl.headrotation.interpolator.IInterpolator;
import io.github.kingstefan26.stefans_util.service.impl.headrotation.interpolator.impl.*;
import io.github.kingstefan26.stefans_util.service.serviceMenager;
import io.github.kingstefan26.stefans_util.util.ModuleUtil;
import io.github.kingstefan26.stefans_util.util.StefanutilUtil;
import net.minecraft.util.Vec3;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import javax.vecmath.Vector2d;
import javax.vecmath.Vector2f;
import java.text.DecimalFormat;
import java.util.Objects;

public class HeadRotationDebug extends prototypeModule {

    private static DecimalFormat decimalFormatter = new DecimalFormat("#.##");
    Mode lastTghing = null;
    int mineAction;
    float tf;
    private boolean track;
    private int tickCouner;
    private double logspeed;
    private double bi1;
    private double bi2;
    private double updatespeed;
    private Mode mode;
    private TestType testtype;

    public HeadRotationDebug() {
        super("HeadRotationDebug");
    }

    public static Vector2f PointOnCircle(float radius, float angleInDegrees, Vector2f origin) {
        // Convert from degrees to radians via multiplication by PI/180
        float x = (float) (radius * Math.cos(angleInDegrees * Math.PI / 180F)) + origin.x;
        float y = (float) (radius * Math.sin(angleInDegrees * Math.PI / 180F)) + origin.y;

        return new Vector2f(x, y);
    }

    @Override
    public void onLoad() {

        new EnumSetting<>("test-type", this, TestType.block, TestType.values(), nval -> this.testtype = TestType.valueOf(nval));
        new EnumSetting<>("Rotate mode", this, Mode.logarithmic, Mode.values(), nval -> this.mode = Mode.valueOf(nval));

        new SliderSetting("Logth speed", this, 10, 1, 200, (newvalue) -> {
            logspeed = newvalue * 0.001;
        });

        new SliderSetting("Bicube #1", this, 10, 1, 180, (newvalue) -> {
            bi1 = newvalue;
        });
        new SliderSetting("Bicube #2", this, 10, 1, 180, (newvalue) -> {
            bi2 = newvalue;
        });

        new SliderNoDecimalSetting("Update speed", this, 10, 1, 200, nval -> updatespeed = nval);


        super.onLoad();
    }

    @Override
    protected void PROTOTYPETEST() {

        IInterpolator iInterpolator;

        switch (mode) {
            case logarithmic:
                iInterpolator = new LogarithmicInterpolator((float) logspeed);
                break;
            case bicubic:
                iInterpolator = new BicubicInterpolator((float) bi1, (float) bi2);
                break;
            case liniar:
                iInterpolator = new LinearInterpolator();
                break;
            case cosine:
                iInterpolator = new CosineInterpolator();
                break;
            default:
                iInterpolator = new NoInterpolator();
                break;
        }
        HeadControlService h = (HeadControlService) serviceMenager.getService(HeadControlService.class.getName());

        h.setInterpolator(iInterpolator);

        lastTghing = mode;

        this.track = !this.track;

        if (track) {
            new Thread(() -> {
                float updateFrequency = 50;
                float stepSize = (1 / updateFrequency);
                logger.info("Step size :{}", stepSize);
                int currentStep = 0;

                while (track && ModuleUtil.isOnWorldCheck()) {
                    if (!isToggled()) return;

                    if (currentStep >= updateFrequency) {
                        currentStep = 0;
                    }

                    float v = Float.parseFloat(decimalFormatter.format(currentStep * stepSize));
                    logger.info("Partial Tick" + v);
                    h.setPartialTick(currentStep * stepSize);

                    h.update();

                    try {
                        Thread.sleep((long) updatespeed);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                    currentStep++;

                }

            }).start();
        }


    }

    @Override
    public void onTick(TickEvent.ClientTickEvent e) {
        if (e.phase != TickEvent.Phase.START && !ModuleUtil.isOnWorldCheck()) return;
        if (!track) return;

        if (!Objects.equals(lastTghing, mode)) {
            chatService.queueCleanChatMessage("Changed mode turning off");
            track = false;
        }

        HeadControlService h = (HeadControlService) serviceMenager.getService(HeadControlService.class.getName());


        if (testtype == TestType.mining) {
            tickCouner = StefanutilUtil.every20Ticks(e, tickCouner, () -> {
                mineAction++;
                if (mineAction >= 4) mineAction = 0;
            });

            Vec3 target = null;

            switch (mineAction) {
                case 0:
                    target = new Vec3(1, 4, 1.35);
                    break;
                case 1:
                    target = new Vec3(1, 3, 1.5);
                    break;
                case 2:
                    target = new Vec3(1, 3, 2.4);
                    break;
                case 3:
                    target = new Vec3(1, 4, 2.5);
                    break;
            }

            Vector2d dick = h.faceTarget(target);
            h.setPlayerRotations((float) dick.getX(), (float) dick.getY());
        }


        if (testtype == TestType.block) {
            Vector2d dick = h.faceTarget(new Vec3(1, 4, 1));
            h.setPlayerRotations((float) dick.getX(), (float) dick.getY());
        }

        if (testtype == TestType.moving_target) {
            if (tf >= 360) {
                tf = 0;
            } else {
                tf += 30;
            }

            Vec3 a = mc.thePlayer.getLookVec();

            Vector2f pointOnACritle = PointOnCircle(10, tf, new Vector2f((float) a.xCoord, (float) a.zCoord));

            float y = (float) (a.yCoord + 10);
            float x = pointOnACritle.x;
            float z = pointOnACritle.y;

            Vector2d dick = h.faceTarget(new Vec3(x, y, z));
            h.setPlayerRotations((float) dick.getX(), (float) dick.getY());
        }

    }

    @Override
    public void onRenderTick(TickEvent.RenderTickEvent e) {
        if (!ModuleUtil.isOnWorldCheck() || !track) return;


//        h.setPartialTick(e.renderTickTime);
    }

    enum Mode {
        logarithmic, bicubic, insta, liniar, cosine
    }

    enum TestType {
        block, moving_target, mining
    }
}