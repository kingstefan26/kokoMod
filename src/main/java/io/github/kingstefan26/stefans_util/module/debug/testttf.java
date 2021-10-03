package io.github.kingstefan26.stefans_util.module.debug;

import io.github.kingstefan26.stefans_util.core.module.Module;
import io.github.kingstefan26.stefans_util.core.module.ModuleManager;
import io.github.kingstefan26.stefans_util.util.TrueTypeFont;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

import java.awt.*;

public class testttf extends Module {
    public testttf() {
        super("testTtf", "testing ttf", ModuleManager.Category.DEBUG);
    }
    TrueTypeFont trueTypeFont;
    Font font;
    @Override
    public void onLoad() {
        font = new Font("JetBrains Mono", Font.PLAIN, 32);
        trueTypeFont = new TrueTypeFont(font, true, new char[]{'h', 'e', 'l', 'o', 'u', 'v', 's', ' '});
        super.onLoad();
    }
    @Override
    public void onUnload(){
        trueTypeFont.destroy();
        super.onUnload();
    }

    @Override
    public void onGuiRender(RenderGameOverlayEvent e){
        if(e.type == RenderGameOverlayEvent.ElementType.TEXT){
            trueTypeFont.drawString(20, 20, "hello luvs", 20, 20);
        }
    }

}
