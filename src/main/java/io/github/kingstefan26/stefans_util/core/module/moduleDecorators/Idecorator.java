package io.github.kingstefan26.stefans_util.core.module.moduleDecorators;

import io.github.kingstefan26.stefans_util.core.module.moduleframes.BasicModule;

public class Idecorator implements decoratorInterface{
    public BasicModule parentModule;


    @Override
    public void onEnable() {
    }

    @Override
    public void onDisable() {
    }

    @Override
    public void onLoad() {
    }

    @Override
    public void onInit(BasicModule a) {
        parentModule = a;
    }

    @Override
    public void onUnload() {
    }
}
