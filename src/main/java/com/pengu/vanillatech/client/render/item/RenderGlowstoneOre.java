package com.pengu.vanillatech.client.render.item;

import org.lwjgl.opengl.GL11;

import com.pengu.hammercore.client.render.item.iItemRender;
import com.pengu.hammercore.client.render.vertex.SimpleBlockRendering;
import com.pengu.hammercore.client.utils.RenderBlocks;
import com.pengu.vanillatech.Info;
import com.pengu.vanillatech.blocks.BlockGlowstoneOre;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderGlowstoneOre implements iItemRender
{
	@Override
	public void renderItem(ItemStack item)
	{
		double expansion = .01 / 16;
		
		SimpleBlockRendering sbr = RenderBlocks.forMod(Info.MOD_ID).simpleRenderer;
		sbr.begin();
		sbr.setBrightness(BlockGlowstoneOre.BRIGHTNESS.getAsInt());
		sbr.setSprite(Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(Info.MOD_ID + ":blocks/glowstone_ore_overlay"));
		sbr.setRenderBounds(-expansion, -expansion, -expansion, 1 + expansion, 1 + expansion, 1 + expansion);
		sbr.drawBlock(0, 0, 0);
		sbr.end();
	}
}