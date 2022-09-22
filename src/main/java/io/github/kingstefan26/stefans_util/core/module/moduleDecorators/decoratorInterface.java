package io.github.kingstefan26.stefans_util.core.module.moduleDecorators;


import io.github.kingstefan26.stefans_util.core.module.moduleframes.BasicModule;

public interface decoratorInterface {
    void onEnable();

    void onDisable();

    void onLoad();

    void onInit(BasicModule a);

    void onUnload();
}
