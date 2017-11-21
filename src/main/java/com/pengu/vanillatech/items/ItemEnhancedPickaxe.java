package com.pengu.vanillatech.items;

import java.util.List;

import com.pengu.vanillatech.init.ItemMaterialsVT;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemEnhancedPickaxe extends ItemPickaxe
{
	public ItemEnhancedPickaxe()
	{
		super(ItemMaterialsVT.TOOL_ENHANCED);
		setUnlocalizedName("enhanced_pickaxe");
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn)
	{
		tooltip.add("Rank:");
		tooltip.add("<BAR>nbt:rank 5</BAR>");
	}
	
	@Override
	public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged)
	{
		return slotChanged;
	}
}