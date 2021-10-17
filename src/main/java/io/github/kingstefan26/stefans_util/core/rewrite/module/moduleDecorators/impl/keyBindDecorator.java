package io.github.kingstefan26.stefans_util.core.rewrite.module.moduleDecorators.impl;

import io.github.kingstefan26.stefans_util.core.rewrite.module.moduleDecorators.Idecorator;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class keyBindDecorator extends Idecorator {
    public KeyBinding keybind;
    int deafultKeycode;
    String description;

    public keyBindDecorator(int deafultKey, String description) {
        super();
        this.deafultKeycode = deafultKey;
        this.description = description;
    }
    public keyBindDecorator(String description) {
        super();
        this.description = description;
    }
    @Override
    public void onLoad(){
        super.onLoad();
        this.keybind = new KeyBinding(description, deafultKeycode, "kokoMod");
        ClientRegistry.registerKeyBinding(keybind);
    }
    public void fireKeyBind(){
        parentModule.toggle();
    }
}