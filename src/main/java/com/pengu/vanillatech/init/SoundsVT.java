package com.pengu.vanillatech.init;

import com.pengu.hammercore.common.SimpleRegistration;
import com.pengu.hammercore.utils.SoundObject;
import com.pengu.vanillatech.InfoVT;

public class SoundsVT
{
	public static final SoundObject //
	TIME_BOMB_EXPLODE = new SoundObject(InfoVT.MOD_ID, "time_bomb_explode");
	
	public static void clinit()
	{
	}
	
	static
	{
		SimpleRegistration.registerSound(TIME_BOMB_EXPLODE);
	}
}