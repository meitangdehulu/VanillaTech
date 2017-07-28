package com.pengu.vanillatech.blocks;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockQuartzOre extends BlockOD
{
	public BlockQuartzOre()
	{
		super(Material.ROCK, "oreQuartz");
		setUnlocalizedName("quartz_ore");
		setHardness(2F);
		setResistance(16F);
		setHarvestLevel("pickaxe", 2);
	}
	
	@Override
	public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player)
	{
		return true;
	}
	
	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune)
	{
		return Items.QUARTZ;
	}
}