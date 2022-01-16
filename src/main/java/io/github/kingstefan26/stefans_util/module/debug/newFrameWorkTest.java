package io.github.kingstefan26.stefans_util.module.debug;

import io.github.kingstefan26.stefans_util.core.module.ModuleMenagers.moduleManager;
import io.github.kingstefan26.stefans_util.core.module.moduleDecorators.impl.keyBindDecorator;
import io.github.kingstefan26.stefans_util.core.module.moduleDecorators.impl.onoffMessageDecorator;
import io.github.kingstefan26.stefans_util.core.module.moduleDecorators.impl.presistanceDecorator;
import io.github.kingstefan26.stefans_util.core.module.moduleDecorators.impl.visibleDecorator;
import io.github.kingstefan26.stefans_util.core.module.moduleFrames.basicModule;
import io.github.kingstefan26.stefans_util.core.setting.impl.*;
import io.github.kingstefan26.stefans_util.util.CustomFont;
import io.github.kingstefan26.stefans_util.util.renderUtil.drawCenterString;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.awt.*;
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


    @Override
    public void onLoad(){
        new CheckSetting("check test", this, false, (newvalue)->{

        });
        new SliderNoDecimalSetting("SliderNoDecimalSetting test", this, 10, 1, 50, (newvalue)->{

        });
        new SliderSetting("SliderSetting test", this, 10.0D, 1, 50, (newvalue) -> {

        });
        new MultichoiseSetting("MultichoiseSetting test", this, "test1", new ArrayList<String>() {{
            add("test0");
            add("test1");
            add("test2");
        }}, (newvalue) -> {

        });

        new ChoseAKeySetting("ChoseAKeySetting test", this, 1, (newvalue) -> {
        });


        super.onLoad();
    }
    CustomFont c = new CustomFont(new Font("JetBrains Mono", Font.BOLD, 20), 20);

    @SubscribeEvent
    public void onRenderTick(TickEvent.RenderTickEvent r){
        drawCenterString.drawCenterStringOnScreen(mc, "am i work", "FFFFFF");
    }

    @Override
    public void onGuiRender(RenderGameOverlayEvent e) {
        if(e.type == RenderGameOverlayEvent.ElementType.TEXT){
            c.drawString("bone", 100, 100, 0xFFFFFFFF, 0.2F);
        }
    }
}