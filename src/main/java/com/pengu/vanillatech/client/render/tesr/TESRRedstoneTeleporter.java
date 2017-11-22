package com.pengu.vanillatech.client.render.tesr;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;

import java.util.Random;
import java.util.UUID;

import org.lwjgl.opengl.GL11;

import com.pengu.hammercore.client.render.tesr.TESR;
import com.pengu.hammercore.client.render.vertex.SimpleBlockRendering;
import com.pengu.hammercore.client.utils.RenderBlocks;
import com.pengu.hammercore.client.utils.RenderUtil;
import com.pengu.vanillatech.InfoVT;
import com.pengu.vanillatech.blocks.BlockRedstoneTeleporter;
import com.pengu.vanillatech.tile.TileRedstoneTeleporter;

public class TESRRedstoneTeleporter extends TESR<TileRedstoneTeleporter>
{
	@Override
	public void renderItem(ItemStack item)
	{
		SimpleBlockRendering sbr = RenderBlocks.forMod(InfoVT.MOD_ID).simpleRenderer;
		
		int activeTicks = 0;
		int color = 0;
		
		if(item.hasTagCompound() && item.getTagCompound().hasKey("UUIDMost"))
		{
			UUID uuid = item.getTagCompound().getUniqueId("UUID");
			Integer i = TileRedstoneTeleporter.TICKS.get(uuid.toString());
			activeTicks =  i != null ? i.intValue() : 0;
			
			Random rand = new Random(uuid != null ? uuid.getMostSignificantBits() - uuid.getLeastSignificantBits() : 0L);
			color = uuid != null ? rand.nextInt(0xFFFFFF) : 0xFF7777;
		}
		
		sbr.begin();
		sbr.setRenderBounds(BlockRedstoneTeleporter.aabb);
		sbr.setBrightness(getBrightnessForRB(null, sbr.rb));
		sbr.setSprite(mc.getTextureMapBlocks().getAtlasSprite("minecraft:blocks/stone_slab_top"));
		sbr.setSpriteForSide(EnumFacing.UP, mc.getTextureMapBlocks().getAtlasSprite(InfoVT.MOD_ID + ":blocks/redstone_teleporter_o" + (activeTicks > 0 ? "n" : "ff")));
		sbr.drawBlock(0, 0, 0);
		sbr.setSprite(mc.getTextureMapBlocks().getAtlasSprite(InfoVT.MOD_ID + ":blocks/ender_torch_o" + (activeTicks > 0 ? "n" : "ff")));
		
		sbr.setRenderBounds(7 / 16D, 2 / 16D, 7 / 16D, 9 / 16D, 10 / 16D, 9 / 16D);
		sbr.disableFace(EnumFacing.UP);
		sbr.disableFace(EnumFacing.DOWN);
		sbr.drawBlock(0, 0, -4 / 16D);
		
		sbr.setRenderBounds(7 / 16D, 4 / 16D, 7 / 16D, 9 / 16D, 10 / 16D, 9 / 16D);
		sbr.disableFace(EnumFacing.UP);
		sbr.disableFace(EnumFacing.DOWN);
		sbr.drawBlock(0, -2 / 16D, 5 / 16D);
		
		if(activeTicks > 0)
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
		
		if(activeTicks > 0)
		{
			GL11.glPushMatrix();
			GL11.glTranslated(.5, .55, .25);
			GL11.glScaled(5 * (activeTicks / 10F), 5 * (activeTicks / 10F), 5 * (activeTicks / 10F));
			RenderUtil.renderLightRayEffects(0, 0, 0, 120 << 24 | color, 0L, Minecraft.getMinecraft().player.ticksExisted / 360F, 1, 2F, 25, 15);
			GL11.glPopMatrix();
		}
	}
	
	@Override
	public void renderTileEntityAt(TileRedstoneTeleporter te, double x, double y, double z, float partialTicks, ResourceLocation destroyStage, float alpha)
	{
		if(te.direction == null)
			return;
		
		GL11.glPushMatrix();
		
		SimpleBlockRendering sbr = RenderBlocks.forMod(InfoVT.MOD_ID).simpleRenderer;
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
		sbr.setSpriteForSide(EnumFacing.UP, mc.getTextureMapBlocks().getAtlasSprite(InfoVT.MOD_ID + ":blocks/redstone_teleporter_o" + (te.isActive ? "n" : "ff")));
		sbr.drawBlock(0, 0, 0);
		sbr.setSprite(mc.getTextureMapBlocks().getAtlasSprite(InfoVT.MOD_ID + ":blocks/ender_torch_o" + (te.isActive ? "n" : "ff")));
		
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
		
		if(te.activeTicks > 0)
		{
			float strength = 360;
			Vec3d i0 = new Vec3d(te.direction == EnumFacing.EAST ? .25 : te.direction == EnumFacing.WEST ? .75 : .5, .55, te.direction == EnumFacing.SOUTH ? .25 : te.direction == EnumFacing.NORTH ? .75 : .5);
			GL11.glPushMatrix();
			GL11.glTranslated(x + i0.x, y + i0.y, z + i0.z);
			GL11.glScaled(5 * (te.activeTicks / 10F), 5 * (te.activeTicks / 10F), 5 * (te.activeTicks / 10F));
			RenderUtil.renderLightRayEffects(0, 0, 0, 5 << 24 | te.getColor(), te.getPos().toLong(), te.ticksExisted / strength + partialTicks / strength, 1, 2F, 25, 15);
			GL11.glPopMatrix();
		}
	}
}