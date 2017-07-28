package com.pengu.vanillatech.net;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.pengu.hammercore.common.utils.XPUtil;
import com.pengu.hammercore.net.packetAPI.IPacket;
import com.pengu.hammercore.net.packetAPI.IPacketListener;

public class PacketSendXP implements IPacket, IPacketListener<PacketSendXP, IPacket>
{
	private int xp;
	
	public PacketSendXP(int xp)
	{
		this.xp = xp;
	}
	
	public PacketSendXP()
	{
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		nbt.setInteger("xp", xp);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		xp = nbt.getInteger("xp");
	}
	
	@Override
	public IPacket onArrived(PacketSendXP packet, MessageContext context)
	{
		if(context.side == Side.CLIENT)
			packet.client();
		if(context.side == Side.SERVER)
			return new PacketSendXP(XPUtil.getXPTotal(context.getServerHandler().player));
		return null;
	}
	
	@SideOnly(Side.CLIENT)
	public void client()
	{
		XPUtil.setPlayersExpTo(Minecraft.getMinecraft().player, xp);
	}
}