package com.pengu.vanillatech.tile;

import java.util.Map;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;

import com.pengu.hammercore.tile.TileSyncableTickable;
import com.pengu.hammercore.utils.ListUtils;
import com.pengu.vanillatech.init.DamageSourcesVT;
import com.pengu.vanillatech.process.ProcessHitEntityWithZap;

public class TileAnriatphyteBlock extends TileSyncableTickable
{
	public int delay = 0;
	
	@Override
	public void addProperties(Map<String, Object> properties, RayTraceResult trace)
	{
		properties.put("Cooldown", delay);
	}
	
	@Override
	public void tick()
	{
		if(world.isRemote)
			return;
		if(delay > 0)
		{
			--delay;
			if(atTickRate(20) || delay == 0)
				sendChangesToNearby();
		} else
		{
			EntityLivingBase ent = ListUtils.random(world.getEntitiesWithinAABB(EntityLivingBase.class, loc.getAABB().grow(8), en -> !(en instanceof EntityPlayer)), rand);
			if(ent != null && rand.nextInt(40) < 2)
				doHitEntity(ent);
		}
	}
	
	public void doHitEntity(EntityLivingBase ent)
	{
		if(world.isRemote || ent.isEntityInvulnerable(DamageSourcesVT.ANRIATPHYTE_BLOCK))
			return;
		new ProcessHitEntityWithZap(20 + rand.nextInt(60), 0xFFF7777, new Vec3d(pos).addVector(.5, .5, .5), ent, DamageSourcesVT.ANRIATPHYTE_BLOCK, 10).spawn();
		delay += 100 + rand.nextInt(500) + ent.getHealth() * 2;
	}
	
	@Override
	public void writeNBT(NBTTagCompound nbt)
	{
		nbt.setInteger("HurtTime", delay);
	}
	
	@Override
	public void readNBT(NBTTagCompound nbt)
	{
		delay = nbt.getInteger("HurtTime");
	}
}