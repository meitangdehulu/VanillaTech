package com.pengu.vanillatech.inventory.slot;

import com.pengu.vanillatech.items.ItemBackpack;
import com.pengu.vanillatech.items.ItemEnderBackpack;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotNotTakeableBackpack extends Slot
{
	public SlotNotTakeableBackpack(IInventory inventoryIn, int index, int xPosition, int yPosition)
	{
		super(inventoryIn, index, xPosition, yPosition);
	}
	
	@Override
	public boolean isItemValid(ItemStack stack)
	{
		if(stack.getItem() instanceof ItemBackpack || stack.getItem() instanceof ItemEnderBackpack)
			return false;
		return true;
	}
	
	@Override
	public boolean canTakeStack(EntityPlayer playerIn)
	{
		if(!getStack().isEmpty() && (getStack().getItem() instanceof ItemBackpack || getStack().getItem() instanceof ItemEnderBackpack))
			return false;
		return true;
	}
}