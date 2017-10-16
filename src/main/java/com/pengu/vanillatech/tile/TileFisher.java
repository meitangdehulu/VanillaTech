package com.pengu.vanillatech.tile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootTableList;

import com.pengu.hammercore.common.utils.WorldUtil;
import com.pengu.hammercore.net.HCNetwork;
import com.pengu.hammercore.tile.TileSyncableTickable;
import com.pengu.vanillatech.net.PacketAddFisherItem;
import com.pengu.vanillatech.utils.AtomicTuple;
import com.pengu.vanillatech.utils.InventoryHelpers;

public class TileFisher extends TileSyncableTickable
{
	public int fishCooldown;
	public float fishDistance, fishApproachAngle;
	public boolean caught = true;
	public boolean flag1 = false;
	
	public List<AtomicTuple<Integer, ItemStack>> pickedUp = new ArrayList<>();
	
	@Override
	public void tick()
	{
		if(world.isRemote)
			for(int i = 0; i < pickedUp.size(); ++i)
			{
				AtomicTuple<Integer, ItemStack> pair = pickedUp.get(i);
				pair.setFirst(pair.getFirst() + 1);
				if(pair.getFirst() >= 20)
					pickedUp.remove(i);
			}
		
		if(fishCooldown <= 0 && caught)
			fishCooldown = 750 + rand.nextInt(750);
		
		if(fishCooldown > 0 && world.getBlockState(pos.down()).getBlock() == Blocks.WATER)
		{
			fishCooldown--;
			flag1 = false;
		}
		
		if(fishCooldown == 0)
		{
			if(!flag1)
			{
				fishDistance = rand.nextFloat() * 8F;
				fishApproachAngle = rand.nextFloat() * 360F;
				flag1 = true;
				caught = false;
			} else
			{
				fishDistance -= rand.nextFloat() * .2F;
				float currApproachAngle = (float) (fishApproachAngle + Math.sin(ticksExisted));
				
				float xOff = pos.getX() + .5F + (float) Math.sin(currApproachAngle) * fishDistance;
				float zOff = pos.getZ() + .5F + (float) Math.cos(currApproachAngle) * fishDistance;
				
				IBlockState state = world.getBlockState(new BlockPos(xOff, pos.getY() - 1, zOff));
				
				if(state.getBlock() == Blocks.WATER)
					for(int i = 0; i < 9; ++i)
						HCNetwork.spawnParticle(world, EnumParticleTypes.WATER_SPLASH, xOff, pos.getY(), zOff, 0, 0, 0);
				
				if(fishDistance <= .25F)
				{
					caught = true;
					for(ItemStack drop : getDrop())
						insertOrDrop(drop);
				}
			}
		}
	}
	
	@Override
	public void addProperties(Map<String, Object> properties, RayTraceResult trace)
	{
		properties.put("Cooldown", fishCooldown);
	}
	
	public void insertOrDrop(ItemStack stack)
	{
		if(world.isRemote)
			return;
		ItemStack toAnim = stack.copy();
		IInventory inv = WorldUtil.cast(world.getTileEntity(pos.up()), IInventory.class);
		if(inv != null)
		{
			int slot = InventoryHelpers.queryItemOrEmptySlot(inv, stack, true, EnumFacing.DOWN);
			while(slot != -1 && !stack.isEmpty())
			{
				InventoryHelpers.putItem(inv, slot, stack);
				slot = InventoryHelpers.queryItemOrEmptySlot(inv, stack, true, EnumFacing.DOWN);
			}
		}
		if(!stack.isEmpty())
			WorldUtil.spawnItemStack(loc, stack);
		else
			HCNetwork.manager.sendToAllAround(new PacketAddFisherItem(this, toAnim), getSyncPoint(128));
	}
	
	public List<ItemStack> getDrop()
	{
		if(!(world instanceof WorldServer))
			return Arrays.asList();
		LootContext.Builder builder = new LootContext.Builder((WorldServer) world);
		builder.withLuck(0);
		return world.getLootTableManager().getLootTableFromLocation(LootTableList.GAMEPLAY_FISHING).generateLootForPools(rand, builder.build());
	}
	
	@Override
	public void writeNBT(NBTTagCompound nbt)
	{
		nbt.setInteger("FishCooldown", fishCooldown);
	}
	
	@Override
	public void readNBT(NBTTagCompound nbt)
	{
		fishCooldown = nbt.getInteger("FishCooldown");
	}
}