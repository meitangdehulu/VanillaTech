package com.pengu.vanillatech.blocks.pim;

import java.util.function.Predicate;

import com.pengu.hammercore.common.match.item.ItemMatchParams;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ITickable;

public class SlotRule implements ITickable
{
	public final TilePlayerInventoryManager inv;
	
	public int ruleSlot;
	public EntityPlayerMP player;
	public MinecraftServer server;
	
	/**
	 * Contains the policy according to the slot. <br>
	 * 0 - neutral (don't do anything with the slot) <br>
	 * 1 - extract (extract items from the slot) <br>
	 * 2 - insert (insert specific item into the slot)
	 */
	public int slotPolicy = 0;
	
	public ItemStack filterStack = ItemStack.EMPTY;
	public ItemMatchParams pars = new ItemMatchParams();
	
	public final Predicate<ItemStack> INSERT_MATCHER = stack -> filterStack.isEmpty() || (pars.useDamage ? stack.isItemEqual(filterStack) : stack.isItemEqualIgnoreDurability(filterStack));
	
	public SlotRule(TilePlayerInventoryManager inv, int ruleSlot)
	{
		this.inv = inv;
		this.ruleSlot = ruleSlot;
	}
	
	public SlotRule(TilePlayerInventoryManager inv, NBTTagCompound nbt)
    {
		this.inv = inv;
		readFromNBT(nbt);
    }
	
	public void update()
	{
		if(slotPolicy == 0 || player == null)
			return;
		
		InventoryPlayer pi = player.inventory;
		ItemStack ruleStack = pi.getStackInSlot(ruleSlot);
		
		if(slotPolicy == 1 && !ruleStack.isEmpty())
			if(filterStack.isEmpty() || (pars.useDamage ? ruleStack.isItemEqual(filterStack) : ruleStack.isItemEqualIgnoreDurability(filterStack)))
				inv.addInternItem(ruleStack, 1);
		if(slotPolicy == 2)
		{
			ItemStack ins = inv.getInternItem(INSERT_MATCHER);
			if(!ins.isEmpty())
				if(ruleStack.isEmpty())
				{
					pi.setInventorySlotContents(ruleSlot, ins.copy());
					pi.getStackInSlot(ruleSlot).setCount(1);
					ins.shrink(1);
				} else if(ruleStack.isItemEqual(ins) && ruleStack.getCount() < ruleStack.getMaxStackSize())
				{
					ruleStack.grow(1);
					ins.shrink(1);
				}
		}
	}
	
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		nbt.setInteger("Policy", slotPolicy);
		nbt.setInteger("RuleSlot", ruleSlot);
		nbt.setTag("Filter", filterStack.writeToNBT(new NBTTagCompound()));
		nbt.setBoolean("UseDamage", pars.useDamage);
		return nbt;
	}
	
	public SlotRule readFromNBT(NBTTagCompound nbt)
	{
		slotPolicy = nbt.getInteger("Policy");
		ruleSlot = nbt.getInteger("RuleSlot");
		filterStack = new ItemStack(nbt.getCompoundTag("Filter"));
		pars.useDamage = nbt.getBoolean("UseDamage");
		return this;
	}
}