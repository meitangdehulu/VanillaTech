package com.pengu.vanillatech.client.render.tesr;

import java.util.HashMap;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.pengu.hammercore.client.DestroyStageTexture;
import com.pengu.hammercore.client.render.tesr.TESR;
import com.pengu.hammercore.client.render.vertex.SimpleBlockRendering;
import com.pengu.hammercore.client.utils.RenderBlocks;
import com.pengu.vanillatech.InfoVT;
import com.pengu.vanillatech.init.BlocksVT;
import com.pengu.vanillatech.init.EnchantmentsVT;
import com.pengu.vanillatech.tile.TileEnhancedFurnace;

public class TESREnhancedFurnace extends TESR<TileEnhancedFurnace>
{
	private static final ItemStack //
	        furnaceOff = new ItemStack(BlocksVT.ENHANCED_FURNACE), //
	        furnaceOn = new ItemStack(BlocksVT.ENHANCED_FURNACE);
	
	static
	{
		furnaceOff.setTagCompound(new NBTTagCompound());
		furnaceOff.getTagCompound().setBoolean("Lit", false);
		furnaceOn.setTagCompound(new NBTTagCompound());
		furnaceOn.getTagCompound().setBoolean("Lit", true);
	}
	
	@Override
	public void renderTileEntityAt(TileEnhancedFurnace te, double x, double y, double z, float partialTicks, ResourceLocation destroyStage, float alpha)
	{
		if(te.face == null)
			return;
		
		SimpleBlockRendering sbr = RenderBlocks.forMod(InfoVT.MOD_ID).simpleRenderer;
		
		ItemStack furnace = te.isActive ? furnaceOn : furnaceOff;
		
		EnchantmentHelper.setEnchantments(te.enchantments, furnace);
		
		GL11.glPushMatrix();
		GL11.glTranslated(x + .5, y + .5, z + .5);
		int orientation = te.face.getOpposite().ordinal();
		if(orientation == 2)
			GL11.glRotatef(180, 0, 1, 0);
		else if(orientation == 4)
			GL11.glRotatef(-90, 0, 1, 0);
		else if(orientation == 5)
			GL11.glRotatef(90, 0, 1, 0);
		GL11.glScaled(2, 2, 2);
		Minecraft.getMinecraft().getRenderItem().renderItem(furnace, TransformType.FIXED);
		GL11.glPopMatrix();
		
		if(destroyProgress > 0)
		{
			double expansion = .01 / 16;
			
			sbr.begin();
			sbr.setSprite(DestroyStageTexture.getAsSprite(destroyProgress));
			sbr.setRenderBounds(-expansion, -expansion, -expansion, 1 + expansion, 1 + expansion, 1 + expansion);
			sbr.drawBlock(x, y, z);
			sbr.end();
		}
	}
}