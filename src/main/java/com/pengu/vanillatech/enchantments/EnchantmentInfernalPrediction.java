package com.pengu.vanillatech.enchantments;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;

import com.pengu.vanillatech.Info;

public class EnchantmentInfernalPrediction extends Enchantment
{
	
	public EnchantmentInfernalPrediction()
	{
		super(Rarity.VERY_RARE, EnumEnchantmentType.DIGGER, new EntityEquipmentSlot[] { EntityEquipmentSlot.MAINHAND, EntityEquipmentSlot.OFFHAND });
		setName("infernal_prediction");
	}
	
	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack)
	{
		return super.canApplyAtEnchantingTable(stack) && canApply(stack);
	}
	
	@Override
	public boolean canApply(ItemStack stack)
	{
		return stack.getItem() instanceof ItemTool;
	}
	
	@Override
	public int getMaxLevel()
	{
		return 10;
	}
}