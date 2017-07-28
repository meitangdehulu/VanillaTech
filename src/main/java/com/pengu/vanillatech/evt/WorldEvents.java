package com.pengu.vanillatech.evt;

import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import com.pengu.hammercore.annotations.MCFBus;
import com.pengu.vanillatech.utils.WorldEventListenerVT;

@MCFBus
public class WorldEvents
{
	@SubscribeEvent
	public void worldLoad(WorldEvent.Load evt)
	{
		if(!evt.getWorld().isRemote)
			evt.getWorld().addEventListener(new WorldEventListenerVT(evt.getWorld()));
	}
}