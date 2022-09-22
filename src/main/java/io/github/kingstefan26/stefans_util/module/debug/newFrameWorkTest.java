package io.github.kingstefan26.stefans_util.module.debug;

import io.github.kingstefan26.stefans_util.core.module.moduleDecorators.impl.keyBindDecorator;
import io.github.kingstefan26.stefans_util.core.module.moduleDecorators.impl.onoffMessageDecorator;
import io.github.kingstefan26.stefans_util.core.module.moduleDecorators.impl.presistanceDecorator;
import io.github.kingstefan26.stefans_util.core.module.moduleDecorators.impl.visibleDecorator;
import io.github.kingstefan26.stefans_util.core.module.moduleframes.BasicModule;
import io.github.kingstefan26.stefans_util.core.module.modulemenagers.ModuleManager;
import io.github.kingstefan26.stefans_util.core.setting.attnotationSettings.attnotaions.*;
import io.github.kingstefan26.stefans_util.core.setting.impl.*;
import io.github.kingstefan26.stefans_util.util.CustomFont;
import io.github.kingstefan26.stefans_util.util.renderUtil.drawCenterString;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;

import java.awt.*;

public class newFrameWorkTest extends BasicModule {
    public newFrameWorkTest() {
        super("newFrameWorkTest", "testing the new framework", ModuleManager.Category.DEBUG,
                new onoffMessageDecorator(),
                new keyBindDecorator("cocok and bones"),
                new presistanceDecorator(),
                new visibleDecorator()
        );
    }

    @boolenSetting(name = "example boolean", defaultValue = true)
    boolean renderRed;

    @slidersetting(name = "example float setting", min = 0, max = 1, defaultValue = 0.4D)
    double floatsetting;

    @slidernodecimalsetting(name = "example float setting", min = 0, max = 2, defaultValue = 1)
    double intsettnig;

    @choseakeysetting(name = "example key setting", defaultValue = Keyboard.KEY_UP)
    int keysetting;

    @multiChoisesetting(name = "example option setting", defaultValue = "up", options = {"up", "down"})
    String optionsetting;

    @Override
    public void onLoad() {
        new CheckSetting("check test", this, false, (newvalue) -> {

        });
        new SliderNoDecimalSetting("SliderNoDecimalSetting test", this, 10, 1, 50, (newvalue) -> {

        });
        new SliderSetting("SliderSetting test", this, 10.0D, 1, 50, (newvalue) -> {

        });
        new MultichoiseSetting("MultichoiseSetting test", this, "test1", new String[]{"test1", "test2", "test3"}, (newvalue) -> {

        });

        new ChoseAKeySetting("ChoseAKeySetting test", this, 1, (newvalue) -> {
        });


        super.onLoad();
    }
    CustomFont c = new CustomFont(new Font("JetBrains Mono", Font.BOLD, 20), 20);

    @SubscribeEvent
    public void onRenderTick(TickEvent.RenderTickEvent r){
        drawCenterString.drawCenterStringOnScreen(mc, "am i work", renderRed ? "FF0000" : "FFFFFF");
    }

    @Override
    public void onGuiRender(RenderGameOverlayEvent e) {
        if(e.type == RenderGameOverlayEvent.ElementType.TEXT){
            c.drawString("bone", 100, 100, 0xFFFFFFFF, 0.2F);
        }
    }
}