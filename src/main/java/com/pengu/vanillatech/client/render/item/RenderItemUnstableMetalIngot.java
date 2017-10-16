package com.pengu.vanillatech.client.render.item;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import com.pengu.hammercore.client.render.item.iItemRender;
import com.pengu.hammercore.client.utils.RenderUtil;
import com.pengu.vanillatech.client.render.generic.RenderZapLayered;
import com.pengu.vanillatech.client.render.generic.ZapLayer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemStack;

public class RenderItemUnstableMetalIngot implements iItemRender
{
	public final List<ZapLayer> zaps = new ArrayList<>();
	
	{
		for(int i = 0; i < 10; ++i)
			zaps.add(new ZapLayer(0xFFFFFF, 10));
		RenderZapLayered.addTickableLayers(zaps);
	}
	
	@Override
	public void renderItem(ItemStack item)
	{
		GL11.glPushMatrix();
		GL11.glTranslated(.5, .5, .5);
		GL11.glScaled(.5, .5, .5);
		RenderZapLayered.renderRandomLayerBasedOnSeed(item.hashCode(), zaps);
		GL11.glPopMatrix();
		
		double scale = Math.sin(((System.currentTimeMillis() + item.hashCode()) % 9000L) / 100D) * 2;
		
		GL11.glPushMatrix();
		GL11.glTranslated(.5, .5, .5);
		GL11.glScaled(10 + scale, 10 + scale, 10 + scale);
		RenderUtil.renderLightRayEffects(0, 0, 0, 0x77FFFFFF, 0, ((System.currentTimeMillis() + item.hashCode()) % 20000L) / 20000F, 1, 2f, 30, 15);
		GL11.glPopMatrix();
		
		Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
	}
}