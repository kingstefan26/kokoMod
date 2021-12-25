package io.github.kingstefan26.stefans_util.core.rewrite.setting.general;

import io.github.kingstefan26.stefans_util.core.config.configObject;
import io.github.kingstefan26.stefans_util.core.rewrite.module.moduleFrames.basicModule;

import java.util.function.Consumer;

public class AbstractSetting {
    protected String name;
    protected basicModule parent;
    protected configObject ConfigObject;
    protected SettingsCore.type type;
    protected String comment;
    protected final Consumer<Object> callback;
    public AbstractSetting(String name, basicModule parentModule, SettingsCore.type type, Consumer<Object> callback) {
        this.name = name;
        this.parent = parentModule;
        this.type = type;
        this.callback = callback;
//        if(SettingsCore.getSettingsCore().doesSettingExist(name)) throw new IllegalStateException("Name is already registered");
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

    public basicModule getParent() {
        return this.parent;
    }

    public String getComment() {
        return comment;
    }
}
