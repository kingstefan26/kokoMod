package io.github.kingstefan26.stefans_util.core.rewrite.module.moduleDecorators;

import io.github.kingstefan26.stefans_util.core.rewrite.module.moduleFrames.basicModule;

public class Idecorator implements decoratorInterface{
    public basicModule parentModule;


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
    public void onInit(basicModule a) {
        parentModule = a;
    }

    @Override
    public void onUnload() {

    }
}
