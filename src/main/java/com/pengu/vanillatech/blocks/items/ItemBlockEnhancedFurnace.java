package com.pengu.vanillatech.blocks.items;

import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Enchantments;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class ItemBlockEnhancedFurnace extends ItemBlock
{
	public ItemBlockEnhancedFurnace(Block block)
	{
		super(block);
		addPropertyOverride(new ResourceLocation("lit"), new IItemPropertyGetter()
		{
			@Override
			public float apply(ItemStack stack, World worldIn, EntityLivingBase entityIn)
			{
				return stack.hasTagCompound() && stack.getTagCompound().getBoolean("Lit") ? 1 : 0;
			}
		});
	}
	
	@Override
	public boolean isEnchantable(ItemStack stack)
	{
		return true;
	}
	
	@Override
	public int getItemEnchantability()
	{
		return 15;
	}
	
	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment)
	{
		return enchantment == Enchantments.EFFICIENCY || enchantment == Enchantments.FORTUNE || enchantment == Enchantments.UNBREAKING;
	}
}