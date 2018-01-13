package com.pengu.vanillatech.init;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import com.endie.simplequarry.init.ItemsSQ;
import com.pengu.hammercore.common.SimpleRegistration;
import com.pengu.vanillatech.InfoVT;
import com.pengu.vanillatech.intr.simplequarry.ItemsVTSQ;

import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.oredict.OreDictionary;

public class RecipesVT
{
	public static void furnace()
	{
		OreDictionary.getOreID("chestEnder");
	}
	
	private static void craftingTable()
	{
		shaped(BlocksVT.ANRIATPHYTE_BLOCK, "ccc", "ccc", "ccc", 'c', ItemsVT.ANRIATPHYTE_CRYSTAL);
		shaped(BlocksVT.COMPRESSED_CHARCOAL_BLOCK, "ccc", "ccc", "ccc", 'c', BlocksVT.CHARCOAL_BLOCK);
		shaped(BlocksVT.COMPRESSED_COAL_BLOCK, "ccc", "ccc", "ccc", 'c', Blocks.COAL_BLOCK);
		shaped(BlocksVT.UNSTABLE_METAL_BLOCK, "ccc", "ccc", "ccc", 'c', ItemsVT.UNSTABLE_METAL_INGOT);
		shaped(BlocksVT.CHARCOAL_BLOCK, "ccc", "ccc", "ccc", 'c', new ItemStack(Items.COAL, 1, 1));
		shapeless(new ItemStack(BlocksVT.CHARCOAL_BLOCK, 9), BlocksVT.COMPRESSED_CHARCOAL_BLOCK);
		shapeless(new ItemStack(Blocks.COAL_BLOCK, 9), BlocksVT.COMPRESSED_COAL_BLOCK);
		shapeless(new ItemStack(ItemsVT.ANRIATPHYTE_CRYSTAL, 9), BlocksVT.ANRIATPHYTE_BLOCK);
		shapeless(new ItemStack(Items.COAL, 9, 1), BlocksVT.CHARCOAL_BLOCK);
		shapeless(new ItemStack(ItemsVT.UNSTABLE_METAL_INGOT, 9), BlocksVT.UNSTABLE_METAL_BLOCK);

		shaped(ItemsVT.BACKPACK, "lll", "cic", "lll", 'l', "leather", 'c', "chestWood", 'i', "nuggetIron");
		shaped(ItemsVT.ENDER_BACKPACK, "lll", "cic", "lll", 'l', "obsidian", 'c', "chestEnder", 'i', "gemDiamond");
		shaped(BlocksVT.ENHANCED_FURNACE, " b ", "bfb", " b ", 'b', BlocksVT.UNSTABLE_METAL_BLOCK, 'f', Blocks.FURNACE);
		shaped(ItemsVT.ENHANCED_PICKAXE, "nnn", " d ", " s ", 'n', ItemsVT.NETHERSTAR_SHARD, 's', "stickWood", 'd', Items.DIAMOND_PICKAXE);
		shaped(Items.NETHER_STAR, "lll", "l l", "lll", 'l', ItemsVT.NETHERSTAR_SHARD);
		shapeless(Items.QUARTZ, Blocks.QUARTZ_BLOCK);
		shaped(new ItemStack(BlocksVT.REDSTONE_TELEPORTER, 2), "e", "r", 'e', "enderpearl", 'r', Items.REPEATER);
		shaped(new ItemStack(BlocksVT.REDSTONE_TELEPORTER, 2), " e ", "trt", "sss", 'e', "enderpearl", 'r', "dustRedstone", 't', Blocks.REDSTONE_TORCH, 's', "stone");
		shaped(ItemsVT.SHIELD_TOTEM, "t", "s", "t", 't', Items.TOTEM_OF_UNDYING, 's', "netherStar");
		shaped(new ItemStack(BlocksVT.WITHER_PROOF_RED_STONE, 4), "oro", "rbr", "oro", 'r', BlocksVT.RED_STONE, 'b', Items.BLAZE_POWDER, 'o', "obsidian");
		shaped(BlocksVT.FISHER, "sws", "wsw", "sws", 's', "string", 'w', "stickWood");
		shaped(BlocksVT.TIME_BOMB, "tut", "usn", "tut", 't', Blocks.TNT, 'u', ItemsVT.UNSTABLE_METAL_INGOT, 's', ItemsVT.NETHERSTAR_SHARD);
		
		if(Loader.isModLoaded("simplequarry"))
			craftingTableSQ();
	}
	
	private static void craftingTableSQ()
	{
		shaped(ItemsVTSQ.NETHERSTARMINE_UPGRADE, " n ", "nun", " n ", 'n', ItemsVT.NETHERSTAR_SHARD, 'u', ItemsSQ.UPGRADE_BASE);
	}
	
	public static ItemStack enchantedBook(Enchantment ench, int lvl)
	{
		ItemStack book = new ItemStack(Items.ENCHANTED_BOOK);
		Map<Enchantment, Integer> enchs = new HashMap<>();
		enchs.put(ench, lvl);
		EnchantmentHelper.setEnchantments(enchs, book);
		return book;
	}
	
	private static final List<IRecipe> recipes = new ArrayList<>();
	
	public static Collection<IRecipe> collect()
	{
		craftingTable();
		HashSet<IRecipe> recipes = new HashSet<>(RecipesVT.recipes);
		RecipesVT.recipes.clear();
		return recipes;
	}
	
	private static void smelting(ItemStack in, ItemStack out)
	{
		smelting(in, out, 0);
	}
	
	private static void smelting(Item in, ItemStack out, float xp)
	{
		FurnaceRecipes.instance().addSmeltingRecipe(new ItemStack(in), out, xp);
	}
	
	private static void smelting(ItemStack in, ItemStack out, float xp)
	{
		FurnaceRecipes.instance().addSmeltingRecipe(in, out, xp);
	}
	
	private static void shaped(ItemStack out, Object... recipeComponents)
	{
		recipes.add(SimpleRegistration.parseShapedRecipe(out, recipeComponents).setRegistryName(InfoVT.MOD_ID, "recipes." + recipes.size()));
	}
	
	private static void shaped(Item out, Object... recipeComponents)
	{
		recipes.add(SimpleRegistration.parseShapedRecipe(new ItemStack(out), recipeComponents).setRegistryName(InfoVT.MOD_ID, "recipes." + recipes.size()));
	}
	
	private static void shaped(Block out, Object... recipeComponents)
	{
		recipes.add(SimpleRegistration.parseShapedRecipe(new ItemStack(out), recipeComponents).setRegistryName(InfoVT.MOD_ID, "recipes." + recipes.size()));
	}
	
	private static void shapeless(ItemStack out, Object... recipeComponents)
	{
		recipes.add(SimpleRegistration.parseShapelessRecipe(out, recipeComponents).setRegistryName(InfoVT.MOD_ID, "recipes." + recipes.size()));
	}
	
	private static void shapeless(Item out, Object... recipeComponents)
	{
		recipes.add(SimpleRegistration.parseShapelessRecipe(new ItemStack(out), recipeComponents).setRegistryName(InfoVT.MOD_ID, "recipes." + recipes.size()));
	}
	
	private static void recipe(IRecipe recipe)
	{
		recipes.add(recipe);
	}
	
	private static void shapeless(Block out, Object... recipeComponents)
	{
		recipes.add(SimpleRegistration.parseShapelessRecipe(new ItemStack(out), recipeComponents).setRegistryName(InfoVT.MOD_ID, "recipes." + recipes.size()));
	}
}