package com.pengu.vanillatech.tile;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;

import java.util.List;

import com.pengu.hammercore.api.explosion.CustomExplosion;
import com.pengu.hammercore.common.utils.WorldUtil;
import com.pengu.hammercore.net.HCNetwork;
import com.pengu.hammercore.net.utils.NetPropertyNumber;
import com.pengu.hammercore.tile.TileSyncableTickable;
import com.pengu.hammercore.tile.tooltip.ProgressBar;
import com.pengu.hammercore.tile.tooltip.iTooltipTile;
import com.pengu.vanillatech.init.BlocksVT;
import com.pengu.vanillatech.init.SoundsVT;
import com.pengu.vanillatech.net.PacketExplosion;

public class TileTimeBomb extends TileSyncableTickable implements iTooltipTile
{
	public ProgressBar bar = new ProgressBar(100L);
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
		
		if(atTickRate(2))
			sendChangesToNearby();
		
		if(timeElapsed == 60)
		{
			SoundsVT.TIME_BOMB_EXPLODE.playAt(loc, 20F, 1F, SoundCategory.BLOCKS);
			loc.setState(BlocksVT.TIME_BOMB.getStateFromMeta(1));
			loc.setTile(this);
			validate();
			sendChangesToNearby();
		}
		
		if(timeElapsed == 20)
		{
			loc.setState(BlocksVT.TIME_BOMB.getStateFromMeta(2));
			loc.setTile(this);
			validate();
			sendChangesToNearby();
		}
		
		if(timeElapsed == 5)
		{
			PacketExplosion expl = new PacketExplosion();
			expl.x = pos.getX() + .5F;
			expl.y = pos.getY() + .5F;
			expl.z = pos.getZ() + .5F;
			HCNetwork.manager.sendToDimension(expl, world.provider.getDimension());
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

	@Override
	public void getTextTooltip(List<String> list, EntityPlayer player)
	{
	}
	
	@Override
	public boolean hasProgressBars(EntityPlayer player)
	{
		return false;
	}
	
	@Override
	public ProgressBar[] getProgressBars(EntityPlayer player)
	{
		bar.setMaxValue(20 * 10);
		bar.setProgress(timeElapsed);
		
		bar.filledMainColor = 0xFF00FF00;
		bar.filledAlternateColor = 0xFF009600;
		bar.backgroundColor = 0xFF005100;
		bar.borderColor = 0xFF757575;
		
		bar.prefix = "Time Until Detonation";
		return new ProgressBar[] { bar };
	}
}