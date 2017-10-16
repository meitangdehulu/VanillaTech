package com.pengu.vanillatech.cfg;

import com.pengu.hammercore.cfg.HCModConfigurations;
import com.pengu.hammercore.cfg.iConfigReloadListener;
import com.pengu.hammercore.cfg.fields.ModConfigPropertyBool;
import com.pengu.vanillatech.Info;

import net.minecraftforge.common.config.Configuration;

@HCModConfigurations(modid = Info.MOD_ID)
public class ConfigsVT implements iConfigReloadListener
{
	@ModConfigPropertyBool(category = "Client", defaultValue = true, name = "3D Redstone", comment = "Should VanillaTech render redstone objects in 3D?")
	public static boolean client_3D_redstone = true;
	
	@ModConfigPropertyBool(category = "Blocks", defaultValue = true, name = "Time Bomb", comment = "Should VanillaTech add Time Bomb?")
	public static boolean blocks_TimeBomb = true;
	
	public static Configuration cfgs;
	
	@Override
	public void reloadCustom(Configuration cfgs)
	{
		this.cfgs = cfgs;
	}
}