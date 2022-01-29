/*
 * Copyright (c) 2022. All copyright reserved
 */

package io.github.kingstefan26.stefans_util.mixins;

import net.minecraft.client.renderer.RenderGlobal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = RenderGlobal.class)
public class MixinrenderGlobal2 {

    @Inject(method = "renderEntityOutlineFramebuffer()V", at = @At("HEAD"))
    public void renderoutline(CallbackInfo ci) {
//        EntityOutlineRenderer.renderEntityOutlines((ICamera) Minecraft.getMinecraft().entityRenderer, 0, 0, 0, 0);
    }

}
