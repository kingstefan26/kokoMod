package io.github.kingstefan26.kokomod.core.module.blueprints;

public interface moduleCallables {
    void init();
    void onTick();
    void onWorldRender();
    void onPlayerFall();
    void onPlayerTeleport();
    void close();
}
