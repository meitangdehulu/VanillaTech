package com.pengu.vanillatech.client.render.tesr;

import java.util.Random;

import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Tuple;

import org.lwjgl.opengl.GL11;

import com.pengu.hammercore.client.render.tesr.TESR;
import com.pengu.vanillatech.tile.TileFisher;

public class TESRFisher extends TESR<TileFisher>
{
	private final Random rand = new Random();
	
	@Override
	public void renderTileEntityAt(TileFisher te, double x, double y, double z, float partialTicks, ResourceLocation destroyStage, float alpha)
	{
		rand.setSeed(te.hashCode());
		
		GL11.glPushMatrix();
		GL11.glTranslated(x + .1, y, z + .1);
		for(int i = 0; i < te.pickedUp.size(); ++i)
		{
			Tuple<Integer, ItemStack> drop = te.pickedUp.get(i);
			float lev = drop.getFirst() / 20F;
			
			rand.setSeed(rand.nextLong() - drop.hashCode());
			
			GL11.glPushMatrix();
			GL11.glTranslated(rand.nextDouble() * .8, lev, rand.nextDouble() * .8);
			mc.getRenderItem().renderItem(drop.getSecond(), TransformType.GROUND);
			GL11.glPopMatrix();
		}
		GL11.glPopMatrix();
	}
}