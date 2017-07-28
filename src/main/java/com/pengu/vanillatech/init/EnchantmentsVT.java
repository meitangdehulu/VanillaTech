package com.pengu.vanillatech.init;

import java.lang.reflect.Field;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;

import com.pengu.vanillatech.Info;
import com.pengu.vanillatech.enchantments.EnchantmentAutosmelt;
import com.pengu.vanillatech.enchantments.EnchantmentInfernalPrediction;
import com.pengu.vanillatech.enchantments.EnchantmentRepair;

public class EnchantmentsVT
{
	private static NonNullList<Enchantment> ENCHANTMENTS;
	public static final Enchantment //
	        AUTO_SMELT = new EnchantmentAutosmelt(), //
	        INFERNAL_PREDICTOR = new EnchantmentInfernalPrediction(), //
	        REPAIR = new EnchantmentRepair();
	
	public static void init()
	{
		if(ENCHANTMENTS != null)
			return;
		
		MinecraftForge.EVENT_BUS.register(AUTO_SMELT);
		MinecraftForge.EVENT_BUS.register(REPAIR);
		
		NonNullList<Enchantment> ench = NonNullList.<Enchantment> create();
		IForgeRegistry<Enchantment> reg = GameRegistry.findRegistry(Enchantment.class);
		for(Field f : EnchantmentsVT.class.getDeclaredFields())
			if(Enchantment.class.isAssignableFrom(f.getType()))
			{
				f.setAccessible(true);
				try
				{
					Enchantment e = (Enchantment) f.get(null);
					e.setRegistryName(Info.MOD_ID, e.getName().substring(12));
					e.setName(e.getRegistryName().toString());
					reg.register(e);
					ench.add(e);
				} catch(Throwable err)
				{
				}
			}
		ENCHANTMENTS = NonNullList.<Enchantment> withSize(ench.size(), AUTO_SMELT);
		for(int i = 0; i < ench.size(); ++i)
			ENCHANTMENTS.set(i, ench.get(i));
	}
	
	public static NonNullList<Enchantment> getEnchantments()
	{
		return ENCHANTMENTS;
	}
}