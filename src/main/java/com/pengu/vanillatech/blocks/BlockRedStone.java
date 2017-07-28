package com.pengu.vanillatech.blocks;

import java.util.Random;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;

import com.pengu.vanillatech.init.BlocksVT;

public class BlockRedStone extends BlockOD
{
	public BlockRedStone()
	{
		super(Material.ROCK, MapColor.RED, "stone");
		setUnlocalizedName("red_stone");
		setHardness(1.5F);
		setResistance(10F);
	}
	
	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune)
	{
		return Item.getItemFromBlock(BlocksVT.RED_COBBLESTONE);
	}
}