package io.github.kingstefan26.kokomod.util.stolenBs.freeCamv2;

import io.github.kingstefan26.kokomod.core.module.Category;
import io.github.kingstefan26.kokomod.core.module.blueprints.Module;
import net.minecraft.client.Minecraft;

public class freeCamModulev2 extends Module {

        public freeCamModulev2()
        {
            super("freecamv2", "frees ur cam!", Category.RENDER, true, " freecam enabled", " freecam disabled");
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
