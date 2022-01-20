/*
 * Copyright (c) 2022. All copyright reserved
 */

package io.github.kingstefan26.stefans_util.mixins;

import io.github.kingstefan26.stefans_util.module.macro.wart.UniversalWartMacro;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = Minecraft.class)
public class MixinMinecraft {

    @Inject(method = "setIngameNotInFocus()V", at = @At("HEAD"), cancellable = true)
    public void setIngameNotInFocus(CallbackInfo ci) {
        if (UniversalWartMacro.shouldBlockGui) {
            ci.cancel();
        }
    }

}
