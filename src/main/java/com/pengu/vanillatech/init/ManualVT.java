package com.pengu.vanillatech.init;

import com.pengu.hammercore.bookAPI.fancy.ManualCategories;
import com.pengu.hammercore.bookAPI.fancy.ManualCategory;
import com.pengu.hammercore.bookAPI.fancy.ManualEntry;
import com.pengu.hammercore.bookAPI.fancy.ManualPage;
import com.pengu.hammercore.bookAPI.fancy.ManualPage.PageType;
import com.pengu.vanillatech.InfoVT;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class ManualVT
{
	public static void init()
	{
		ManualCategories.registerCategory("vanillatech", new ResourceLocation(InfoVT.MOD_ID, "textures/gui/manual_icon.png"), new ResourceLocation(InfoVT.MOD_ID, "textures/gui/manual_back.png"));
		
		make("netherstar_shard", 0, 0, new ItemStack(ItemsVT.NETHERSTAR_SHARD, 8)).setPages(new ManualPage("vt.manual_desc.netherstar_shard"), new ManualPage(new ItemStack(Items.NETHER_STAR), PageType.NORMAL_CRAFTING)).registerEntry();
		make("backpack", 0, -2, new ItemStack(ItemsVT.BACKPACK)).setPages(new ManualPage("vt.manual_desc.backpack"), new ManualPage(new ItemStack(ItemsVT.BACKPACK), PageType.NORMAL_CRAFTING)).registerEntry();
		make("enhanced_pickaxe", 1, -1, new ItemStack(ItemsVT.ENHANCED_PICKAXE)).setPages(new ManualPage("vt.manual_desc.enhanced_pickaxe.1"), new ManualPage(new ItemStack(ItemsVT.ENHANCED_PICKAXE), PageType.NORMAL_CRAFTING), new ManualPage("vt.manual_desc.enhanced_pickaxe.2")).setParents("vt.netherstar_shard").registerEntry();
		make("unstable_metal", 1, 1, new ItemStack(ItemsVT.UNSTABLE_METAL_INGOT)).setPages(new ManualPage("vt.manual_desc.unstable_metal"), new ManualPage(ItemStack.EMPTY, PageType.NORMAL_CRAFTING).setRecipe(new ItemStack[] { new ItemStack(ItemsVT.UNSTABLE_METAL_INGOT), new ItemStack(BlocksVT.UNSTABLE_METAL_BLOCK) })).setParents("vt.netherstar_shard").registerEntry();		
		make("enhanced_furnace", 0, 2, new ItemStack(BlocksVT.ENHANCED_FURNACE)).setPages(new ManualPage("vt.manual_desc.enhanced_furnace"), new ManualPage(new ItemStack(BlocksVT.ENHANCED_FURNACE), PageType.NORMAL_CRAFTING)).setParents("vt.unstable_metal").registerEntry();
		make("anriatphyte_crystal", 2, 2, new ItemStack(ItemsVT.ANRIATPHYTE_CRYSTAL)).setPages(new ManualPage("vt.manual_desc.anriatphyte_crystal"), new ManualPage(ItemStack.EMPTY, PageType.NORMAL_CRAFTING).setRecipe(new ItemStack[] { new ItemStack(ItemsVT.ANRIATPHYTE_CRYSTAL), new ItemStack(BlocksVT.ANRIATPHYTE_BLOCK) })).setParents("vt.unstable_metal").registerEntry();
	}
	
	public static ManualEntry make(String id, int x, int y, ItemStack icon)
	{
		return new ManualEntry("vt." + id, "vanillatech", x, y, icon);
	}
}