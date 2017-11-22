package com.pengu.vanillatech;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.init.Items;
import net.minecraft.item.ItemEnchantedBook;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.NonNullList;

import com.pengu.hammercore.utils.NPEUtils;
import com.pengu.vanillatech.init.EnchantmentsVT;
import com.pengu.vanillatech.init.ItemsVT;
import com.pengu.vanillatech.init.PotionsVT;

public class CreativeTabVT extends CreativeTabs
{
	CreativeTabVT(boolean $_)
	{
		super(InfoVT.MOD_ID);
	}
	
	public CreativeTabVT()
	{
		super(InfoVT.MOD_ID);
		NPEUtils.noInstancesError();
	}
	
	@Override
	public ItemStack getTabIconItem()
	{
		return new ItemStack(ItemsVT.BACKPACK);
	}
	
	@Override
	public void displayAllRelevantItems(NonNullList<ItemStack> items)
	{
		super.displayAllRelevantItems(items);
		NonNullList<Enchantment> ench = EnchantmentsVT.getEnchantments();
		NonNullList<PotionType> ptypes = PotionsVT.getPotionTypes();
		PotionsVT.init();
		for(Enchantment e : ench)
			items.add(ItemEnchantedBook.getEnchantedItemStack(new EnchantmentData(e, e.getMaxLevel())));
		for(PotionType t : ptypes)
			items.add(PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), t));
		for(PotionType t : ptypes)
			items.add(PotionUtils.addPotionToItemStack(new ItemStack(Items.SPLASH_POTION), t));
		for(PotionType t : ptypes)
			items.add(PotionUtils.addPotionToItemStack(new ItemStack(Items.LINGERING_POTION), t));
	}
}