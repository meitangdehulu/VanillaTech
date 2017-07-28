package com.pengu.vanillatech.client.render.item;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

import com.pengu.hammercore.client.render.item.IItemRender;
import com.pengu.hammercore.client.render.vertex.SimpleBlockRendering;
import com.pengu.hammercore.client.utils.RenderBlocks;
import com.pengu.vanillatech.Info;
import com.pengu.vanillatech.client.render.generic.RenderZapLayered;
import com.pengu.vanillatech.client.render.generic.ZapLayer;

public class RenderBlockUnstableMetal implements IItemRender
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
		SimpleBlockRendering s = RenderBlocks.forMod(Info.MOD_ID).simpleRenderer;
		s.begin();
		s.setBrightness(s.rb.setLighting(Minecraft.getMinecraft().world, Minecraft.getMinecraft().player.getPosition()));
		s.setSprite(Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(Info.MOD_ID + ":blocks/unstable_metal_block"));
		s.drawBlock(0, 0, 0);
		s.end();
		RenderZapLayered.renderRandomLayerBasedOnSeed(item.hashCode(), zaps);
	}
}