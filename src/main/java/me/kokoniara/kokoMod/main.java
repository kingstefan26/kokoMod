package me.kokoniara.kokoMod;

import me.kokoniara.kokoMod.core.kokoMod;
import me.kokoniara.kokoMod.util.renderUtil.updateWidowTitle;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = main.MODID, version = main.VERSION)
public class main {
    public static final String MODID = "kokoMod";
    public static final String VERSION = "0.2.7-APLHA";
    public static final boolean debug = false;

    @EventHandler
    public static void Init(FMLPreInitializationEvent event) {
        updateWidowTitle.updateTitle("Kokoclient V69.420");
        kokoMod.instance = kokoMod.getkokoMod();
        kokoMod.instance.init();
    }
}