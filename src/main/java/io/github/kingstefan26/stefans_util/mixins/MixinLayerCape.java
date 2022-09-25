/*
 * Copyright (c) 2022. All copyright reserved
 */

package io.github.kingstefan26.stefans_util.mixins;

import io.github.kingstefan26.stefans_util.module.wip.CapeFix;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerCape;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = LayerCape.class)
public abstract class MixinLayerCape implements LayerRenderer<AbstractClientPlayer> {
    @Final
    @Shadow
    private RenderPlayer playerRenderer;


    /**
     * Most of this function is copy-paste (mojang plz don't send cease and desist cuz copyright)
     * cuz tbh I didn't feel like doing callback injectors so just override the method, right?
     *
     * @param playerIn     THE PLAYER WE ARE RENDERING
     * @param partialTicks For tick interpolation, so we update rendering more than tick rate
     * @param scale        IDK
     */
    @Override
    public void doRenderLayer(AbstractClientPlayer playerIn, float idk, float idk1, float partialTicks, float idk2, float idk3, float idk4, float scale) {
        CapeFix.rendercapeRedirect(playerIn, partialTicks, playerRenderer);
    }
}
