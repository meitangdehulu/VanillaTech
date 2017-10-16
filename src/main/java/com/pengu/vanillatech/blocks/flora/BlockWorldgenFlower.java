package com.pengu.vanillatech.blocks.flora;

import java.util.Random;
import java.util.function.Predicate;

import com.pengu.hammercore.utils.iRegisterListener;
import com.pengu.hammercore.world.WorldGenHelper;
import com.pengu.hammercore.world.gen.WorldRetroGen;
import com.pengu.hammercore.world.gen.iWorldGenFeature;

import net.minecraft.block.BlockBush;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

public class BlockWorldgenFlower extends BlockBush implements iWorldGenFeature, iRegisterListener
{
	protected int worldgen_maxSpread = 6, worldgen_minCount = 2, worldgen_maxCount = 8;
	protected Predicate<Biome> worldgen_spawnableBiomes = b -> true;
	
	public BlockWorldgenFlower(String name)
	{
		setUnlocalizedName("flora/" + name);
		setSoundType(SoundType.PLANT);
	}
	
	@Override
	public void onRegistered()
	{
		WorldRetroGen.addWorldFeature(this);
	}
	
	@Override
	protected boolean canSustainBush(IBlockState state)
	{
		return WorldGenHelper.GRASS_OR_DIRT_CHECKER.test(state);
	}
	
	@Override
	public int getMaxChances(World world, ChunkPos chunk, Random rand)
	{
		return world.provider.getDimensionType() == DimensionType.OVERWORLD ? 2 : 1;
	}
	
	@Override
	public int getMinY(World world, BlockPos pos, Random rand)
	{
		return 250;
	}
	
	@Override
	public int getMaxY(World world, BlockPos pos, Random rand)
	{
		return 255;
	}
	
	@Override
	public void generate(World world, BlockPos pos, Random rand)
	{
		if(worldgen_spawnableBiomes.test(world.getBiome(pos)) && rand.nextInt(6) == 0)
			WorldGenHelper.generateFlower(getDefaultState(), rand, world, pos, worldgen_maxSpread, worldgen_minCount, worldgen_maxCount, false, WorldGenHelper.GRASS_OR_DIRT_CHECKER);
	}
}