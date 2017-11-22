package com.pengu.vanillatech.client.render.item;

import java.util.Random;

import org.lwjgl.opengl.GL11;

import com.pengu.hammercore.client.render.item.iItemRender;
import com.pengu.hammercore.client.utils.RenderUtil;
import com.pengu.hammercore.color.InterpolationUtil;
import com.pengu.hammercore.common.utils.ColorUtil;
import com.pengu.hammercore.utils.ColorHelper;
import com.pengu.vanillatech.client.render.generic.ZapLayerControllable;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;

public class RenderEnhancedPickaxe implements iItemRender
{
	public static int targetSize = 0;
	public static int currSize = 0;
	
	{
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	@SubscribeEvent
	public void clientTick(ClientTickEvent e)
	{
		if(e.phase == Phase.START && !Minecraft.getMinecraft().isGamePaused())
		{
			if(currSize >= targetSize && currSize > 0)
				targetSize = 0;
			currSize += Math.min(1, Math.max(-1, targetSize - currSize));
		}
	}
	
	@Override
	public void renderItem(ItemStack item)
	{
		NBTTagCompound nbt = item.getTagCompound();
		if(nbt == null)
			item.setTagCompound(nbt = new NBTTagCompound());
		
		int heat = nbt.getInteger("Heat") + nbt.getInteger("Rank");
		
		if(heat > 0)
		{
			float cu = heat / 10F;
			
			if(heat > 40)
			{
				ZapLayerControllable.ENHANCED_PICKACE__REPAIR.render();
				Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
			}
			
			GL11.glPushMatrix();
			GL11.glTranslated(.75, .69, .5);
			GL11.glScaled(5 * cu, 5 * cu, 5 * cu);
			
			int count = heat > 20 ? 25 + heat / 2 : 25;
			int countf = heat > 20 ? 15 + heat / 2 : 15;
			
			if(heat > 20 && ZapLayerControllable.ENHANCED_PICKACE__REPAIR.zaps.size() < 32)
				ZapLayerControllable.ENHANCED_PICKACE__REPAIR.summon(.5F);
			
			RenderUtil.renderColorfulLightRayEffects(0, 0, 0, func ->
			{
				int prim = 0x55666666;
				
				if(heat > 20)
				{
					float h = (heat - 20) / 80F;
					Random rand = ColRandomizer.rand;
					rand.setSeed(func);
					int rb = (rand.nextBoolean() ? 127 : 0) + rand.nextInt(128);
					int gb = (rand.nextBoolean() ? 127 : 0) + rand.nextInt(128);
					int bb = (rand.nextBoolean() ? 127 : 0) + rand.nextInt(128);
					int f = InterpolationUtil.interpolate(0x666666, ColorHelper.packARGB(1F, rb / 255F, gb / 255F, bb / 255F), h);
					return 0x55 << 24 | ColorHelper.packRGB(ColorHelper.getRed(f), ColorHelper.getGreen(f), ColorHelper.getBlue(f));
				}
				
				return prim;
			}, 1, Minecraft.getMinecraft().player.ticksExisted / 300F, 1, 2F, count, countf);
			GL11.glPopMatrix();
			GL11.glColor4f(1, 1, 1, 1);
			GL11.glEnable(3042);
		}
		
		RenderHelper.enableStandardItemLighting();
	}
	
	private static class ColRandomizer
	{
		private static final Random rand = new Random();
	}
}