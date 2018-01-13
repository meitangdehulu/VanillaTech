package com.pengu.vanillatech.intr.simplequarry.items;

import com.endie.simplequarry.items.ItemUpgrade;
import com.endie.simplequarry.tile.TilePoweredQuarry;
import com.pengu.vanillatech.init.BlocksVT;
import com.pengu.vanillatech.init.ItemsVT;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fluids.FluidRegistry;

public class ItemNetherStarMiningUpgrade extends ItemUpgrade
{
	public ItemNetherStarMiningUpgrade()
	{
		setUnlocalizedName("upgrade_nether_star");
		quarryUseMultiply = 2;
	}
	
	@Override
	public void handleDrops(TilePoweredQuarry quarry, BlockPos unused, NonNullList<ItemStack> drops)
	{
		if(quarry.y == 10)
		{
			World world = quarry.getWorld();
			Chunk c = world.getChunkFromBlockCoords(quarry.getPos());
			int chunkX = c.x;
			int chunkZ = c.z;
			
			block0: for(int x = 0; x < 16; ++x)
			{
				for(int z = 0; z < 16; ++z)
				{
					BlockPos pos = new BlockPos(chunkX * 16 + x, 10, chunkZ * 16 + z);
					IBlockState state = world.getBlockState(pos);
					Block b = state.getBlock();
					
					if(b != BlocksVT.NETHERSTAR_ORE)
						continue;
					
					drops.add(new ItemStack(ItemsVT.NETHERSTAR_SHARD, world.rand.nextInt(2) + 1));
					world.destroyBlock(pos, false);
					
					break block0;
				}
			}
		}
	}
}