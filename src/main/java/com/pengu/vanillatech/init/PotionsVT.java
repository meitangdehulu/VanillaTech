package com.pengu.vanillatech.init;

import java.lang.reflect.Field;

import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;

import com.pengu.vanillatech.Info;

public class PotionsVT
{
	private static NonNullList<PotionType> POTIONS;
	public static final PotionType //
	        TYPE_LEVITATION_SHORT = new PotionType("levitation_short", new PotionEffect(MobEffects.LEVITATION, 100)).setRegistryName("levitation_short"), //
	        TYPE_LEVITATION_LONG = new PotionType("levitation_long", new PotionEffect(MobEffects.LEVITATION, 600)).setRegistryName("levitation_long");
	
	public static void init()
	{
		if(POTIONS != null)
			return;
		NonNullList<PotionType> pots = NonNullList.<PotionType> create();
		IForgeRegistry<PotionType> reg = GameRegistry.findRegistry(PotionType.class);
		for(Field f : PotionsVT.class.getDeclaredFields())
			if(PotionType.class.isAssignableFrom(f.getType()))
			{
				f.setAccessible(true);
				try
				{
					PotionType e = (PotionType) f.get(null);
					pots.add(e);
					reg.register(e);
				} catch(Throwable err)
				{
				}
			}
		POTIONS = NonNullList.<PotionType> withSize(pots.size(), TYPE_LEVITATION_LONG);
		for(int i = 0; i < pots.size(); ++i)
			POTIONS.set(i, pots.get(i));
	}
	
	public static NonNullList<PotionType> getPotionTypes()
	{
		return POTIONS;
	}
}