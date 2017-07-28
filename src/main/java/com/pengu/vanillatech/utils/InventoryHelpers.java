package com.pengu.vanillatech.utils;

import javax.annotation.Nullable;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;

import com.pengu.hammercore.common.InterItemStack;
import com.pengu.hammercore.common.utils.WorldUtil;

public class InventoryHelpers
{
	/**
	 * Return first slot that is either empty or slot that can accept this stack
	 * or -1 if container is full
	 */
	public static int queryItemOrEmptySlot(IInventory inv, ItemStack stack, boolean checkCanInsert, @Nullable EnumFacing from)
	{
		for(int i = 0; i < inv.getSizeInventory(); ++i)
			if((!checkCanInsert && stacksEqual(stack, inv.getStackInSlot(i)) && inv.getStackInSlot(i).getCount() < Math.min(stack.getMaxStackSize(), inv.getInventoryStackLimit())) || (checkCanInsert && canInsert(inv, i, stack, from)))
				return i;
		return -1;
	}
	
	public static boolean putItem(IInventory inv, int slot, ItemStack stack)
	{
		if(inv.getSizeInventory() <= slot)
			return false;
		if(inv.getStackInSlot(slot).isEmpty())
		{
			inv.setInventorySlotContents(slot, stack.copy());
			stack.setCount(0);
			return true;
		} else if(stacksEqual(stack, inv.getStackInSlot(slot)) && inv.getStackInSlot(slot).getCount() < Math.min(stack.getMaxStackSize(), inv.getInventoryStackLimit()))
		{
			int put = Math.min(stack.getCount(), stack.getMaxStackSize() - inv.getStackInSlot(slot).getCount());
			inv.getStackInSlot(slot).grow(put);
			stack.shrink(put);
		}
		return false;
	}
	
	public static boolean canInsert(IInventory inv, int slot, ItemStack stack, @Nullable EnumFacing from)
	{
		if(inv.getSizeInventory() <= slot)
			return false;
		
		ISidedInventory sided = WorldUtil.cast(inv, ISidedInventory.class);
		if((sided != null && !sided.canInsertItem(slot, stack, from)) || (sided == null && !inv.isItemValidForSlot(slot, stack)))
			return false;
		
		if(inv.getStackInSlot(slot).isEmpty())
			return true;
		if(stacksEqual(stack, inv.getStackInSlot(slot)) && inv.getStackInSlot(slot).getCount() < Math.min(stack.getMaxStackSize(), inv.getInventoryStackLimit()))
			return true;
		return false;
	}
	
	public static boolean stacksEqual(ItemStack a, ItemStack b)
	{
		if(a == b || (a.isEmpty() && b.isEmpty()))
			return true;
		if(!InterItemStack.isStackNull(a) && !InterItemStack.isStackNull(b) && a.getItem() == b.getItem() && a.getItemDamage() == b.getItemDamage())
		{
			if(a.hasTagCompound() && b.hasTagCompound())
			{
				if(ItemStack.areItemStackTagsEqual(a, b))
					return true;
			} else if(!a.hasTagCompound() && !b.hasTagCompound())
				return true;
		}
		return false;
	}
}