/*
 * Copyright (c) 2022. All copyright reserved
 */

package io.github.kingstefan26.stefans_util.mixins;

import io.github.kingstefan26.stefans_util.module.render.blurClickGui;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = GuiScreen.class)
public class MixinGuiScreen /*extends GuiScreen*/ {

    @Shadow
    public Minecraft mc;
    @Shadow
    public int width;
    @Shadow
    public int height;

    @Shadow
    public void drawBackground(int tint) {
    }

    @Shadow
    protected void drawGradientRect(int left, int top, int right, int bottom, int startColor, int endColor) {
    }

    @Inject(method = "drawWorldBackground(I)V", at = @At("HEAD"))
    public void drawWorldBackground(int tint, CallbackInfo ci) {
        if (this.mc.theWorld != null) {
            drawGradientRect(0, 0, this.width, this.height, blurClickGui.getBackgroundColor(false), blurClickGui.getBackgroundColor(true));
        } else {
            drawBackground(tint);
        }
        ci.cancel();
    }


//    @Inject(method = "sendChatMessage(Ljava/lang/String;Z)V", at=@At("HEAD"))
//    public void onSendChatMessage(String message, boolean addToChat, CallbackInfo ci) {
//        SBInfo.getInstance().onSendChatMessage(message);
//    }


}
