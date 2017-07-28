package com.pengu.vanillatech.tile;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;

import com.pengu.hammercore.api.explosion.CustomExplosion;
import com.pengu.hammercore.common.utils.WorldUtil;
import com.pengu.hammercore.net.utils.NetPropertyNumber;
import com.pengu.hammercore.tile.TileSyncableTickable;
import com.pengu.vanillatech.init.BlocksVT;
import com.pengu.vanillatech.init.SoundsVT;

public class TileTimeBomb extends TileSyncableTickable
{
	public int timeElapsed = 20 * 10;
	public final NetPropertyNumber<Integer> PLACER_ID;
	
	{
		PLACER_ID = new NetPropertyNumber<Integer>(this, -1);
	}
	
	public TileTimeBomb()
	{
	}
	
	public TileTimeBomb(EntityLivingBase placer)
	{
		PLACER_ID.set(placer.getEntityId());
	}
	
	@Override
	public void tick()
	{
		if(world.isRemote || timeElapsed == -1)
			return;
		
		--timeElapsed;
		
		if(timeElapsed == 60)
		{
			SoundsVT.TIME_BOMB_EXPLODE.playAt(loc, 20F, 1F, SoundCategory.BLOCKS);
			loc.setState(BlocksVT.TIME_BOMB.getStateFromMeta(1));
			loc.setTile(this);
			validate();
		}
		
		if(timeElapsed == 20)
		{
			loc.setState(BlocksVT.TIME_BOMB.getStateFromMeta(2));
			loc.setTile(this);
			validate();
		}
		
		if(timeElapsed == 0)
		{
			loc.setAir();
			loc.playSound("entity.generic.explode", 20F, .2F, SoundCategory.BLOCKS);
			EntityLivingBase placer = PLACER_ID.get() != -1 ? WorldUtil.cast(world.getEntityByID(PLACER_ID.get()), EntityLivingBase.class) : null;
			CustomExplosion.doExplosionAt(world, pos, 4F, placer != null ? DamageSource.causeExplosionDamage(placer) : DamageSource.GENERIC, false);
		}
	}
	
	@Override
	public void writeNBT(NBTTagCompound nbt)
	{
		nbt.setInteger("TimeLeft", timeElapsed);
	}
	
	@Override
	public void readNBT(NBTTagCompound nbt)
	{
		timeElapsed = nbt.getInteger("TimeLeft");
	}
}