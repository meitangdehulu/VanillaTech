package com.pengu.vanillatech.api.tile;

import java.util.List;

import net.minecraft.inventory.IInventory;

public interface IInventoryNode
{
	void getInventories(List<IInventory> inventories);
}