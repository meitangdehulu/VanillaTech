package com.pengu.vanillatech;

import com.pengu.hammercore.utils.NPEUtils;

public class InfoVT
{
	private InfoVT()
	{
		NPEUtils.noInstancesError();
	}
	
	public static final String //
	        MOD_ID = "vanillatech", //
	        MOD_NAME = "Vanilla Tech", //
	        MOD_VERSION = "@VERSION@", //
	        PROXY_BASE = "com.pengu.vanillatech.proxy.";
}