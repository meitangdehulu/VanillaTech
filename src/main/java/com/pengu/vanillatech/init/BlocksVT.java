package com.pengu.vanillatech.init;

import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;

import com.pengu.hammercore.utils.NPEUtils;
import com.pengu.vanillatech.blocks.BlockAnriatphyte;
import com.pengu.vanillatech.blocks.BlockCharcoal;
import com.pengu.vanillatech.blocks.BlockCompressedCharcoal;
import com.pengu.vanillatech.blocks.BlockCompressedCoal;
import com.pengu.vanillatech.blocks.BlockCustomCraftingTable;
import com.pengu.vanillatech.blocks.BlockEnhancedFurnace;
import com.pengu.vanillatech.blocks.BlockFisher;
import com.pengu.vanillatech.blocks.BlockGlowstoneOre;
import com.pengu.vanillatech.blocks.BlockNetherstarOre;
import com.pengu.vanillatech.blocks.BlockOD;
import com.pengu.vanillatech.blocks.BlockQuartzOre;
import com.pengu.vanillatech.blocks.BlockQuartzSpike;
import com.pengu.vanillatech.blocks.BlockRedStone;
import com.pengu.vanillatech.blocks.BlockRedstoneTeleporter;
import com.pengu.vanillatech.blocks.BlockTimeBomb;
import com.pengu.vanillatech.blocks.BlockUnstableMetal;
import com.pengu.vanillatech.blocks.BlockWitherProofRedStone;
import com.pengu.vanillatech.blocks.flora.BlockButtercup;
import com.pengu.vanillatech.blocks.flora.BlockDaisy;
import com.pengu.vanillatech.blocks.flora.BlockLavender;
import com.pengu.vanillatech.blocks.flora.BlockSlimyFlower;
import com.pengu.vanillatech.blocks.pim.BlockPlayerInventoryManager;
import com.pengu.vanillatech.cfg.ConfigsVT;

public class BlocksVT
{
	{
		NPEUtils.noInstancesError();
	}
	
	public static final Block //
	        CHARCOAL_BLOCK = new BlockCharcoal(), //
	        COMPRESSED_COAL_BLOCK = new BlockCompressedCoal(), //
	        COMPRESSED_CHARCOAL_BLOCK = new BlockCompressedCharcoal(), //
	        RED_STONE = new BlockRedStone(), //
	        WITHER_PROOF_RED_STONE = new BlockWitherProofRedStone(), //
	        ENHANCED_FURNACE = new BlockEnhancedFurnace(), //
	        QUARTZ_ORE = new BlockQuartzOre(), //
	        QUARTZ_SPIKE = new BlockQuartzSpike(), //
	        GLOWSTONE_ORE = new BlockGlowstoneOre(), //
	        NETHERSTAR_ORE = new BlockNetherstarOre(), //
	        UNSTABLE_METAL_BLOCK = new BlockUnstableMetal(), //
	        ANRIATPHYTE_BLOCK = new BlockAnriatphyte(), //
	        RED_COBBLESTONE = new BlockOD(Material.ROCK, MapColor.RED, "cobblestone").setUnlocalizedName("red_cobblestone").setHardness(2F).setResistance(10F), //
	        MOSSY_RED_COBBLESTONE = new Block(Material.ROCK, MapColor.RED).setUnlocalizedName("mossy_red_cobblestone").setHardness(2F).setResistance(10F), //
	        ACACIA_CRAFTING_TABLE = new BlockCustomCraftingTable("acacia"), //
	        BIRCH_CRAFTING_TABLE = new BlockCustomCraftingTable("birch"), //
	        DARK_OAK_CRAFTING_TABLE = new BlockCustomCraftingTable("dark_oak"), //
	        JUNGLE_CRAFTING_TABLE = new BlockCustomCraftingTable("jungle"), //
	        SPRUCE_CRAFTING_TABLE = new BlockCustomCraftingTable("spruce"), //
	        REDSTONE_TELEPORTER = new BlockRedstoneTeleporter(), //
	        FISHER = new BlockFisher(), //
	        PLAYER_INVENTORY_MANAGER = new BlockPlayerInventoryManager(), //
	        TIME_BOMB;
	
	public static final Block //
	        SLIMY_FLOWER = new BlockSlimyFlower(), //
	        LAVENDER = new BlockLavender(), //
	        BUTTERCUP = new BlockButtercup(), //
	        DAISY = new BlockDaisy();
	
	static
	{
		if(ConfigsVT.blocks_TimeBomb)
			TIME_BOMB = new BlockTimeBomb();
		else
			TIME_BOMB = null;
	}
}