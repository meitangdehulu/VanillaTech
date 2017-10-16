package com.pengu.vanillatech.client.render.item;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import com.pengu.hammercore.client.render.item.iItemRender;
import com.pengu.vanillatech.client.render.generic.RenderZapLayered;
import com.pengu.vanillatech.client.render.generic.ZapLayer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemStack;

public class RenderBlockUnstableMetal implements iItemRender
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
		GL11.glScaled(.85, .85, .85);
		RenderZapLayered.renderRandomLayerBasedOnSeed(item.hashCode(), zaps);
		GL11.glPopMatrix();
		
		Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
	}
}