package com.pengu.vanillatech.client;

import java.util.ArrayList;
import java.util.List;

import com.pengu.hammercore.client.particle.def.ParticleSlowZap;
import com.pengu.hammercore.proxy.ParticleProxy_Client;
import com.pengu.hammercore.utils.ColorHelper;
import com.pengu.vanillatech.blocks.BlockNetherstarOre;
import com.pengu.vanillatech.client.particle.ParticleFirefly;
import com.pengu.vanillatech.client.particle.ParticleGlowingBlockOverlay;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.profiler.Profiler;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientTicker
{
	@SubscribeEvent
	public void drawGui(RenderTooltipEvent e)
	{
		List<String> lines = e.getLines();
		ItemStack stack = e.getStack();
		
		String s = "                ";
		
		for(int i = 0; i < lines.size(); ++i)
		{
			String ln = lines.get(i);
			
			if(ln.startsWith("<BAR>") && ln.endsWith("</BAR>"))
			{
				String[] data = ln.substring(5, ln.length() - 6).split(" ");
				float cur = parseFloat(stack, data[0]);
				float max = parseFloat(stack, data[1]);
				
				lines.set(i, s);
			}
		}
		
		int x = e.getX();
		int y = e.getY();
	}
	
	public static float parseFloat(ItemStack stack, String token)
	{
		try
		{
			return Float.parseFloat(token);
		} catch(Throwable err)
		{
			String[] data = token.split(":");
			
			if(data[0].equalsIgnoreCase("nbt") && stack.hasTagCompound())
				return stack.getTagCompound().getFloat(data[1]);
			else if(data[0].equalsIgnoreCase("stack"))
			{
				String sub = data[1];
				
				if(sub.equalsIgnoreCase("count") || sub.equalsIgnoreCase("amount"))
					return stack.getCount();
				
				if(sub.equalsIgnoreCase("damage") || sub.equalsIgnoreCase("metadata") || sub.equalsIgnoreCase("meta"))
					return stack.getItemDamage();
			}
		}
		
		return 0F;
	}
	
	@SubscribeEvent
	public void tickWorld(ClientTickEvent evt)
	{
		if(evt.phase != Phase.START)
			return;
		WorldClient w = Minecraft.getMinecraft().world;
		EntityPlayer p = Minecraft.getMinecraft().player;
		if(w == null || p == null)
		{
			if(!ParticleGlowingBlockOverlay.particles.isEmpty())
			{
				ArrayList<ParticleGlowingBlockOverlay> overlays = new ArrayList<>(ParticleGlowingBlockOverlay.particles.values());
				for(ParticleGlowingBlockOverlay p2 : overlays)
					p2.setExpired();
			}
			ParticleGlowingBlockOverlay.particles.clear();
			return;
		}
		
		Profiler pr = w.profiler;
		
		long dayTime = w.getWorldTime() % 24000L;
		boolean noScreen = Minecraft.getMinecraft().currentScreen == null || !Minecraft.getMinecraft().currentScreen.doesGuiPauseGame();
		boolean evening = dayTime >= 13000 && dayTime < 18000 && noScreen;
		
		pr.startSection("Fireflies Spawning");
		if(!w.isRaining() && evening && w.rand.nextInt(35) == 0)
		{
			double rad = 24;
			double flyRad = 16;
			
			double x1 = p.posX - (w.rand.nextDouble() - w.rand.nextDouble()) * rad;
			double z1 = p.posZ - (w.rand.nextDouble() - w.rand.nextDouble()) * rad;
			
			double x2 = x1 - (w.rand.nextDouble() - w.rand.nextDouble()) * flyRad;
			double z2 = z1 - (w.rand.nextDouble() - w.rand.nextDouble()) * flyRad;
			
			BlockPos p1 = w.getHeight(new BlockPos(x1, 255, z1));
			BlockPos p2 = w.getHeight(new BlockPos(x2, 255, z2));
			
			double y1 = p1.getY() + 4 + w.rand.nextDouble() * 12 - w.rand.nextDouble() * 4;
			double y2 = p2.getY() + 4 + w.rand.nextDouble() * 12 - w.rand.nextDouble() * 8;
			
			if(p.getDistanceSq(x1, y1, z1) > 16D && p.getDistanceSq(x2, y2, z2) > 16D)
			{
				ParticleFirefly f = new ParticleFirefly(w, x1, y1, z1, x2, y2, z2, 80, 1.5F);
				f.setColor(ColorHelper.packRGB(.8F + w.rand.nextFloat() * .2F, .8F + w.rand.nextFloat() * .2F, 25 / 255F));
				ParticleProxy_Client.queueParticleSpawn(f);
			}
		}
		pr.endSection();
		
		if(noScreen && w.isThundering() && w.rand.nextInt(50) == 0)
		{
			double r = 128;
			double x1 = p.posX - (w.rand.nextDouble() - w.rand.nextDouble()) * r;
			double z1 = p.posZ - (w.rand.nextDouble() - w.rand.nextDouble()) * r;
			double x2 = p.posX + (w.rand.nextDouble() - w.rand.nextDouble()) * r;
			double z2 = p.posZ + (w.rand.nextDouble() - w.rand.nextDouble()) * r;
			
			ParticleProxy_Client.queueParticleSpawn(new ParticleSlowZap(w, 5, w.rand.nextFloat(), x1, 255, z1, x2, 255, z2, 0, .5F, 1));
		}
		
		pr.startSection("Tick NetherStarOre Tex");
		int bright = BlockNetherstarOre.bright;
		bright += w.rand.nextInt(2) - w.rand.nextInt(2);
		BlockNetherstarOre.bright = MathHelper.clamp(bright, 15, 30);
		pr.endSection();
	}
}