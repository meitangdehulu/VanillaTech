package com.pengu.vanillatech.client.render.item;

import org.lwjgl.opengl.GL11;

import com.pengu.hammercore.client.render.item.iItemRender;
import com.pengu.hammercore.client.render.vertex.SimpleBlockRendering;
import com.pengu.hammercore.client.utils.RenderBlocks;
import com.pengu.vanillatech.InfoVT;
import com.pengu.vanillatech.blocks.BlockNetherstarOre;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderNetherstarOre implements iItemRender
{
	@Override
	public void renderItem(ItemStack item)
	{
		double expansion = .01 / 16;
		
		ItemStack stone = new ItemStack(Blocks.STONE);
		if(item.hasTagCompound())
			stone.setTagCompound(item.getTagCompound());
		
		SimpleBlockRendering sbr = RenderBlocks.forMod(InfoVT.MOD_ID).simpleRenderer;
		sbr.begin();
		sbr.setBrightness(BlockNetherstarOre.BRIGHTNESS.getAsInt());
		sbr.setSprite(Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(InfoVT.MOD_ID + ":blocks/netherstar_ore"));
		sbr.setRenderBounds(-expansion, -expansion, -expansion, 1 + expansion, 1 + expansion, 1 + expansion);
		sbr.drawBlock(0, 0, 0);
		sbr.end();
	}
}