package com.pengu.vanillatech.net;

import com.pengu.hammercore.net.packetAPI.iPacket;
import com.pengu.hammercore.net.packetAPI.iPacketListener;
import com.pengu.hammercore.proxy.ParticleProxy_Client;
import com.pengu.vanillatech.client.particle.ParticleExplosion;
import com.pengu.vanillatech.client.render.item.RenderEnhancedPickaxe;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PacketExplosion implements iPacket, iPacketListener<PacketExplosion, iPacket>
{
	public float x, y, z;
	
	@Override
	public iPacket onArrived(PacketExplosion packet, MessageContext context)
	{
		if(context.side == Side.CLIENT)
			packet.client();
		return null;
	}
	
	@SideOnly(Side.CLIENT)
	public void client()
	{
		ParticleProxy_Client.queueParticleSpawn(new ParticleExplosion(Minecraft.getMinecraft().world, x, y, z));
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		nbt.setFloat("x", x);
		nbt.setFloat("y", y);
		nbt.setFloat("z", z);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		x = nbt.getFloat("x");
		y = nbt.getFloat("y");
		z = nbt.getFloat("z");
	}
}