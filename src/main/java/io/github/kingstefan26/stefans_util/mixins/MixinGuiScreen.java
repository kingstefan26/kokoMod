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


//    public void drawBackground(int tint) {
//        GlStateManager.disableLighting();
//        GlStateManager.disableFog();
//        Tessellator tessellator = Tessellator.getInstance();
//        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
//        this.mc.getTextureManager().bindTexture(optionsBackground);
//        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
//        float f = 32.0F;
//        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
//        worldrenderer.pos(0.0D, (double) this.height, 0.0D).tex(0.0D, (double) ((float) this.height / 32.0F + (float) tint)).color(64, 64, 64, 255).endVertex();
//        worldrenderer.pos((double) this.width, (double) this.height, 0.0D).tex((double) ((float) this.width / 32.0F), (double) ((float) this.height / 32.0F + (float) tint)).color(64, 64, 64, 255).endVertex();
//        worldrenderer.pos((double) this.width, 0.0D, 0.0D).tex((double) ((float) this.width / 32.0F), (double) tint).color(64, 64, 64, 255).endVertex();
//        worldrenderer.pos(0.0D, 0.0D, 0.0D).tex(0.0D, (double) tint).color(64, 64, 64, 255).endVertex();
//        tessellator.draw();
//    }

//    protected void drawGradientRect(int left, int top, int right, int bottom, int startColor, int endColor) {
//        float f = (float) (startColor >> 24 & 255) / 255.0F;
//        float f1 = (float) (startColor >> 16 & 255) / 255.0F;
//        float f2 = (float) (startColor >> 8 & 255) / 255.0F;
//        float f3 = (float) (startColor & 255) / 255.0F;
//        float f4 = (float) (endColor >> 24 & 255) / 255.0F;
//        float f5 = (float) (endColor >> 16 & 255) / 255.0F;
//        float f6 = (float) (endColor >> 8 & 255) / 255.0F;
//        float f7 = (float) (endColor & 255) / 255.0F;
//        GlStateManager.disableTexture2D();
//        GlStateManager.enableBlend();
//        GlStateManager.disableAlpha();
//        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
//        GlStateManager.shadeModel(7425);
//        Tessellator tessellator = Tessellator.getInstance();
//        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
//        worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
//        worldrenderer.pos((double) right, (double) top, 0).color(f1, f2, f3, f).endVertex();
//        worldrenderer.pos((double) left, (double) top, 0).color(f1, f2, f3, f).endVertex();
//        worldrenderer.pos((double) left, (double) bottom, 0).color(f5, f6, f7, f4).endVertex();
//        worldrenderer.pos((double) right, (double) bottom, 0).color(f5, f6, f7, f4).endVertex();
//        tessellator.draw();
//        GlStateManager.shadeModel(7424);
//        GlStateManager.disableBlend();
//        GlStateManager.enableAlpha();
//        GlStateManager.enableTexture2D();
//    }


//    @Inject(method = "drawWorldBackground(I)V", at = @At("HEAD"), cancellable = true)
////    @Redirect(method="drawWorldBackground(I)V", at=@At(value="INVOKE"))
//    public void drawWorldBackground(int tint, CallbackInfo ci) {
//        if (this.mc.theWorld != null) {
//            drawGradientRect(0, 0, this.width, this.height, blurClickGui.getBackgroundColor(false), blurClickGui.getBackgroundColor(true));
//        } else {
//            drawBackground(tint);
//        }
//        ci.cancel();
//    }

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
