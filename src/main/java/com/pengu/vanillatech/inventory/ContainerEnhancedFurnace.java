package com.pengu.vanillatech.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnaceFuel;
import net.minecraft.inventory.SlotFurnaceOutput;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.pengu.vanillatech.tile.TileEnhancedFurnace;

public class ContainerEnhancedFurnace extends Container
{
	private final TileEnhancedFurnace tileFurnace;
	private int cookTime;
	private int totalCookTime;
	private int furnaceBurnTime;
	private int currentItemBurnTime;
	
	public ContainerEnhancedFurnace(InventoryPlayer playerInventory, TileEnhancedFurnace furnaceInventory)
	{
		tileFurnace = furnaceInventory;
		addSlotToContainer(new Slot(furnaceInventory, 0, 56, 17));
		addSlotToContainer(new SlotFurnaceFuel(furnaceInventory, 1, 56, 53));
		addSlotToContainer(new SlotFurnaceOutput(playerInventory.player, furnaceInventory, 2, 116, 35));
		
		for(int i = 0; i < 3; ++i)
			for(int j = 0; j < 9; ++j)
				addSlotToContainer(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
		
		for(int k = 0; k < 9; ++k)
			addSlotToContainer(new Slot(playerInventory, k, 8 + k * 18, 142));
	}
	
	public void addListener(IContainerListener listener)
	{
		super.addListener(listener);
		listener.sendAllWindowProperties(this, tileFurnace);
	}
	
	public void detectAndSendChanges()
	{
		super.detectAndSendChanges();
		cookTime = tileFurnace.cookTime;
		furnaceBurnTime = tileFurnace.furnaceBurnTime;
		currentItemBurnTime = tileFurnace.currentItemBurnTime;
		totalCookTime = tileFurnace.totalCookTime;
	}
	
	@SideOnly(Side.CLIENT)
	public void updateProgressBar(int id, int data)
	{
	}
	
	public boolean canInteractWith(EntityPlayer playerIn)
	{
		return tileFurnace.isUsableByPlayer(playerIn);
	}
	
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
	{
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = inventorySlots.get(index);
		
		if(slot != null && slot.getHasStack())
		{
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();
			
			if(index == 2)
			{
				if(!mergeItemStack(itemstack1, 3, 39, true))
				{
					return ItemStack.EMPTY;
				}
				
				slot.onSlotChange(itemstack1, itemstack);
			} else if(index != 1 && index != 0)
			{
				if(!FurnaceRecipes.instance().getSmeltingResult(itemstack1).isEmpty())
				{
					if(!mergeItemStack(itemstack1, 0, 1, false))
						return ItemStack.EMPTY;
				} else if(TileEntityFurnace.isItemFuel(itemstack1))
				{
					if(!mergeItemStack(itemstack1, 1, 2, false))
						return ItemStack.EMPTY;
				} else if(index >= 3 && index < 30)
				{
					if(!mergeItemStack(itemstack1, 30, 39, false))
						return ItemStack.EMPTY;
				} else if(index >= 30 && index < 39 && !mergeItemStack(itemstack1, 3, 30, false))
					return ItemStack.EMPTY;
			} else if(!mergeItemStack(itemstack1, 3, 39, false))
				return ItemStack.EMPTY;
			if(itemstack1.isEmpty())
				slot.putStack(ItemStack.EMPTY);
			else
				slot.onSlotChanged();
			if(itemstack1.getCount() == itemstack.getCount())
				return ItemStack.EMPTY;
			slot.onTake(playerIn, itemstack1);
		}
		
		return itemstack;
	}
}