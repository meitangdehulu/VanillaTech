package com.pengu.vanillatech.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockWitherProofRedStone extends Block
{
	public BlockWitherProofRedStone()
	{
		super(Material.ROCK, MapColor.RED);
		setUnlocalizedName("witherproof_red_stone");
		setHardness(10F);
		setResistance(Float.POSITIVE_INFINITY);
		setHarvestLevel("pickaxe", 3);
	}
	
	@Override
	public boolean canEntityDestroy(IBlockState state, IBlockAccess world, BlockPos pos, Entity entity)
	{
		return false;
	}
}