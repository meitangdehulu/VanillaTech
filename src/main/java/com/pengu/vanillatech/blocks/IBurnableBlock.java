package com.pengu.vanillatech.blocks;

import net.minecraft.item.ItemStack;

public interface IBurnableBlock
{
	int getBurnTime(ItemStack stack);
}