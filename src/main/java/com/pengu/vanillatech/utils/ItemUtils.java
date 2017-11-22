package com.pengu.vanillatech.utils;

import java.util.Objects;

import net.minecraft.item.ItemStack;

public class ItemUtils
{
	public static boolean tagChanged(ItemStack a, ItemStack b, String tag)
	{
		if(a.hasTagCompound() != b.hasTagCompound() || !a.hasTagCompound())
			return false;
		return Objects.equals(a.getTagCompound().getTag(tag), b.getTagCompound().getTag(tag));
	}
}