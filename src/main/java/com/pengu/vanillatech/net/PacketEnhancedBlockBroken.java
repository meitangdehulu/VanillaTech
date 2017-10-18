package com.pengu.vanillatech.net;

import com.pengu.hammercore.net.packetAPI.iPacket;
import com.pengu.hammercore.net.packetAPI.iPacketListener;
import com.pengu.vanillatech.client.render.item.RenderEnhancedPickaxe;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PacketEnhancedBlockBroken implements iPacket, iPacketListener<PacketEnhancedBlockBroken, iPacket>
{
	@Override
	public iPacket onArrived(PacketEnhancedBlockBroken packet, MessageContext context)
	{
		if(context.side == Side.CLIENT)
			client();
		return null;
	}
	
	@SideOnly(Side.CLIENT)
	public void client()
	{
		RenderEnhancedPickaxe.targetSize = 20;
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
	}
}