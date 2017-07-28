package com.pengu.vanillatech.init;

import com.pengu.hammercore.init.SimpleRegistration;
import com.pengu.hammercore.utils.SoundObject;
import com.pengu.vanillatech.Info;

public class SoundsVT
{
	public static final SoundObject //
	TIME_BOMB_EXPLODE = new SoundObject(Info.MOD_ID, "time_bomb_explode");
	
	public static void clinit()
	{
	}
	
	static
	{
		SimpleRegistration.registerSound(TIME_BOMB_EXPLODE);
	}
}