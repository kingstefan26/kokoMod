package io.github.kingstefan26.stefans_util.module.debug;

import io.github.kingstefan26.stefans_util.core.rewrite.module.ModuleMenagers.moduleManager;
import io.github.kingstefan26.stefans_util.core.rewrite.module.moduleDecorators.impl.keyBindDecorator;
import io.github.kingstefan26.stefans_util.core.rewrite.module.moduleDecorators.impl.presistanceDecorator;
import io.github.kingstefan26.stefans_util.core.rewrite.module.moduleDecorators.impl.visibleDecorator;
import io.github.kingstefan26.stefans_util.core.rewrite.module.moduleFrames.basicModule;

import io.github.kingstefan26.stefans_util.core.rewrite.module.moduleDecorators.impl.onoffMessageDecorator;
import io.github.kingstefan26.stefans_util.core.rewrite.setting.impl.CheckSetting;
import io.github.kingstefan26.stefans_util.core.rewrite.setting.impl.MultichoiseSetting;
import io.github.kingstefan26.stefans_util.core.rewrite.setting.impl.SliderNoDecimalSetting;
import io.github.kingstefan26.stefans_util.core.rewrite.setting.impl.SliderSetting;
import io.github.kingstefan26.stefans_util.util.renderUtil.drawCenterString;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;

public class newFrameWorkTest extends basicModule {
    public newFrameWorkTest(){
        super("newFrameWorkTest", "testing the new framework", moduleManager.Category.DEBUG,
                new onoffMessageDecorator(),
                new keyBindDecorator("cocok and bones"),
                new presistanceDecorator(),
                new visibleDecorator()
        );
    }

    CheckSetting checkSetting1;

    @Override
    public void onLoad(){
        checkSetting1 = new CheckSetting("check test", this, false);
        new SliderNoDecimalSetting("SliderNoDecimalSetting test", this, 10, 1, 50);
        new SliderSetting("SliderSetting test", this, 10.0D, 1, 50);
        new MultichoiseSetting("MultichoiseSetting test", this, "test1", new ArrayList<String>() {{
            add("test0");
            add("test1");
            add("test2");
        }});
        super.onLoad();
    }

    @SubscribeEvent
    public void onRenderTick(TickEvent.RenderTickEvent r){
        drawCenterString.drawCenterStringOnScreen(mc, "am i work", "FFFFFF");
    }
}