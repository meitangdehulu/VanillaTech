package com.pengu.vanillatech.process;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.pengu.hammercore.HammerCore;
import com.pengu.hammercore.api.iProcess;
import com.pengu.hammercore.common.utils.WorldUtil;
import com.pengu.hammercore.net.HCNetwork;
import com.pengu.hammercore.utils.AdvancementUtils;
import com.pengu.hammercore.utils.WorldLocation;
import com.pengu.vanillatech.Info;
import com.pengu.vanillatech.init.BlocksVT;
import com.pengu.vanillatech.init.DamageSourcesVT;
import com.pengu.vanillatech.init.ItemsVT;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ProcessConvertIronToUnstable implements iProcess
{
	private static final Map<Integer, Map<Long, ProcessConvertIronToUnstable>> PROCESSES = new LinkedHashMap<>();
	
	private int ticksExisted = 0;
	private final WorldLocation loc;
	
	@Nonnull
	public static ProcessConvertIronToUnstable startProcess(WorldLocation loc)
	{
		ProcessConvertIronToUnstable p = getProcess(loc);
		if(p == null)
		{
			p = new ProcessConvertIronToUnstable(loc);
			HammerCore.updatables.add(p);
		}
		return p;
	}
	
	@Nullable
	public static ProcessConvertIronToUnstable getProcess(WorldLocation loc)
	{
		int dim = 0;
		Map<Long, ProcessConvertIronToUnstable> p = PROCESSES.get(dim = loc.getWorld().provider.getDimension());
		if(p == null)
			PROCESSES.put(dim, p = new LinkedHashMap<>());
		ProcessConvertIronToUnstable pr = p.get(loc.getPos().toLong());
		if(pr != null && !pr.isAlive())
			pr = null;
		return pr;
	}
	
	public ProcessConvertIronToUnstable(WorldLocation loc)
	{
		this.loc = loc;
		
		int dim = 0;
		Map<Long, ProcessConvertIronToUnstable> p = PROCESSES.get(dim = loc.getWorld().provider.getDimension());
		if(p == null)
			PROCESSES.put(dim, p = new LinkedHashMap<>());
		
		p.put(loc.getPos().toLong(), this);
	}
	
	@Override
	public void update()
	{
		++ticksExisted;
		
		World world = loc.getWorld();
		BlockPos pos = loc.getPos();
		
		IBlockState state = world.getBlockState(pos);
		
		if(state.getBlock() != Blocks.IRON_BLOCK)
		{
			ticksExisted = Integer.MAX_VALUE;
			WorldUtil.spawnItemStack(loc, new ItemStack(ItemsVT.NETHERSTAR_SHARD));
			HammerCore.audioProxy.playSoundAt(world, "entity.generic.explode", pos, 10F, 1F, SoundCategory.BLOCKS);
			HCNetwork.spawnParticle(world, EnumParticleTypes.EXPLOSION_HUGE, pos.getX() + .5, pos.getY() + .5, pos.getZ() + .5, 0, 0, 0);
			return;
		}
		
		float a = (2400 - ticksExisted) / 2400F;
		
		if(world.rand.nextFloat() <= a)
		{
			int maxTicks = Math.round(25 - a * 20) + 10;
			
			double x = pos.getX() + .5 + (world.rand.nextDouble() - world.rand.nextDouble()) * (a + .2) * 5D;
			double y = pos.getY() + .5 + (world.rand.nextDouble() - world.rand.nextDouble()) * (a + .2) * 5D;
			double z = pos.getZ() + .5 + (world.rand.nextDouble() - world.rand.nextDouble()) * (a + .2) * 5D;
			
			HammerCore.particleProxy.spawnSlowZap(world, new Vec3d(x, y, z), new Vec3d(pos.getX() + .5, pos.getY() + .5, pos.getZ() + .5), 0xFFFFFF, maxTicks, .4F);
			
			List<Entity> ents = world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(x, y, z, pos.getX() + .5, pos.getY() + .5, pos.getZ() + .5), ent -> ent != null && !EntityItem.class.isAssignableFrom(ent.getClass()));
			for(Entity e : ents)
				e.attackEntityFrom(DamageSourcesVT.UNSTABLE_BLOCK_FORM, 1000F);
		}
		
		if(!isAlive())
		{
			loc.setState(BlocksVT.UNSTABLE_METAL_BLOCK.getDefaultState());
			HammerCore.audioProxy.playSoundAt(world, "entity.generic.explode", pos, 10F, .8F, SoundCategory.BLOCKS);
			HCNetwork.spawnParticle(world, EnumParticleTypes.EXPLOSION_HUGE, pos.getX() + .5, pos.getY() + .5, pos.getZ() + .5, 0, 0, 0);
			int dim = 0;
			Map<Long, ProcessConvertIronToUnstable> p = PROCESSES.get(dim = world.provider.getDimension());
			if(p == null)
				PROCESSES.put(dim, p = new LinkedHashMap<>());
			p.remove(pos.toLong());
			
			HammerCore.audioProxy.playSoundAt(world, "entity.elder_guardian.curse", pos, 10F, 1F, SoundCategory.NEUTRAL);
			
			int r = world.rand.nextInt(64);
			for(int i = 0; i < 64 + r; ++i)
			{
				int maxTicks = 20;
				
				double x = pos.getX() + .5 + (world.rand.nextDouble() - world.rand.nextDouble()) * 12D;
				double y = pos.getY() + .5 + world.rand.nextDouble() * 12D;
				double z = pos.getZ() + .5 + (world.rand.nextDouble() - world.rand.nextDouble()) * 12D;
				
				HammerCore.particleProxy.spawnSlowZap(world, new Vec3d(pos.getX() + .5, pos.getY() + .5, pos.getZ() + .5), new Vec3d(x, y, z), 0xFFFFFF, maxTicks, .4F);
			}
			
			List<Entity> ents = world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(pos).grow(12), ent -> ent != null && !EntityItem.class.isAssignableFrom(ent.getClass()));
			for(Entity e : ents)
				e.attackEntityFrom(DamageSourcesVT.UNSTABLE_BLOCK_FORM, 1000F);
			
			List<EntityPlayerMP> pls = world.getEntitiesWithinAABB(EntityPlayerMP.class, new AxisAlignedBB(pos).grow(48));
			for(EntityPlayerMP e : pls)
				AdvancementUtils.completeAdvancement(new ResourceLocation(Info.MOD_ID, "main/unstable_metal_block"), e);
		}
	}
	
	@Override
	public boolean isAlive()
	{
		return ticksExisted < 2400;
	}
}