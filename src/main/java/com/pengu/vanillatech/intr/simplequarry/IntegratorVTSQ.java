package com.pengu.vanillatech.intr.simplequarry;

import com.pengu.hammercore.common.SimpleRegistration;
import com.pengu.vanillatech.InfoVT;
import com.pengu.vanillatech.VanillaTech;

import net.minecraftforge.fml.common.Loader;

public class IntegratorVTSQ
{
	public static void preInit()
	{
		if(!Loader.isModLoaded("simplequarry"))
			return;
		
		SimpleRegistration.registerFieldItemsFrom(ItemsVTSQ.class, InfoVT.MOD_ID, VanillaTech.tab);
	}
}