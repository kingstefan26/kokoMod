package io.github.kingstefan26.kokomod.core.module.blueprints;

import io.github.kingstefan26.kokomod.core.module.Category;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;

public class UtilModule {

	protected static Minecraft mc = Minecraft.getMinecraft();

	private final String name;


	public UtilModule(String name) {
		super();
		this.name = name;
		this.onEnable();
	}

	public void onEnable() {
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	public void onDisable() {
		MinecraftForge.EVENT_BUS.unregister(this);
	}
	
	public String getName() {
		return this.name;
	}
}
