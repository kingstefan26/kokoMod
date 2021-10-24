package io.github.kingstefan26.stefans_util.mixins;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EnumPlayerModelParts;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({EntityPlayer.class})
public abstract class MixinEntityPlayer {

    @Inject(method="isWearing", at=@At("HEAD"), cancellable = true)
    public void isWearing(EnumPlayerModelParts part, CallbackInfoReturnable<Boolean> cir) {
        if(part == EnumPlayerModelParts.CAPE) {
            cir.setReturnValue(false);
        }
    }
}