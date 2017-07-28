package com.pengu.vanillatech.items;

import net.minecraft.item.ItemPickaxe;

import com.pengu.vanillatech.init.ItemMaterialsVT;

public class ItemEnhancedPickaxe extends ItemPickaxe
{
	public ItemEnhancedPickaxe()
    {
		super(ItemMaterialsVT.TOOL_ENHANCED);
		setUnlocalizedName("enhanced_pickaxe");
    }
}