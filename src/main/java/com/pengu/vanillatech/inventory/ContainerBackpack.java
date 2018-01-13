package com.pengu.vanillatech.inventory;

import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.common.util.Constants.NBT;

import com.pengu.hammercore.HammerCore;
import com.pengu.hammercore.common.inventory.InventoryNonTile;
import com.pengu.hammercore.net.HCNetwork;
import com.pengu.vanillatech.inventory.slot.SlotNotTakeableBackpack;
import com.pengu.vanillatech.items.ItemBackpack;
import com.pengu.vanillatech.items.ItemEnderBackpack;

public class ContainerBackpack extends Container
{
	public final UUID backpack;
	public final ItemStack packStack;
	public final InventoryNonTile inv = new InventoryNonTile(54);
	public final EnumHand hand;
	
	public boolean ender;
	
	public ContainerBackpack(EntityPlayer player, UUID backpack, boolean ender)
	{
		this.backpack = backpack;
		this.ender = ender;
		if(!player.world.isRemote)
			HammerCore.audioProxy.playSoundAt(player.world, ender ? "block.enderchest.open" : "ui.toast.in", player.getPosition(), 1, 1, SoundCategory.PLAYERS);
		
		if(!player.getHeldItemMainhand().isEmpty() && (player.getHeldItemMainhand().getItem() instanceof ItemBackpack || player.getHeldItemMainhand().getItem() instanceof ItemEnderBackpack) && player.getHeldItemMainhand().hasTagCompound() && backpack.equals(player.getHeldItemMainhand().getTagCompound().getUniqueId("UUID")))
		{
			packStack = player.getHeldItemMainhand();
			hand = EnumHand.MAIN_HAND;
		} else if(!player.getHeldItemOffhand().isEmpty() && (player.getHeldItemOffhand().getItem() instanceof ItemBackpack || player.getHeldItemOffhand().getItem() instanceof ItemEnderBackpack) && player.getHeldItemOffhand().hasTagCompound() && backpack.equals(player.getHeldItemOffhand().getTagCompound().getUniqueId("UUID")))
		{
			packStack = player.getHeldItemOffhand();
			hand = EnumHand.OFF_HAND;
		} else
		{
			packStack = ItemStack.EMPTY;
			hand = null;
		}
		
		if(!ender)
		{
			if(!packStack.isEmpty())
			{
				if(packStack.getTagCompound().hasKey("Items", NBT.TAG_COMPOUND))
					inv.readFromNBT(packStack.getTagCompound().getCompoundTag("Items"));
				
				for(int j = 0; j < 6; ++j)
					for(int k = 0; k < 9; ++k)
						this.addSlotToContainer(new SlotNotTakeableBackpack(inv, k + j * 9, 8 + k * 18, 18 + j * 18));
			}
			
			for(int l = 0; l < 3; ++l)
				for(int j1 = 0; j1 < 9; ++j1)
					this.addSlotToContainer(new SlotNotTakeableBackpack(player.inventory, j1 + l * 9 + 9, 8 + j1 * 18, 103 + l * 18 + 36));
				
			for(int i1 = 0; i1 < 9; ++i1)
				addSlotToContainer(new SlotNotTakeableBackpack(player.inventory, i1, 8 + i1 * 18, 161 + 36));
		} else
		{
			for(int j = 0; j < 3; ++j)
				for(int k = 0; k < 9; ++k)
					this.addSlotToContainer(new SlotNotTakeableBackpack(player.getInventoryEnderChest(), k + j * 9, 8 + k * 18, 18 + j * 18));
				
			for(int l = 0; l < 3; ++l)
				for(int j1 = 0; j1 < 9; ++j1)
					this.addSlotToContainer(new SlotNotTakeableBackpack(player.inventory, j1 + l * 9 + 9, 8 + j1 * 18, 84 + l * 18 + 36));
				
			for(int i1 = 0; i1 < 9; ++i1)
				addSlotToContainer(new SlotNotTakeableBackpack(player.inventory, i1, 8 + i1 * 18, 142));
		}
	}
	
	@Override
	public void onContainerClosed(EntityPlayer playerIn)
	{
		if(!playerIn.world.isRemote)
		{
			HammerCore.audioProxy.playSoundAt(playerIn.world, ender ? "block.enderchest.close" : "ui.toast.out", playerIn.getPosition(), 1, 1, SoundCategory.PLAYERS);
			
			if(!packStack.isEmpty())
			{
				if(!packStack.hasTagCompound())
					packStack.setTagCompound(new NBTTagCompound());
				NBTTagCompound tag = new NBTTagCompound();
				inv.writeToNBT(tag);
				packStack.getTagCompound().setTag("Items", tag);
			}
			if(hand != null)
				HCNetwork.swingArm(playerIn, hand);
		}
		super.onContainerClosed(playerIn);
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
	{
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.inventorySlots.get(index);
		
		if(slot != null && slot.getHasStack())
		{
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();
			
			int s = ender ? 27 : 54;
			
			if(index < s)
			{
				if(!this.mergeItemStack(itemstack1, s, inventorySlots.size(), true))
					return ItemStack.EMPTY;
			} else if(!mergeItemStack(itemstack1, 0, s, false))
				return ItemStack.EMPTY;
			
			if(itemstack1.isEmpty())
				slot.putStack(ItemStack.EMPTY);
			else
				slot.onSlotChanged();
		}
		
		return itemstack;
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer playerIn)
	{
		if(ender && packStack.getItem() instanceof ItemEnderBackpack)
			return true;
		return !packStack.isEmpty() && ((!playerIn.getHeldItemMainhand().isEmpty() && playerIn.getHeldItemMainhand().getItem() instanceof ItemBackpack && playerIn.getHeldItemMainhand().hasTagCompound() && backpack.equals(playerIn.getHeldItemMainhand().getTagCompound().getUniqueId("UUID"))) || (!playerIn.getHeldItemOffhand().isEmpty() && playerIn.getHeldItemOffhand().getItem() instanceof ItemBackpack && playerIn.getHeldItemOffhand().hasTagCompound() && backpack.equals(playerIn.getHeldItemOffhand().getTagCompound().getUniqueId("UUID"))));
	}
}