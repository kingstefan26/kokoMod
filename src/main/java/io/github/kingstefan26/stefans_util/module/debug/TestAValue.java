/*
 * Copyright (c) 2022. All copyright reserved
 */

package io.github.kingstefan26.stefans_util.module.debug;

import io.github.kingstefan26.stefans_util.core.module.moduleframes.prototypeModule;
import io.github.kingstefan26.stefans_util.util.CustomFont;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

import java.awt.*;

public class TestAValue extends prototypeModule {
    public static String[] ToDraw = new String[]{"", "", "", "", ""};
    CustomFont c = new CustomFont(new Font("JetBrains Mono", Font.BOLD, 20), 20);


    public TestAValue() {
        super("TestAValue");
    }

    @Override
    public void onGuiRender(RenderGameOverlayEvent e) {
        if (e.type != RenderGameOverlayEvent.ElementType.TEXT) return;

        int a = 10;
        for (String s : ToDraw) {
            a += c.getStringHeight(s) + 2;
            c.drawString(s, 100, a, 0xFFFFFFFF, 1);
        }
    }

    @Override
    protected void PROTOTYPETEST() {

    }
}
