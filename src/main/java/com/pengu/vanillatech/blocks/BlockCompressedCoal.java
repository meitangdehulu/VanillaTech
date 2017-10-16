package com.pengu.vanillatech.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;

public class BlockCompressedCoal extends Block implements iBurnableBlock
{
	public BlockCompressedCoal()
	{
		super(Material.ROCK);
		setUnlocalizedName("compressed_coal_block");
		setHardness(8F);
		setResistance(5F);
	}
	
	@Override
	public int getBurnTime(ItemStack stack)
	{
		return 160_000;
	}
}