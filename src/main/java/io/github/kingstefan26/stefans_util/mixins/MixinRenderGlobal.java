/*
 * Copyright (c) 2022. All copyright reserved
 */

package io.github.kingstefan26.stefans_util.mixins;

import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.client.shader.ShaderGroup;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = RenderGlobal.class)
public interface MixinRenderGlobal {

    @Accessor("entityOutlineFramebuffer")
    public Framebuffer getentityOutlineFramebuffer();

    @Accessor("entityOutlineShader")
    public ShaderGroup getentityOutlineShader();
}