package com.pengu.vanillatech.tile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import com.pengu.hammercore.HammerCore;
import com.pengu.hammercore.tile.TileSyncableTickable;
import com.pengu.hammercore.tile.tooltip.iTooltipTile;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;

public class TileRedstoneTeleporter extends TileSyncableTickable implements iTooltipTile
{
	public static final Map<String, Integer> ENABLED = new HashMap<>();
	
	public int activeTicks = 0;
	public UUID uuid;
	public boolean isActive = false;
	public EnumFacing direction;
	public int redstone;
	public boolean isReceiving = false;
	public Integer color;
	
	public TileRedstoneTeleporter(UUID uuid)
	{
		this.uuid = uuid;
		
		Random rand = new Random(uuid != null ? uuid.getMostSignificantBits() - uuid.getLeastSignificantBits() : 0L);
		color = uuid != null ? rand.nextInt(0xFFFFFF) : 0xFF7777;
	}
	
	public TileRedstoneTeleporter()
	{
	}
	
	@Override
	public void addProperties(Map<String, Object> properties, RayTraceResult trace)
	{
		properties.put("uuid", uuid);
		properties.put("active", isActive);
		properties.put("receiving", isReceiving);
		if(direction != null)
			properties.put("direction", direction.getName());
		properties.put("strength", redstone);
	}
	
	public int getColor()
	{
		if(color == null)
		{
			Random rand = new Random(uuid != null ? uuid.getMostSignificantBits() - uuid.getLeastSignificantBits() : 0L);
			color = uuid != null ? rand.nextInt(0xFFFFFF) : 0xFF7777;
		}
		
		return color;
	}
	
	@Override
	public void tick()
	{
		getColor();
		
		if(redstone > 0)
		{
			if(activeTicks < 10)
				++activeTicks;
		} else if(activeTicks > 0)
			--activeTicks;
		else
			activeTicks = 0;
		
		if(world.isRemote || direction == null)
			return;
		
		boolean zap = false;
		
		if(!isReceiving)
		{
			BlockPos emitter = pos.offset(direction);
			IBlockState state = world.getBlockState(emitter);
			redstone = world.getRedstonePower(emitter, direction);
			isActive = redstone > 0;
			int remoteActive = ENABLED.get(uuid.toString()) != null ? ENABLED.get(uuid.toString()) : 0;
			if(remoteActive != redstone)
			{
				ENABLED.put(uuid.toString(), redstone);
				sendChangesToNearby();
				zap = true;
			}
		} else
		{
			int remoteActive = ENABLED.get(uuid.toString()) != null ? ENABLED.get(uuid.toString()) : 0;
			if(redstone != remoteActive)
			{
				isActive = remoteActive > 0;
				redstone = remoteActive;
				loc.markDirty(3);
				sendChangesToNearby();
				zap = true;
			}
		}
		
		if(zap || (isActive && atTickRate(10) && rand.nextInt(20) == 0))
		{
			Vec3d i0 = new Vec3d(pos).addVector(direction == EnumFacing.EAST ? .25 : direction == EnumFacing.WEST ? .75 : .5, .5, direction == EnumFacing.SOUTH ? .25 : direction == EnumFacing.NORTH ? .75 : .5);
			Vec3d i1 = null;
			
			int tempts = 10;
			while(tempts-- > 0 && (i1 == null || i0.distanceTo(i1) < .25))
				i1 = new Vec3d(pos).addVector((rand.nextDouble() - rand.nextDouble()) * 1.5, rand.nextDouble() + .5, (rand.nextDouble() - rand.nextDouble()) * 1.5);
			
			Vec3d start = isReceiving ? i1 : i0;
			Vec3d end = isReceiving ? i0 : i1;
			
			Random rand = new Random(uuid != null ? uuid.getMostSignificantBits() - uuid.getLeastSignificantBits() : 0L);
			int color = uuid != null ? rand.nextInt(0xFFFFFF) : 0xFF7777;
			HammerCore.particleProxy.spawnSlowZap(world, start, end, color, 3, .3F);
		}
		
		if(atTickRate(20))
			sendChangesToNearby();
	}
	
	@Override
	public void writeNBT(NBTTagCompound nbt)
	{
		if(uuid != null)
			nbt.setUniqueId("UUID", uuid);
		if(direction != null)
			nbt.setString("Direction", direction.getName());
		nbt.setBoolean("Sending", !isReceiving);
		nbt.setBoolean("IsActive", isActive);
		nbt.setInteger("Redstone", redstone);
		if(color != null)
			nbt.setInteger("Color", color);
	}
	
	@Override
	public void readNBT(NBTTagCompound nbt)
	{
		if(nbt.hasKey("UUIDMost"))
			uuid = nbt.getUniqueId("UUID");
		direction = EnumFacing.byName(nbt.getString("Direction"));
		isReceiving = !nbt.getBoolean("Sending");
		isActive = nbt.getBoolean("IsActive");
		redstone = nbt.getInteger("Redstone");
		if(nbt.hasKey("Color"))
			color = nbt.getInteger("Color");
	}
	
	@Override
	public void getTextTooltip(List<String> list, EntityPlayer player)
	{
		list.add("Mode: " + (isReceiving ? "Receiving" : "Sending"));
		list.add("Power: " + redstone);
	}
}