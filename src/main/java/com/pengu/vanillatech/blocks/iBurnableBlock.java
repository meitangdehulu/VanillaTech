package com.pengu.vanillatech.blocks;

import net.minecraft.item.ItemStack;

public interface iBurnableBlock
{
	int getBurnTime(ItemStack stack);
}