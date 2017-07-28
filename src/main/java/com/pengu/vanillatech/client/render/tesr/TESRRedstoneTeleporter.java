package com.pengu.vanillatech.client.render.tesr;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.pengu.hammercore.client.render.tesr.TESR;
import com.pengu.hammercore.client.render.vertex.SimpleBlockRendering;
import com.pengu.hammercore.client.utils.RenderBlocks;
import com.pengu.vanillatech.Info;
import com.pengu.vanillatech.blocks.BlockRedstoneTeleporter;
import com.pengu.vanillatech.tile.TileRedstoneTeleporter;

public class TESRRedstoneTeleporter extends TESR<TileRedstoneTeleporter>
{
	@Override
	public void renderItem(ItemStack item)
	{
		SimpleBlockRendering sbr = RenderBlocks.forMod(Info.MOD_ID).simpleRenderer;
		
		boolean isActive = false;
		
		if(item.hasTagCompound() && item.getTagCompound().hasKey("UUIDMost"))
		{
			Integer r = TileRedstoneTeleporter.ENABLED.get(item.getTagCompound().getUniqueId("UUID").toString());
			isActive = (r != null ? r : 0) > 0;
		}
		
		sbr.begin();
		sbr.setRenderBounds(BlockRedstoneTeleporter.aabb);
		sbr.setBrightness(getBrightnessForRB(null, sbr.rb));
		sbr.setSprite(mc.getTextureMapBlocks().getAtlasSprite("minecraft:blocks/stone_slab_top"));
		sbr.setSpriteForSide(EnumFacing.UP, mc.getTextureMapBlocks().getAtlasSprite(Info.MOD_ID + ":blocks/redstone_teleporter_o" + (isActive ? "n" : "ff")));
		sbr.drawBlock(0, 0, 0);
		sbr.setSprite(mc.getTextureMapBlocks().getAtlasSprite(Info.MOD_ID + ":blocks/ender_torch_o" + (isActive ? "n" : "ff")));
		
		sbr.setRenderBounds(7 / 16D, 2 / 16D, 7 / 16D, 9 / 16D, 10 / 16D, 9 / 16D);
		sbr.disableFace(EnumFacing.UP);
		sbr.disableFace(EnumFacing.DOWN);
		sbr.drawBlock(0, 0, -4 / 16D);
		
		sbr.setRenderBounds(7 / 16D, 4 / 16D, 7 / 16D, 9 / 16D, 10 / 16D, 9 / 16D);
		sbr.disableFace(EnumFacing.UP);
		sbr.disableFace(EnumFacing.DOWN);
		sbr.drawBlock(0, -2 / 16D, 5 / 16D);
		
		if(isActive)
			sbr.setBrightness(15 << 20 | 15 << 4);
		
		sbr.setRenderBounds(7 / 16D, 8 / 16D, 6 / 16D, 9 / 16D, 10 / 16D, 8 / 16D);
		sbr.disableFaces();
		sbr.enableFace(EnumFacing.UP);
		sbr.drawBlock(0, -2 / 16D, 6 / 16D);
		
		sbr.setRenderBounds(7 / 16D, 8 / 16D, 6 / 16D, 9 / 16D, 10 / 16D, 8 / 16D);
		sbr.disableFaces();
		sbr.enableFace(EnumFacing.UP);
		sbr.drawBlock(0, 0, -3 / 16D);
		
		sbr.end();
	}
	
	@Override
	public void renderTileEntityAt(TileRedstoneTeleporter te, double x, double y, double z, float partialTicks, ResourceLocation destroyStage, float alpha)
	{
		if(te.direction == null)
			return;
		
		GL11.glPushMatrix();
		
		SimpleBlockRendering sbr = RenderBlocks.forMod(Info.MOD_ID).simpleRenderer;
		GL11.glTranslated(x, y, z);
		int orientation = te.direction.ordinal();
		
		if(orientation == 2)
		{
			GL11.glTranslated(1, 0, 1);
			GL11.glRotatef(180, 0, 1, 0);
		} else if(orientation == 4)
		{
			GL11.glTranslated(1, 0, 0);
			GL11.glRotatef(-90, 0, 1, 0);
		} else if(orientation == 5)
		{
			GL11.glTranslated(0, 0, 1);
			GL11.glRotatef(90, 0, 1, 0);
		}
		
		sbr.begin();
		sbr.setRenderBounds(BlockRedstoneTeleporter.aabb);
		sbr.setBrightness(getBrightnessForRB(te, sbr.rb));
		sbr.setSprite(mc.getTextureMapBlocks().getAtlasSprite("minecraft:blocks/stone_slab_top"));
		sbr.setSpriteForSide(EnumFacing.UP, mc.getTextureMapBlocks().getAtlasSprite(Info.MOD_ID + ":blocks/redstone_teleporter_o" + (te.isActive ? "n" : "ff")));
		sbr.drawBlock(0, 0, 0);
		sbr.setSprite(mc.getTextureMapBlocks().getAtlasSprite(Info.MOD_ID + ":blocks/ender_torch_o" + (te.isActive ? "n" : "ff")));
		
		double off = te.isReceiving ? .5 / 16 : 5 / 16D;
		
		sbr.setRenderBounds(7 / 16D, 2 / 16D, 7 / 16D, 9 / 16D, 10 / 16D, 9 / 16D);
		sbr.disableFace(EnumFacing.UP);
		sbr.disableFace(EnumFacing.DOWN);
		sbr.drawBlock(0, 0, -4 / 16D);
		
		sbr.setRenderBounds(7 / 16D, 4 / 16D, 7 / 16D, 9 / 16D, 10 / 16D, 9 / 16D);
		sbr.disableFace(EnumFacing.UP);
		sbr.disableFace(EnumFacing.DOWN);
		sbr.drawBlock(0, -2 / 16D, off);
		
		if(te.isActive)
			sbr.setBrightness(15 << 20 | 15 << 4);
		
		sbr.setRenderBounds(7 / 16D, 8 / 16D, 6 / 16D, 9 / 16D, 10 / 16D, 8 / 16D);
		sbr.disableFaces();
		sbr.enableFace(EnumFacing.UP);
		sbr.drawBlock(0, -2 / 16D, off + 1 / 16D);
		
		sbr.setRenderBounds(7 / 16D, 8 / 16D, 6 / 16D, 9 / 16D, 10 / 16D, 8 / 16D);
		sbr.disableFaces();
		sbr.enableFace(EnumFacing.UP);
		sbr.drawBlock(0, 0, -3 / 16D);
		
		sbr.end();
		
		GL11.glPopMatrix();
	}
}