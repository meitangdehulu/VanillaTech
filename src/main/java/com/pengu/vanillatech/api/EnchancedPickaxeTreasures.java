package com.pengu.vanillatech.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.pengu.vanillatech.init.ItemsVT;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.WeightedRandom;

public class EnchancedPickaxeTreasures
{
	private static final List<Drop> listAllDrops = new ArrayList<>();
	
	static
	{
		//Register vanilla entries
		
		addTreasure(5, new ItemStack(Items.COAL, 4), 400);
		addTreasure(5, new ItemStack(Items.IRON_NUGGET), 250);
		addTreasure(5, new ItemStack(Items.GOLD_NUGGET), 175);
		addTreasure(6, new ItemStack(Items.DIAMOND), 50);
		addTreasure(6, new ItemStack(Items.EMERALD), 25);
		addTreasure(7, new ItemStack(ItemsVT.NETHERSTAR_SHARD), 1);
	}
	
	/**
	 * @param weight
	 *            in 10000 to generate this treasure.
	 */
	public static void addTreasure(int minRank, ItemStack stack, int weight)
	{
		listAllDrops.add(new Drop(weight, minRank, stack));
	}
	
	public static Drop choose(Random rand, int rank)
	{
		List<Drop> supported = new ArrayList<>();
		listAllDrops.stream().filter(drop -> drop.mr <= rank).forEach(d -> supported.add(d));
		return WeightedRandom.getRandomItem(rand, supported);
	}
	
	public static class Drop extends WeightedRandom.Item
	{
		private int mr;
		private ItemStack stack;
		
		public Drop(int itemWeightIn, int mr, ItemStack stack)
		{
			super(itemWeightIn);
			this.mr = mr;
			this.stack = stack;
		}
		
		public int getMinRank()
		{
			return mr;
		}
		
		public ItemStack getStack()
		{
			return stack.copy();
		}
	}
}