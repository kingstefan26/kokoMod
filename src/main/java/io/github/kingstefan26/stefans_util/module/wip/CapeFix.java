/*
 * Copyright (c) 2022. All copyright reserved
 */

package io.github.kingstefan26.stefans_util.module.wip;

import io.github.kingstefan26.stefans_util.core.module.ModuleManager;
import io.github.kingstefan26.stefans_util.core.module.moduleDecorators.impl.presistanceDecorator;
import io.github.kingstefan26.stefans_util.core.module.moduleframes.BasicModule;

public class CapeFix extends BasicModule {
    public static boolean enabled = false;

    public CapeFix() {
        super("CapeFix", "Make Cape not go erect when high speed", ModuleManager.Category.RENDER, new presistanceDecorator());
    }

    @Override
    public void onEnable() {
        enabled = true;
        super.onEnable();
    }

    @Override
    public void onDisable() {
        enabled = false;
        super.onDisable();
    }
}
