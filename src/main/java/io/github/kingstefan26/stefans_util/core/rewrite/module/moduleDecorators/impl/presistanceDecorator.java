package io.github.kingstefan26.stefans_util.core.rewrite.module.moduleDecorators.impl;

import io.github.kingstefan26.stefans_util.core.config.configObject;
import io.github.kingstefan26.stefans_util.core.rewrite.module.moduleDecorators.Idecorator;

public class presistanceDecorator extends Idecorator {
    configObject presistanceenabled;

    @Override
    public void onLoad() {
        super.onLoad();
        presistanceenabled = new configObject(this.parentModule.getName() + "presistanceDecorator","presistance", false);
        updateParentModState();
    }

    public boolean isPresidentaceEnabled() {
        return presistanceenabled.getBooleanValue();
    }

    public void setPresistanceState(boolean val){
        presistanceenabled.setBooleanValue(val);
    }

    public void updateParentModState(){
        parentModule.setToggled(presistanceenabled.getBooleanValue());
    }

    public void toggleParentModState(){
        presistanceenabled.setBooleanValue(!presistanceenabled.getBooleanValue());
        updateParentModState();
    }

}
