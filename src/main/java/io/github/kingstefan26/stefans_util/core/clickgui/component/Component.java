package io.github.kingstefan26.stefans_util.core.clickgui.component;

import io.github.kingstefan26.stefans_util.util.CustomFont;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;

import java.awt.*;

public class Component {
	public boolean closed;

	public CustomFont c = new CustomFont(Minecraft.getMinecraft(), new Font("JetBrains Mono", Font.BOLD, 20), 20);
	public CustomFont p1 = new CustomFont(Minecraft.getMinecraft(), new Font("JetBrains Mono", Font.PLAIN, 17), 17);


	public static void drawRect(int left, int top, int right, int bottom, int color)
	{
		GlStateManager.disableDepth();
		GlStateManager.disableAlpha();

		if (left < right)
		{
			int i = left;
			left = right;
			right = i;
		}

		if (top < bottom)
		{
			int j = top;
			top = bottom;
			bottom = j;
		}

		float f3 = (float)(color >> 24 & 255) / 255.0F;
		float f = (float)(color >> 16 & 255) / 255.0F;
		float f1 = (float)(color >> 8 & 255) / 255.0F;
		float f2 = (float)(color & 255) / 255.0F;
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldrenderer = tessellator.getWorldRenderer();
		GlStateManager.enableBlend();
		GlStateManager.disableTexture2D();
		GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		GlStateManager.color(f, f1, f2, f3);
		worldrenderer.begin(7, DefaultVertexFormats.POSITION);
		worldrenderer.pos((double)left, (double)bottom, 0.0D).endVertex();
		worldrenderer.pos((double)right, (double)bottom, 0.0D).endVertex();
		worldrenderer.pos((double)right, (double)top, 0.0D).endVertex();
		worldrenderer.pos((double)left, (double)top, 0.0D).endVertex();
		tessellator.draw();
		GlStateManager.enableTexture2D();
		GlStateManager.disableBlend();

		GlStateManager.enableAlpha();
		GlStateManager.enableDepth();
	}

	public void renderComponent() {
		
	}
	
	public void updateComponent(int mouseX, int mouseY) {
		
	}
	
	public void mouseClicked(int mouseX, int mouseY, int button) {
		
	}
	
	public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
	}
	
	public int getParentHeight() {
		return 0;
	}
	
	public void keyTyped(char typedChar, int key) {
		
	}
	
	public void setOff(int newOff) {
		
	}
	
	public int getHeight() {
		return 0;
	}
}
