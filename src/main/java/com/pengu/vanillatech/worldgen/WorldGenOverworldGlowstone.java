package com.pengu.vanillatech.worldgen;

import java.util.Random;

import com.pengu.hammercore.world.gen.iWorldGenFeature;
import com.pengu.vanillatech.init.BlocksVT;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenMinable;

public class WorldGenOverworldGlowstone implements iWorldGenFeature
{
	@Override
	public int getMaxChances(World world, ChunkPos chunk, Random rand)
	{
		return world.provider.getDimensionType() == DimensionType.OVERWORLD ? 16 : 1;
	}
	
	@Override
	public int getMinY(World world, BlockPos pos, Random rand)
	{
		return 6;
	}
	
	@Override
	public int getMaxY(World world, BlockPos pos, Random rand)
	{
		return 32;
	}
	
	public static final WorldGenMinable glowstoneGenerator = new WorldGenMinable(BlocksVT.GLOWSTONE_ORE.getDefaultState(), 6);
	
	@Override
	public void generate(World world, BlockPos pos, Random rand)
	{
		glowstoneGenerator.generate(world, rand, pos);
	}
}