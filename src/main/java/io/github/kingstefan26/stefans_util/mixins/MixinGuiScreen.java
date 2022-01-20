/*
 * Copyright (c) 2022. All copyright reserved
 */

package io.github.kingstefan26.stefans_util.mixins;

import io.github.kingstefan26.stefans_util.module.render.blurClickGui;
import net.minecraft.client.gui.GuiScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = GuiScreen.class, remap = false)
public class MixinGuiScreen extends GuiScreen {

    @Inject(method = "drawWorldBackground(I)V", at = @At("HEAD"), cancellable = true)
    public void drawWorldBackground(int tint, CallbackInfo ci) {
        if (this.mc.theWorld != null) {
            this.drawGradientRect(0, 0, this.width, this.height, blurClickGui.getBackgroundColor(false), blurClickGui.getBackgroundColor(true));
        } else {
            this.drawBackground(tint);
        }
        ci.cancel();
    }

}
