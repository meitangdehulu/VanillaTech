package com.pengu.vanillatech.client.render.item;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.opengl.GL11;

import com.pengu.hammercore.client.render.item.IItemRender;
import com.pengu.hammercore.client.render.vertex.SimpleBlockRendering;
import com.pengu.hammercore.client.utils.RenderBlocks;
import com.pengu.vanillatech.Info;
import com.pengu.vanillatech.blocks.BlockNetherstarOre;

@SideOnly(Side.CLIENT)
public class RenderNetherstarOre implements IItemRender
{
	@Override
	public void renderItem(ItemStack item)
	{
		double expansion = .01 / 16;
		
		ItemStack stone = new ItemStack(Blocks.STONE);
		if(item.hasTagCompound())
			stone.setTagCompound(item.getTagCompound());
		
		GL11.glPushMatrix();
		GL11.glTranslated(.5, .5, .5);
		GL11.glScaled(2, 2, 2);
		Minecraft.getMinecraft().getRenderItem().renderItem(stone, TransformType.FIXED);
		GL11.glPopMatrix();
		
		SimpleBlockRendering sbr = RenderBlocks.forMod(Info.MOD_ID).simpleRenderer;
		sbr.begin();
		sbr.setBrightness(BlockNetherstarOre.BRIGHTNESS.getAsInt());
		sbr.setSprite(Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(Info.MOD_ID + ":blocks/netherstar_ore"));
		sbr.setRenderBounds(-expansion, -expansion, -expansion, 1 + expansion, 1 + expansion, 1 + expansion);
		sbr.drawBlock(0, 0, 0);
		sbr.end();
	}
}