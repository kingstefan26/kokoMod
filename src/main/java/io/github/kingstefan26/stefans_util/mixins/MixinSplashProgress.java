/*
 * Copyright (c) 2022. All copyright reserved
 */

package io.github.kingstefan26.stefans_util.mixins;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.SplashProgress;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(
        value = {SplashProgress.class},
        priority = 2000
)
public class MixinSplashProgress {
    @Unique
    private static final ResourceLocation troll = new ResourceLocation("stefan_util:splash/troll.png");
    @Unique
    private static final ResourceLocation arabic = new ResourceLocation("stefan_util:splash/arabic.png");

    @ModifyVariable(
            method = {"start"},
            at = @At("STORE"),
            ordinal = 1,
            remap = false
    )
    private static ResourceLocation setForgeTitle(ResourceLocation resourceLocation) {
        return troll;
    }

    @ModifyVariable(
            method = {"start"},
            at = @At("STORE"),
            ordinal = 0,
            remap = false
    )
    private static ResourceLocation setForgeUnicode(ResourceLocation resourceLocation) {
        return arabic;
    }
}
