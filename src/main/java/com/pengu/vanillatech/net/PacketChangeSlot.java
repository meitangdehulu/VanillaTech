package com.pengu.vanillatech.net;

import com.pengu.hammercore.common.utils.WorldUtil;
import com.pengu.hammercore.net.packetAPI.iPacket;
import com.pengu.hammercore.net.packetAPI.iPacketListener;
import com.pengu.vanillatech.blocks.pim.SlotCondition;
import com.pengu.vanillatech.blocks.pim.TilePlayerInventoryManager;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class PacketChangeSlot implements iPacket, iPacketListener<PacketChangeSlot, iPacket>
{
	public int dim, slot;
	public long pos;
	public NBTTagCompound nbt;
	
	public PacketChangeSlot(TilePlayerInventoryManager mgr, SlotCondition slot)
	{
		this.slot = slot.ruleSlot;
		this.dim = mgr.getWorld().provider.getDimension();
		this.pos = mgr.getPos().toLong();
		nbt = slot.writeNBT(new NBTTagCompound());
	}
	
	public PacketChangeSlot()
	{
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		nbt.setInteger("Dim", dim);
		nbt.setInteger("Slot", slot);
		nbt.setLong("Pos", pos);
		nbt.setTag("Data", this.nbt);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		dim = nbt.getInteger("Dim");
		slot = nbt.getInteger("Slot");
		pos = nbt.getLong("Pos");
		this.nbt = nbt.getCompoundTag("Data");
	}
	
	@Override
	public iPacket onArrived(PacketChangeSlot packet, MessageContext context)
	{
		if(context.side == Side.SERVER)
		{
			WorldServer world = context.getServerHandler().player.mcServer.getWorld(packet.dim);
			TilePlayerInventoryManager mgr = WorldUtil.cast(world.getTileEntity(BlockPos.fromLong(packet.pos)), TilePlayerInventoryManager.class);
			if(mgr != null)
			{
				mgr.rules.setCondition(packet.slot, new SlotCondition(mgr, packet.nbt));
				mgr.sync();
			}
		}
		
		return null;
	}
}