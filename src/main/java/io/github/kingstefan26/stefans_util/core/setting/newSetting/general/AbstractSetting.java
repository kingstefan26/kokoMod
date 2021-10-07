package io.github.kingstefan26.stefans_util.core.setting.newSetting.general;

import io.github.kingstefan26.stefans_util.core.config.configObject;
import io.github.kingstefan26.stefans_util.core.module.Module;

public class AbstractSetting {
    protected String name;
    protected Module parent;
    protected configObject ConfigObject;
    protected SettingsCore.type type;
    protected String comment;

    public AbstractSetting(String name, Module parentModule, SettingsCore.type type) {
        this.name = name;
        this.parent = parentModule;
        this.type = type;
        addToSettingsCore();
    }

    public void addToSettingsCore(){
        SettingsCore.getSettingsCore().addSetting(this);
    }

    public void removeFromSettingsCore(){
        SettingsCore.getSettingsCore().removeSetting(this);
    }

    public String getName() {
        return this.name;
    }

    public SettingsCore.type getType() { return this.type;}

    public Module getParent() {
        return this.parent;
    }

    public String getComment() {
        return comment;
    }
}
