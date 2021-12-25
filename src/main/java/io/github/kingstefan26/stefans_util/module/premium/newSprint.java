package io.github.kingstefan26.stefans_util.module.premium;

import io.github.kingstefan26.stefans_util.core.rewrite.module.ModuleMenagers.moduleManager;
import io.github.kingstefan26.stefans_util.core.rewrite.module.moduleDecorators.impl.onoffMessageDecorator;
import io.github.kingstefan26.stefans_util.core.rewrite.module.moduleDecorators.impl.presistanceDecorator;
import io.github.kingstefan26.stefans_util.core.rewrite.module.moduleFrames.basicModule;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class newSprint extends basicModule {
    public newSprint() {
        super("newSprint", "sprints!", moduleManager.Category.PLAYER,
                new onoffMessageDecorator(),
                new presistanceDecorator());
    }

    @Override
    public void onTick(TickEvent.ClientTickEvent e){
        if(mc.thePlayer != null || mc.theWorld != null || !mc.gameSettings.keyBindSprint.isKeyDown()) KeyBinding.setKeyBindState(mc.gameSettings.keyBindSprint.getKeyCode(), true);
    }

    @Override
    public void onDisable() {
        if(mc.thePlayer != null || mc.theWorld != null){
            KeyBinding.setKeyBindState(mc.gameSettings.keyBindSprint.getKeyCode(), false);
        }
        super.onDisable();
    }
}