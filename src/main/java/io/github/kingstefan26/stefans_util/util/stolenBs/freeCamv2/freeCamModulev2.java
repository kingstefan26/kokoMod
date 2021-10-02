package io.github.kingstefan26.stefans_util.util.stolenBs.freeCamv2;

import io.github.kingstefan26.stefans_util.core.module.ModuleManager;
import io.github.kingstefan26.stefans_util.core.module.blueprints.Module;
import net.minecraft.client.Minecraft;

public class freeCamModulev2 extends Module {

        public freeCamModulev2()
        {
            super("freecamv2", "frees ur cam!", ModuleManager.Category.RENDER, true);
        }

        private FreecamEntity freecamEnt;

        @Override
        public void onEnable() {
            freecamEnt = new FreecamEntity();
        }

        @Override
        public void onDisable() {
            freecamEnt.resetPlayerPosition();
            freecamEnt.despawn();
            Minecraft.getMinecraft().renderGlobal.loadRenderers();
        }

}
