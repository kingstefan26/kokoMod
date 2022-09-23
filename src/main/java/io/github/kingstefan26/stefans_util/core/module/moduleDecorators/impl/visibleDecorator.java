package io.github.kingstefan26.stefans_util.core.module.moduleDecorators.impl;

import io.github.kingstefan26.stefans_util.core.config.ConfigManager;
import io.github.kingstefan26.stefans_util.core.config.prop.impl.boolProp;
import io.github.kingstefan26.stefans_util.core.module.moduleDecorators.Idecorator;
import io.github.kingstefan26.stefans_util.module.render.HUD;

public class visibleDecorator extends Idecorator {

    boolProp prop;
    @Override
    public void onLoad() {
        super.onLoad();
        HUD.visibleDecorators.add(this);

        prop = (boolProp) ConfigManager.getInstance().getConfigObject(this.parentModule.getName() + "." + "visibility", false);

    }

    public boolean isVisibilityEnabled() {
        return prop.getProperty();
    }

    public void setVisibilityState(boolean val){
        prop.set(val);
    }

    public void toggleVisibility(){
        prop.set(!prop.getProperty());
    }
}
