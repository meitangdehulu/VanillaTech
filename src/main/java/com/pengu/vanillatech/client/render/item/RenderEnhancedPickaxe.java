package com.pengu.vanillatech.client.render.item;

import org.lwjgl.opengl.GL11;

import com.pengu.hammercore.client.render.item.iItemRender;
import com.pengu.hammercore.client.utils.RenderUtil;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
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
		if(currSize > 0)
		{
			float cu = currSize / 10F;
			GL11.glPushMatrix();
			GL11.glTranslated(.75, .69, .5);
			GL11.glScaled(8 * cu, 8 * cu, 8 * cu);
			RenderUtil.renderLightRayEffects(0, 0, 0, 0x55666666, item.getCount(), Minecraft.getMinecraft().player.ticksExisted / 300F, 1, 2F, 25, 15);
			GL11.glPopMatrix();
		}
	}
}