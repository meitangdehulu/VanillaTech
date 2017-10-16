package com.pengu.vanillatech.init.callbacks;

import com.pengu.hammercore.core.gui.iGuiCallback;
import com.pengu.vanillatech.client.gui.inventory.GuiCustomWorkbench;
import com.pengu.vanillatech.inventory.ContainerCustomWorkbench;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class GuiCallbackCustomCraftingTable implements iGuiCallback
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