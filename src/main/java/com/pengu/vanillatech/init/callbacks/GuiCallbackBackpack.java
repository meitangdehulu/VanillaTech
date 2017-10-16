package com.pengu.vanillatech.init.callbacks;

import java.util.UUID;

import com.pengu.hammercore.core.gui.iGuiCallback;
import com.pengu.vanillatech.client.gui.inventory.GuiBackpack;
import com.pengu.vanillatech.inventory.ContainerBackpack;
import com.pengu.vanillatech.items.ItemBackpack;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class GuiCallbackBackpack implements iGuiCallback
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
		UUID backpack = null;
		
		if(!player.getHeldItemMainhand().isEmpty() && player.getHeldItemMainhand().getItem() instanceof ItemBackpack && player.getHeldItemMainhand().hasTagCompound())
			backpack = player.getHeldItemMainhand().getTagCompound().getUniqueId("UUID");
		else if(!player.getHeldItemOffhand().isEmpty() && player.getHeldItemOffhand().getItem() instanceof ItemBackpack && player.getHeldItemOffhand().hasTagCompound())
			backpack = player.getHeldItemOffhand().getTagCompound().getUniqueId("UUID");
		
		if(backpack != null)
			return new ContainerBackpack(player, backpack);
		
		return null;
	}
	
	@Override
	public Object getClientGuiElement(EntityPlayer player, World world, BlockPos pos)
	{
		UUID backpack = null;
		
		if(!player.getHeldItemMainhand().isEmpty() && player.getHeldItemMainhand().getItem() instanceof ItemBackpack && player.getHeldItemMainhand().hasTagCompound())
			backpack = player.getHeldItemMainhand().getTagCompound().getUniqueId("UUID");
		else if(!player.getHeldItemOffhand().isEmpty() && player.getHeldItemOffhand().getItem() instanceof ItemBackpack && player.getHeldItemOffhand().hasTagCompound())
			backpack = player.getHeldItemOffhand().getTagCompound().getUniqueId("UUID");
		
		if(backpack != null)
			return new GuiBackpack(backpack);
		
		return null;
	}
}