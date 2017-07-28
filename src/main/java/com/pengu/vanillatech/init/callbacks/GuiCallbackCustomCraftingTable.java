package com.pengu.vanillatech.init.callbacks;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import com.pengu.hammercore.gui.IGuiCallback;
import com.pengu.vanillatech.client.gui.inventory.GuiCustomWorkbench;
import com.pengu.vanillatech.inventory.ContainerCustomWorkbench;

public class GuiCallbackCustomCraftingTable implements IGuiCallback
{
	int id;
	
	@Override
	public void setGuiID(int id)
	{
		this.id = id;
	}
	
	@Override
	public int getGuiID()
	{
		return id;
	}
	
	@Override
	public Object getServerGuiElement(EntityPlayer player, World world, BlockPos pos)
	{
		return new ContainerCustomWorkbench(player.inventory, world, pos);
	}
	
	@Override
	public Object getClientGuiElement(EntityPlayer player, World world, BlockPos pos)
	{
		return new GuiCustomWorkbench(player.inventory, world, pos);
	}
}