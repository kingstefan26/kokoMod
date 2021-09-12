package me.kokoniara.kokoMod.module.moduleUtil.module;

import me.kokoniara.kokoMod.util.sendChatMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;

import java.util.UUID;


public class Module {
	final String uuid = UUID.randomUUID().toString().replace("-", "");

	protected static Minecraft mc = Minecraft.getMinecraft();
	
	private String name, description, enableMessage, disableMessage;
	private int key;



	private Category category;
	private boolean toggled;
	public boolean visible = true;
	private boolean keybindEnabled = false;
	private boolean enabledisableMessage;
	private KeyBinding fmlkeybindObject;


	public Module(String name, String description, Category category,boolean keybindEnabled, String enableMessage, String disableMessage) {
		super();
		this.name = name;
		this.enableMessage = enableMessage;
		this.disableMessage = disableMessage;
		this.description = description;
		this.enabledisableMessage = true;
		this.key = key;
		this.keybindEnabled = keybindEnabled;
		if(keybindEnabled){
			this.fmlkeybindObject = new KeyBinding(this.description, key, "kokoMod");
			ClientRegistry.registerKeyBinding(this.fmlkeybindObject);
		}
		this.category = category;
		this.toggled = false;

	}

	public Module(String name, String description, Category category,boolean keybindEnabled) {
		super();
		this.name = name;
		this.enableMessage = enableMessage;
		this.disableMessage = disableMessage;
		this.description = description;
		this.key = key;
		this.keybindEnabled = keybindEnabled;
		if(keybindEnabled){
			this.fmlkeybindObject = new KeyBinding(this.description, key, "kokoMod");
			ClientRegistry.registerKeyBinding(this.fmlkeybindObject);
		}
		this.category = category;
		this.toggled = false;

	}

	public Module(String name, String description, Category category) {
		super();
		this.name = name;
		this.description = description;
		this.key = key;
		this.category = category;
		this.toggled = false;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getKey() {
		if(keybindEnabled){
			this.key = this.fmlkeybindObject.getKeyCode();
		}
		return this.key;
	}
	public KeyBinding getKeyBindingObj() {
		return this.fmlkeybindObject;
	}

	public void setKey(int key) {
		this.key = key;
		if(keybindEnabled){
			this.fmlkeybindObject.setKeyCode(key);
		}
	}

	public boolean isToggled() {
		return toggled;
	}

	public void setToggled(boolean toggled) {
		this.toggled = toggled;
		
		if (this.toggled) {
			this.onEnable();
		} else {
			this.onDisable();
		}
	}
	
	public void toggle() {
		this.toggled = !this.toggled;
		
		if (this.toggled) {
			this.onEnable();
		} else {
			this.onDisable();
		}
	}
	
	public void onEnable() {
		MinecraftForge.EVENT_BUS.register(this);
		if(this.enabledisableMessage) sendChatMessage.sendClientMessage(this.enableMessage, true);
	}
	
	public void onDisable() {
		MinecraftForge.EVENT_BUS.unregister(this);
		if(this.enabledisableMessage) sendChatMessage.sendClientMessage(this.disableMessage, true);
	}
	
	public String getName() {
		return this.name;
	}
	
	public Category getCategory() {
		return this.category;
	}
}
