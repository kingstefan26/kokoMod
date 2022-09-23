package io.github.kingstefan26.stefans_util.core.module.moduleDecorators.impl;

import io.github.kingstefan26.stefans_util.core.config.ConfigManager;
import io.github.kingstefan26.stefans_util.core.config.prop.impl.boolProp;
import io.github.kingstefan26.stefans_util.core.module.moduleDecorators.Idecorator;

public class presistanceDecorator extends Idecorator {

    boolProp prop;
    @Override
    public void onLoad() {
        super.onLoad();
        prop = (boolProp) ConfigManager.getInstance().getConfigObject(this.parentModule.getName() + "." + "visibility", false);
        updateParentModState();
    }

    public boolean isPresidentaceEnabled() {
        return prop.getProperty();
    }

    public void setPresistanceState(boolean val){
        prop.set(val);
    }

    public void updateParentModState(){
        parentModule.setToggled(prop.getProperty());
    }

    public void toggleParentModState(){
        prop.set(!prop.getProperty());
        updateParentModState();
    }

}
