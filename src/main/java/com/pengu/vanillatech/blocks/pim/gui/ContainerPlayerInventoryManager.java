package com.pengu.vanillatech.blocks.pim.gui;

import javax.annotation.Nullable;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.pengu.vanillatech.blocks.pim.TilePlayerInventoryManager;

public class ContainerPlayerInventoryManager extends Container
{
	private static final EntityEquipmentSlot[] VALID_EQUIPMENT_SLOTS = new EntityEquipmentSlot[] { EntityEquipmentSlot.HEAD, EntityEquipmentSlot.CHEST, EntityEquipmentSlot.LEGS, EntityEquipmentSlot.FEET };
	public final TilePlayerInventoryManager inv;
	
	public ContainerPlayerInventoryManager(TilePlayerInventoryManager inv, EntityPlayer player)
	{
		this.inv = inv;
		
		for(int i1 = 0; i1 < 9; ++i1)
			addSlotToContainer(new Slot(inv.inventory, i1, 8 + i1 * 18, 18));
		
		for(int k = 0; k < 4; ++k)
		{
			final EntityEquipmentSlot entityequipmentslot = VALID_EQUIPMENT_SLOTS[k];
			addSlotToContainer(new Slot(player.inventory, 36 + (3 - k), 172, 32 + k * 18)
			{
				public int getSlotStackLimit()
				{
					return 1;
				}
				
				public boolean isItemValid(ItemStack stack)
				{
					return stack.getItem().isValidArmor(stack, entityequipmentslot, player);
				}
				
				public boolean canTakeStack(EntityPlayer playerIn)
				{
					ItemStack itemstack = this.getStack();
					return !itemstack.isEmpty() && !playerIn.isCreative() && EnchantmentHelper.hasBindingCurse(itemstack) ? false : super.canTakeStack(playerIn);
				}
				
				@Nullable
				@SideOnly(Side.CLIENT)
				public String getSlotTexture()
				{
					return ItemArmor.EMPTY_SLOT_NAMES[entityequipmentslot.getIndex()];
				}
			});
		}
		
		addSlotToContainer(new Slot(player.inventory, 40, 172, 108)
		{
			@Nullable
			@SideOnly(Side.CLIENT)
			public String getSlotTexture()
			{
				return "minecraft:items/empty_armor_slot_shield";
			}
		});
		
		for(int i1 = 0; i1 < 9; ++i1)
			addSlotToContainer(new Slot(player.inventory, i1, 8 + i1 * 18, 108));
		
		for(int l = 0; l < 3; ++l)
			for(int j1 = 0; j1 < 9; ++j1)
				addSlotToContainer(new Slot(player.inventory, j1 + l * 9 + 9, 8 + j1 * 18, 50 + l * 18));
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer playerIn)
	{
		return inv.inventory.isUsableByPlayer(playerIn, inv.getPos());
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
	{
		return ItemStack.EMPTY;
	}
}