/*
 * Copyright (c) 2022. All copyright reserved
 */

package io.github.kingstefan26.stefans_util.mixins;


import io.github.kingstefan26.stefans_util.core.Globals;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.network.handshake.FMLHandshakeMessage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Map;

@Mixin(value = FMLHandshakeMessage.ModList.class, remap = false)
public class MixinModList {
    @Shadow
    private Map<String, String> modTags;

    @Inject(method = "<init>(Ljava/util/List;)V", at = @At("RETURN"))
    private void removeMod(List<ModContainer> modList, CallbackInfo ci) {

        try {
            if (Minecraft.getMinecraft().isSingleplayer()) {
                return;
            }
        } catch (Exception ignored) {
            return;
        }

        this.modTags.entrySet().removeIf(modx -> modx.getKey().equalsIgnoreCase(Globals.MODID) || modx.getKey().equalsIgnoreCase("djperspectivemod"));

    }
}
