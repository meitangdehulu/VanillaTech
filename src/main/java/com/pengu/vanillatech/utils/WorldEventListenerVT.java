package com.pengu.vanillatech.utils;

import java.util.List;
import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IWorldEventListener;
import net.minecraft.world.World;

import com.pengu.hammercore.HammerCore;
import com.pengu.vanillatech.init.BlocksVT;
import com.pengu.vanillatech.init.DamageSourcesVT;

public class WorldEventListenerVT implements IWorldEventListener
{
	public final World world;
	
	public WorldEventListenerVT(World world)
	{
		this.world = world;
	}
	
	@Override
	public void notifyBlockUpdate(World worldIn, BlockPos pos, IBlockState oldState, IBlockState newState, int flags)
	{
	}
	
	@Override
	public void notifyLightSet(BlockPos pos)
	{
	}
	
	@Override
	public void markBlockRangeForRenderUpdate(int x1, int y1, int z1, int x2, int y2, int z2)
	{
	}
	
	@Override
	public void playSoundToAllNearExcept(EntityPlayer player, SoundEvent soundIn, SoundCategory category, double x, double y, double z, float volume, float pitch)
	{
	}
	
	@Override
	public void playRecord(SoundEvent soundIn, BlockPos pos)
	{
	}
	
	@Override
	public void spawnParticle(int particleID, boolean ignoreRange, double xCoord, double yCoord, double zCoord, double xSpeed, double ySpeed, double zSpeed, int... parameters)
	{
	}
	
	@Override
	public void spawnParticle(int id, boolean ignoreRange, boolean p_190570_3_, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, int... parameters)
	{
	}
	
	@Override
	public void onEntityAdded(Entity entityIn)
	{
	}
	
	@Override
	public void onEntityRemoved(Entity entityIn)
	{
	}
	
	@Override
	public void broadcastSound(int soundID, BlockPos pos, int data)
	{
	}
	
	@Override
	public void playEvent(EntityPlayer player, int type, BlockPos blockPosIn, int data)
	{
		if(type == 1031)
		{
			BlockPos pos = blockPosIn.down();
			if(world.getBlockState(pos).getBlock() == BlocksVT.UNSTABLE_METAL_BLOCK)
			{
				world.destroyBlock(pos, false);
				world.setBlockState(pos, BlocksVT.ANRIATPHYTE_BLOCK.getDefaultState());
				
				HammerCore.audioProxy.playSoundAt(world, "entity.elder_guardian.curse", pos, 4F, 1F, SoundCategory.BLOCKS);
				
				Random r = world.rand;
				int zaps = 32 + r.nextInt(32);
				for(int i = 0; i < zaps; ++i)
				{
					Vec3d a = new Vec3d(pos).addVector(.5, .5, .5);
					Vec3d b = a.addVector((r.nextDouble() - r.nextDouble()) * 12, r.nextDouble() * 12, (r.nextDouble() - r.nextDouble()) * 12);
					HammerCore.particleProxy.spawnSlowZap(world, a, b, 0xCCFFCC, 16, .15F);
					
					List<Entity> entsW = world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(a.x, a.y, a.z, b.x, b.y, b.z), ent -> ent != null && !EntityItem.class.isAssignableFrom(ent.getClass()));
					for(Entity ew : entsW)
						ew.attackEntityFrom(DamageSourcesVT.NETHER_STAR_DISSOLVE, 1000F);
				}
			}
		}
	}
	
	@Override
	public void sendBlockBreakProgress(int breakerId, BlockPos pos, int progress)
	{
	}
}