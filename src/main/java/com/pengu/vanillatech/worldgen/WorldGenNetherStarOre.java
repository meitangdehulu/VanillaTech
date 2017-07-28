package com.pengu.vanillatech.worldgen;

import java.util.Random;

import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenMinable;

import com.pengu.hammercore.world.gen.IWorldGenFeature;
import com.pengu.hammercore.world.gen.WorldGenMinableHC;
import com.pengu.vanillatech.init.BlocksVT;

public class WorldGenNetherStarOre implements IWorldGenFeature
{
	@Override
	public int getMaxChances(World world, ChunkPos chunk, Random rand)
	{
		return world.provider.getDimensionType() == DimensionType.OVERWORLD ? 3 : 1;
	}
	
	@Override
	public int getMinY(World world, BlockPos pos, Random rand)
	{
		return 10;
	}
	
	@Override
	public int getMaxY(World world, BlockPos pos, Random rand)
	{
		return 11;
	}
	
	@Override
	public void generate(World world, BlockPos pos, Random rand)
	{
		if(rand.nextInt(4) == 0 && WorldGenMinableHC.stonePredicate.apply(world.getBlockState(pos)))
		{
			BlockPos ps = new BlockPos(pos);
			for(int i = 0; i < 8; ++i)
			{
				new WorldGenMinable(Blocks.DIAMOND_ORE.getDefaultState(), 16).generate(world, rand, ps);
				
				boolean diamondNear = false;
				
				int rad = 8;
				search: for(int x = -rad; x <= rad; ++x)
					for(int y = -rad; y <= rad; ++y)
						for(int z = -rad; z <= rad; ++z)
							if(world.getBlockState(pos.add(x, y, z)).getBlock() == Blocks.DIAMOND_ORE)
							{
								diamondNear = true;
								break search;
							}
				
				if(diamondNear)
				{
					world.setBlockState(pos, BlocksVT.NETHERSTAR_ORE.getDefaultState());
					break;
				}
			}
		}
	}
}