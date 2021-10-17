package io.github.kingstefan26.stefans_util.core.rewrite.module.moduleDecorators.impl;

import io.github.kingstefan26.stefans_util.core.config.configObject;
import io.github.kingstefan26.stefans_util.core.rewrite.module.moduleDecorators.Idecorator;
import io.github.kingstefan26.stefans_util.module.render.HUD;

public class visibleDecorator extends Idecorator {
    configObject visibilityenabled;

    @Override
    public void onLoad() {
        super.onLoad();
        HUD.visibleDecorators.add(this);
        visibilityenabled = new configObject(this.parentModule.getName() + "visibilityDecorator","visibility", false);
    }

    public boolean isVisibilityEnabled() {
        return visibilityenabled.getBooleanValue();
    }

    public void setVisibilityState(boolean val){
        visibilityenabled.setBooleanValue(val);
    }

    public void toggleVisibility(){
        visibilityenabled.setBooleanValue(!visibilityenabled.getBooleanValue());
    }
}
