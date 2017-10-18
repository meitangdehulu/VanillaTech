package com.pengu.vanillatech.tile;

import java.util.List;
import java.util.Map;

import com.pengu.hammercore.tile.TileSyncableTickable;
import com.pengu.hammercore.tile.tooltip.ProgressBar;
import com.pengu.hammercore.tile.tooltip.iTooltipTile;
import com.pengu.hammercore.utils.ListUtils;
import com.pengu.vanillatech.init.DamageSourcesVT;
import com.pengu.vanillatech.process.ProcessHitEntityWithZap;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;

public class TileAnriatphyteBlock extends TileSyncableTickable implements iTooltipTile
{
	public ProgressBar bar = new ProgressBar(1000L);
	public int delay = 0, lastCooldownLim = 0;
	
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
		lastCooldownLim = (delay += 100 + rand.nextInt(500) + ent.getHealth() * 2);
	}
	
	@Override
	public void writeNBT(NBTTagCompound nbt)
	{
		nbt.setInteger("HurtTime", delay);
		nbt.setInteger("Visual", lastCooldownLim);
	}
	
	@Override
	public void readNBT(NBTTagCompound nbt)
	{
		delay = nbt.getInteger("HurtTime");
		lastCooldownLim = nbt.getInteger("Visual");
	}
	
	@Override
	public void getTextTooltip(List<String> list, EntityPlayer player)
	{
	}
	
	@Override
	public boolean hasProgressBars(EntityPlayer player)
	{
		return true;
	}
	
	@Override
	public ProgressBar[] getProgressBars(EntityPlayer player)
	{
		bar.setProgress(delay);
		if(lastCooldownLim > 0)
			bar.setMaxValue(lastCooldownLim);
		
		bar.filledMainColor = 0xFF00FF00;
		bar.filledAlternateColor = 0xFF009600;
		bar.backgroundColor = 0xFF005100;
		bar.borderColor = 0xFF757575;
		
		bar.prefix = "Cooldown";
		
		return new ProgressBar[] { bar };
	}
}