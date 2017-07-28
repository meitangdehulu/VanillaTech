package com.pengu.vanillatech.client;

import java.util.ArrayList;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.profiler.Profiler;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.pengu.hammercore.client.particle.def.ParticleSlowZap;
import com.pengu.hammercore.proxy.ParticleProxy_Client;
import com.pengu.hammercore.utils.ColorHelper;
import com.pengu.vanillatech.blocks.BlockNetherstarOre;
import com.pengu.vanillatech.client.particle.ParticleFirefly;
import com.pengu.vanillatech.client.particle.ParticleGlowingBlockOverlay;

@SideOnly(Side.CLIENT)
public class ClientTicker
{
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