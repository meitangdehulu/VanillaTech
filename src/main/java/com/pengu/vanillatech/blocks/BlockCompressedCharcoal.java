package com.pengu.vanillatech.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;

public class BlockCompressedCharcoal extends Block implements iBurnableBlock
{
	public BlockCompressedCharcoal()
	{
		super(Material.ROCK);
		setUnlocalizedName("compressed_charcoal_block");
		setHardness(8F);
		setResistance(5F);
	}
	
	@Override
	public int getBurnTime(ItemStack stack)
	{
		return 160_000;
	}
}