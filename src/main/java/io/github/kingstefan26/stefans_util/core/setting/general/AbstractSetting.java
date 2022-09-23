package io.github.kingstefan26.stefans_util.core.setting.general;

import io.github.kingstefan26.stefans_util.core.config.prop.Iproperty;
import io.github.kingstefan26.stefans_util.core.module.moduleframes.BasicModule;

import java.util.function.Consumer;

public abstract class AbstractSetting<T> {
    protected String name;
    protected BasicModule parent;
//    protected configObject ConfigObject;

    protected final Consumer<T> callback;

    protected SettingsCore.type type;
    protected String comment;
    protected Iproperty<T> prop;

    protected AbstractSetting(String name, BasicModule parentModule, SettingsCore.type type, Consumer<T> callback) {
        this.name = name;
        this.parent = parentModule;
        this.type = type;
        this.callback = callback;
        addToSettingsCore();
    }

    public void addToSettingsCore() {
        SettingsCore.getSettingsCore().addSetting(this);
    }

    public void removeFromSettingsCore(){
        SettingsCore.getSettingsCore().removeSetting(this);
    }

    public String getName() {
        return this.name;
    }

    public SettingsCore.type getType() {
        return this.type;
    }

    public BasicModule getParent() {
        return this.parent;
    }

    public String getComment() {
        return comment;
    }

    public T getValue() {
//        return this.ConfigObject.getBooleanValue();
        return (T) prop.getProperty();
    }

    public void setValue(T value) {
//        this.ConfigObject.setBooleanValue(value);
        prop.setProperty(value);

        if (this.callback != null) this.callback.accept(value);
    }
}
