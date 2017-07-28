package com.pengu.vanillatech.api;

import net.minecraft.item.ItemStack;
import net.minecraft.util.Tuple;

import com.pengu.hammercore.intent.IntentManager;
import com.pengu.hammercore.utils.IndexedMap;
import com.pengu.vanillatech.Info;
import com.pengu.vanillatech.init.VTLog;

public class EnhancerRegistry
{
	private static final IndexedMap<ItemStack, Integer> enhancerFuel = new IndexedMap<ItemStack, Integer>();
	
	static
	{
		IntentManager.registerIntentHandler(Info.MOD_ID + ":add_enhancer_fuel", Tuple.class, (name, tuple) ->
		{
			enhancerFuel.put((ItemStack) tuple.getFirst(), (Integer) tuple.getSecond());
			VTLog.info("Accepted call to register Enhancer Fuel (" + tuple.getFirst() + "=" + tuple.getSecond() + ") from mod " + name);
			return null;
		});
	}
	
	public static int getEnhancerFuelValue(ItemStack stack)
	{
		for(ItemStack i : enhancerFuel.getKeys())
			if(i.isItemEqual(stack))
				return enhancerFuel.get(i);
		return 0;
	}
}