package io.github.kingstefan26.stefans_util.module.debug;

import io.github.kingstefan26.stefans_util.core.clickgui.newGui.newClickGui;
import io.github.kingstefan26.stefans_util.core.clickgui.oldGui.ClickGui;
import io.github.kingstefan26.stefans_util.core.module.Module;
import io.github.kingstefan26.stefans_util.core.module.ModuleManager;
import io.github.kingstefan26.stefans_util.core.setting.newSetting.impl.CheckSetting;
import io.github.kingstefan26.stefans_util.core.setting.newSetting.impl.SliderNoDecimalSetting;

public class newFrameWorkTest extends Module {
    public newFrameWorkTest(){
        super("newFrameWorkTest", "testing the new framework", ModuleManager.Category.DEBUG);
    }
    @Override
    public void onLoad(){
        new CheckSetting("yay", this, false);
        new SliderNoDecimalSetting("yay", this, 10, 1, 50);
        newClickGui.getClickGui().registerComponent(this);
        super.onLoad();
    }
}
