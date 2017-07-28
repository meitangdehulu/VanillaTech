package com.pengu.vanillatech.net;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.pengu.hammercore.common.utils.WorldUtil;
import com.pengu.hammercore.net.packetAPI.IPacket;
import com.pengu.hammercore.net.packetAPI.IPacketListener;
import com.pengu.vanillatech.tile.TileFisher;
import com.pengu.vanillatech.utils.AtomicTuple;

public class PacketAddFisherItem implements IPacket, IPacketListener<PacketAddFisherItem, IPacket>
{
	private long pos;
	private ItemStack drop;
	
	public PacketAddFisherItem(TileFisher fisher, ItemStack stack)
	{
		pos = fisher.getPos().toLong();
		drop = stack.copy();
	}
	
	public PacketAddFisherItem()
	{
	}
	
	@Override
	public IPacket onArrived(PacketAddFisherItem packet, MessageContext context)
	{
		if(context.side == Side.CLIENT)
			packet.client();
		
		return null;
	}
	
	@SideOnly(Side.CLIENT)
	public void client()
	{
		TileFisher fisher = WorldUtil.cast(Minecraft.getMinecraft().world.getTileEntity(BlockPos.fromLong(pos)), TileFisher.class);
		if(fisher != null)
			fisher.pickedUp.add(new AtomicTuple<Integer, ItemStack>(0, drop));
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		pos = nbt.getLong("Pos");
		drop = new ItemStack(nbt);
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		drop.writeToNBT(nbt);
		nbt.setLong("Pos", pos);
	}
}