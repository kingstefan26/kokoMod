/*
 * Copyright (c) 2022. All copyright reserved
 */

package io.github.kingstefan26.stefans_util.mixins;

import io.github.kingstefan26.stefans_util.module.wip.ugh.EntityOutlineRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = RenderGlobal.class)
public class MixinRenderGlobal {

    @Final
    @Shadow
    private Minecraft mc;

    @Inject(method = "isRenderEntityOutlines()Z", at = @At("RETURN"), cancellable = true)
    void a(CallbackInfoReturnable<Boolean> cir) {
        ICamera icamera = new Frustum();
        Entity entity = this.mc.getRenderViewEntity();
        float partialTicks = mc.timer.renderPartialTicks;
        double d0 = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double) partialTicks;
        double d1 = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double) partialTicks;
        double d2 = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double) partialTicks;
        icamera.setPosition(d0, d1, d2);
        cir.setReturnValue(EntityOutlineRenderer.renderEntityOutlines(icamera, partialTicks, d0, d1, d2));
    }
}