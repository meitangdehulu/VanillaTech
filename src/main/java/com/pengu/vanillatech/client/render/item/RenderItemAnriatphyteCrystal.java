package com.pengu.vanillatech.client.render.item;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.item.ItemStack;

import org.lwjgl.opengl.GL11;

import com.pengu.hammercore.client.render.item.IItemRender;
import com.pengu.vanillatech.client.render.generic.RenderZapLayered;
import com.pengu.vanillatech.client.render.generic.ZapLayer;
import com.pengu.vanillatech.items.ItemVTHidden.EnumItemVTHidden;

public class RenderItemAnriatphyteCrystal implements IItemRender
{
	public final List<ZapLayer> zaps = new ArrayList<>();
	
	{
		for(int i = 0; i < 10; ++i)
		{
			ZapLayer l;
			zaps.add(l = new ZapLayer(0x77FF77, 30));
			l.maxZapCount = 5;
		}
		RenderZapLayered.addTickableLayers(zaps);
	}
	
	@Override
	public void renderItem(ItemStack item)
	{
		GL11.glPushMatrix();
		GL11.glTranslated(.5, .5, .5);
		GL11.glScaled(1.6, 1.6, 1.6);
		GL11.glRotatef(-45 - 180, 0, 1, 0);
		GL11.glRotated(30, 1, 0, 0);
		Minecraft.getMinecraft().getRenderItem().renderItem(EnumItemVTHidden.CRYSTAL_ANRIATPHYTE.stack(), TransformType.FIXED);
		GL11.glPopMatrix();
		
		RenderZapLayered.renderRandomLayerBasedOnSeed(item.hashCode(), zaps);
		
		GL11.glEnable(2884);
		GlStateManager.color(1, 1, 1);
		GlStateManager.enableBlend();
		GlStateManager.enableAlpha();
	}
}