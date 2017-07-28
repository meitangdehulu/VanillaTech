package com.pengu.vanillatech.init;

import java.lang.reflect.Field;

import com.pengu.hammercore.gui.GuiManager;
import com.pengu.hammercore.gui.IGuiCallback;
import com.pengu.hammercore.utils.NPEUtils;
import com.pengu.vanillatech.init.callbacks.GuiCallbackBackpack;
import com.pengu.vanillatech.init.callbacks.GuiCallbackCustomCraftingTable;

public class GuiCallbacksVT
{
	{
		NPEUtils.noInstancesError();
	}
	
	public static final IGuiCallback //
	        BACKPACK = new GuiCallbackBackpack(), //
	        CUSTOM_CRAFTING_TABLE = new GuiCallbackCustomCraftingTable();
	
	public static void register()
	{
		for(Field f : GuiCallbacksVT.class.getDeclaredFields())
		{
			f.setAccessible(true);
			if(IGuiCallback.class.isAssignableFrom(f.getType()))
				try
				{
					GuiManager.registerGuiCallback((IGuiCallback) f.get(null));
				} catch(Throwable err)
				{
				}
		}
	}
}