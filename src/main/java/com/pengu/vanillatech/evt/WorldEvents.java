package com.pengu.vanillatech.evt;

import com.pengu.hammercore.annotations.MCFBus;
import com.pengu.vanillatech.utils.WorldEventListenerVT;

import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@MCFBus
public class WorldEvents
{
	@SubscribeEvent
	public void worldLoad(WorldEvent.Load evt)
	{
		evt.getWorld().addEventListener(new WorldEventListenerVT(evt.getWorld()));
	}
}