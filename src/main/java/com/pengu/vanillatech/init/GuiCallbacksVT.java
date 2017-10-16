package com.pengu.vanillatech.init;

import java.lang.reflect.Field;

import com.pengu.hammercore.core.gui.GuiManager;
import com.pengu.hammercore.core.gui.iGuiCallback;
import com.pengu.hammercore.utils.NPEUtils;
import com.pengu.vanillatech.init.callbacks.GuiCallbackBackpack;
import com.pengu.vanillatech.init.callbacks.GuiCallbackCustomCraftingTable;

public class GuiCallbacksVT
{
	{
		NPEUtils.noInstancesError();
	}
	
	public static final iGuiCallback //
	        BACKPACK = new GuiCallbackBackpack(), //
	        CUSTOM_CRAFTING_TABLE = new GuiCallbackCustomCraftingTable();
	
	public static void register()
	{
		for(Field f : GuiCallbacksVT.class.getDeclaredFields())
		{
			f.setAccessible(true);
			if(iGuiCallback.class.isAssignableFrom(f.getType()))
				try
				{
					GuiManager.registerGuiCallback((iGuiCallback) f.get(null));
				} catch(Throwable err)
				{
				}
		}
	}
}