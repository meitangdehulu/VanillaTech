package com.pengu.vanillatech.items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;

import com.pengu.vanillatech.init.ItemsVT;

public class ItemVTHidden extends Item
{
	public static enum EnumItemVTHidden
	{
		UNSTABLE_METAL_INGOT, CRYSTAL_ANRIATPHYTE;
		
		public ItemStack stack()
		{
			return stack(1);
		}
		
		public ItemStack stack(int count)
		{
			return new ItemStack(ItemsVT.HIDDEN, count, ordinal());
		}
	}
	
	public ItemVTHidden()
	{
		addPropertyOverride(new ResourceLocation("data"), (stack, world, ent) -> stack.getItemDamage());
		setMaxDamage(EnumItemVTHidden.values().length);
		setMaxStackSize(1);
		setUnlocalizedName("vt_hidden");
		setHasSubtypes(true);
	}
	
	@Override
	public boolean showDurabilityBar(ItemStack stack)
	{
		return false;
	}
	
	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items)
	{
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack)
	{
		return "vanilla_tech_hidden_item";
	}
}