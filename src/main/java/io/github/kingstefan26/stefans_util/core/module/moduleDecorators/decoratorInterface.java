package io.github.kingstefan26.stefans_util.core.module.moduleDecorators;


import io.github.kingstefan26.stefans_util.core.module.moduleFrames.basicModule;

public interface decoratorInterface {
    void onEnable();
    void onDisable();
    void onLoad();
    void onInit(basicModule a);
    void onUnload();
}
