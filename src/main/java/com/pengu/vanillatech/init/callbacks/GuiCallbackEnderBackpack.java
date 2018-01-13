package com.pengu.vanillatech.init.callbacks;

import java.util.UUID;

import com.pengu.hammercore.core.gui.iGuiCallback;
import com.pengu.vanillatech.client.gui.inventory.GuiBackpack;
import com.pengu.vanillatech.inventory.ContainerBackpack;
import com.pengu.vanillatech.items.ItemEnderBackpack;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class GuiCallbackEnderBackpack implements iGuiCallback
{
	@Override
	public Object getServerGuiElement(EntityPlayer player, World world, BlockPos pos)
	{
		UUID backpack = null;
		
		if(!player.getHeldItemMainhand().isEmpty() && player.getHeldItemMainhand().getItem() instanceof ItemEnderBackpack && player.getHeldItemMainhand().hasTagCompound())
			backpack = player.getHeldItemMainhand().getTagCompound().getUniqueId("UUID");
		else if(!player.getHeldItemOffhand().isEmpty() && player.getHeldItemOffhand().getItem() instanceof ItemEnderBackpack && player.getHeldItemOffhand().hasTagCompound())
			backpack = player.getHeldItemOffhand().getTagCompound().getUniqueId("UUID");
		
		if(backpack != null)
			return new ContainerBackpack(player, backpack, true);
		
		return null;
	}
	
	@Override
	public Object getClientGuiElement(EntityPlayer player, World world, BlockPos pos)
	{
		UUID backpack = null;
		
		if(!player.getHeldItemMainhand().isEmpty() && player.getHeldItemMainhand().getItem() instanceof ItemEnderBackpack && player.getHeldItemMainhand().hasTagCompound())
			backpack = player.getHeldItemMainhand().getTagCompound().getUniqueId("UUID");
		else if(!player.getHeldItemOffhand().isEmpty() && player.getHeldItemOffhand().getItem() instanceof ItemEnderBackpack && player.getHeldItemOffhand().hasTagCompound())
			backpack = player.getHeldItemOffhand().getTagCompound().getUniqueId("UUID");
		
		if(backpack != null)
			return new GuiBackpack(backpack, true);
		
		return null;
	}
}