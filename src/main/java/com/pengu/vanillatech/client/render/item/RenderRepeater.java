package com.pengu.vanillatech.client.render.item;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.pengu.hammercore.client.render.item.IItemRender;
import com.pengu.hammercore.client.render.vertex.SimpleBlockRendering;
import com.pengu.hammercore.client.utils.RenderBlocks;
import com.pengu.vanillatech.Info;
import com.pengu.vanillatech.blocks.BlockRedstoneTeleporter;

@SideOnly(Side.CLIENT)
public class RenderRepeater implements IItemRender
{
	@Override
	public void renderItem(ItemStack item)
	{
		Minecraft mc = Minecraft.getMinecraft();
		SimpleBlockRendering sbr = RenderBlocks.forMod(Info.MOD_ID).simpleRenderer;
		
		sbr.begin();
		sbr.setRenderBounds(BlockRedstoneTeleporter.aabb);
		sbr.setBrightness(getBrightnessForRB(sbr.rb));
		sbr.setSprite(mc.getTextureMapBlocks().getAtlasSprite("minecraft:blocks/stone_slab_top"));
		sbr.setSpriteForSide(EnumFacing.UP, mc.getTextureMapBlocks().getAtlasSprite("minecraft:blocks/repeater_off"));
		sbr.drawBlock(0, 0, 0);
		sbr.setSprite(mc.getTextureMapBlocks().getAtlasSprite("minecraft:blocks/redstone_torch_off"));
		sbr.setRenderBounds(7 / 16D, 3 / 16D, 7 / 16D, 9 / 16D, 10 / 16D, 9 / 16D);
		sbr.disableFace(EnumFacing.UP);
		sbr.disableFace(EnumFacing.DOWN);
		sbr.drawBlock(0, -3 / 16D, -4 / 16D);
		sbr.setRenderBounds(7 / 16D, 4 / 16D, 7 / 16D, 9 / 16D, 10 / 16D, 9 / 16D);
		sbr.disableFace(EnumFacing.UP);
		sbr.disableFace(EnumFacing.DOWN);
		sbr.drawBlock(0, -3 / 16D, -1 / 16D);
		sbr.setRenderBounds(7 / 16D, 8 / 16D, 6 / 16D, 9 / 16D, 10 / 16D, 8 / 16D);
		sbr.disableFaces();
		sbr.enableFace(EnumFacing.UP);
		sbr.drawBlock(0, -3 / 16D, 0);
		sbr.setRenderBounds(7 / 16D, 8 / 16D, 6 / 16D, 9 / 16D, 10 / 16D, 8 / 16D);
		sbr.disableFaces();
		sbr.enableFace(EnumFacing.UP);
		sbr.drawBlock(0, -3 / 16D, -3 / 16D);
		sbr.end();
	}
	
	protected int getBrightnessForRB(RenderBlocks rb)
	{
		return rb.setLighting(Minecraft.getMinecraft().world, Minecraft.getMinecraft().player.getPosition());
	}
}