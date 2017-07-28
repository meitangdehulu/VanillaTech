package com.pengu.vanillatech.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;

public class BlockCharcoal extends Block implements IBurnableBlock
{
	public BlockCharcoal()
    {
		super(Material.ROCK);
		setUnlocalizedName("charcoal_block");
		setHardness(3F);
		setResistance(5F);
    }
	
	@Override
	public int getBurnTime(ItemStack stack)
	{
		return 16_000;
	}
}