package io.github.kingstefan26.stefans_util.module.macro.wart;

import io.github.kingstefan26.stefans_util.core.module.Module;
import io.github.kingstefan26.stefans_util.core.module.ModuleManager;
import io.github.kingstefan26.stefans_util.core.setting.Setting;
import io.github.kingstefan26.stefans_util.core.setting.SettingsManager;

import java.util.ArrayList;

public class UniversalWartMacro extends Module {
    public UniversalWartMacro() {
        super("UniversalWartMacro", "macros wart", ModuleManager.Category.MACRO, true);
    }
    public ArrayList<String> options = new ArrayList<>();
    @Override
    public void onLoad(){
        options.add("vertical design");
        options.add("horizontal with tp pads");
        SettingsManager.getSettingsManager().rSetting(new Setting("yaw", this,"vertical design",options));
        super.onLoad();
    }
}
