package io.github.kingstefan26.stefans_util.module.debug;

import io.github.kingstefan26.stefans_util.core.clickgui.newGui.newClickGui;
import io.github.kingstefan26.stefans_util.core.clickgui.oldGui.ClickGui;
import io.github.kingstefan26.stefans_util.core.module.Module;
import io.github.kingstefan26.stefans_util.core.module.ModuleManager;
import io.github.kingstefan26.stefans_util.core.setting.Setting;
import io.github.kingstefan26.stefans_util.core.setting.SettingsManager;
import io.github.kingstefan26.stefans_util.core.setting.newSetting.impl.CheckSetting;
import io.github.kingstefan26.stefans_util.core.setting.newSetting.impl.MultichoiseSetting;
import io.github.kingstefan26.stefans_util.core.setting.newSetting.impl.SliderNoDecimalSetting;
import io.github.kingstefan26.stefans_util.core.setting.newSetting.impl.SliderSetting;

import java.util.ArrayList;

public class newFrameWorkTest extends Module {
    public newFrameWorkTest(){
        super("newFrameWorkTest", "testing the new framework", ModuleManager.Category.DEBUG);
    }
    @Override
    public void onLoad(){
        new CheckSetting("check test", this, false);
        new SliderNoDecimalSetting("SliderNoDecimalSetting test", this, 10, 1, 50);
        new SliderSetting("SliderSetting test", this, 10.0D, 1, 50);
        new MultichoiseSetting("MultichoiseSetting test", this, "test1", new ArrayList<String>() {{
            add("test0");
            add("test1");
            add("test2");
        }});
        newClickGui.getClickGui().registerComponent(this);
        super.onLoad();
    }
}
