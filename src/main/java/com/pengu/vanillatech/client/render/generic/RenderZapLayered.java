package com.pengu.vanillatech.client.render.generic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ITickable;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;

public class RenderZapLayered
{
	private static final List<ITickable> layers = new ArrayList<>();
	private static final Random renderRand = new Random();
	
	/**
	 * Renders existing layer based on seed passed
	 */
	public static void renderRandomLayerBasedOnSeed(long seed, List<ZapLayer> layers)
	{
		renderRand.setSeed(seed);
		int c = layers.size();
		if(c < 1)
			return;
		layers.get(renderRand.nextInt(c)).render();
	}
	
	public static void addTickableLayer(ZapLayer layer)
	{
		layers.add(layer);
	}
	
	public static void addTickableLayers(ZapLayer... layers)
	{
		for(ZapLayer l : layers)
			addTickableLayer(l);
	}
	
	public static void addTickableLayers(Collection<ZapLayer> layers)
	{
		RenderZapLayered.layers.addAll(layers);
	}
	
	@SubscribeEvent
	public void clientTick(ClientTickEvent evt)
	{
		if(evt.phase != Phase.START)
			return;
		World world = Minecraft.getMinecraft().world;
		if(world != null)
			world.profiler.startSection("Tick Zap Effects");
		for(int i = 0; i < layers.size(); ++i)
		{
			if(world != null)
				world.profiler.startSection(i + " / " + layers.size());
			layers.get(i).update();
			if(world != null)
				world.profiler.endSection();
		}
		
		if(world != null)
			world.profiler.startSection("Controllable #1");
		ZapLayerControllable.ENHANCED_PICKACE__REPAIR.update();
		if(world != null)
			world.profiler.endSection();
		
		if(world != null)
			world.profiler.endSection();
	}
}