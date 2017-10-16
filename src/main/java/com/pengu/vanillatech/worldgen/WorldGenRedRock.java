package com.pengu.vanillatech.worldgen;

import java.util.Random;

import com.pengu.hammercore.world.gen.iWorldGenFeature;
import com.pengu.vanillatech.init.BlocksVT;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenMinable;

public class WorldGenRedRock implements iWorldGenFeature
{
	@Override
	public int getMaxChances(World world, ChunkPos chunk, Random rand)
	{
		return world.provider.getDimensionType() == DimensionType.OVERWORLD ? 2 : 1;
	}
	
	@Override
	public int getMinY(World world, BlockPos pos, Random rand)
	{
		return 6;
	}
	
	@Override
	public int getMaxY(World world, BlockPos pos, Random rand)
	{
		return 64;
	}
	
	public static final WorldGenMinable redStoneGenerator = new WorldGenMinable(BlocksVT.RED_STONE.getDefaultState(), 72);
	
	@Override
	public void generate(World world, BlockPos pos, Random rand)
	{
		redStoneGenerator.generate(world, rand, pos);
	}
}