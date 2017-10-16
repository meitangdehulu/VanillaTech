package com.pengu.vanillatech.blocks.pim;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.util.Constants.NBT;

public class SlotMapping
{
	public final TilePlayerInventoryManager mgr;
	private SlotCondition[] rules = new SlotCondition[41];
	
	public SlotMapping(TilePlayerInventoryManager mgr, InventoryPlayer player)
	{
		this.mgr = mgr;
		if(player == null)
			return;
		rules = new SlotCondition[player.getSizeInventory()];
		for(int i = 0; i < rules.length; ++i)
			setCondition(i, new SlotCondition(mgr, i));
	}
	
	public SlotCondition getCondition(int index)
	{
		SlotCondition cond = rules[index];
//		if(cond == null)
//			rules[index] = cond = new SlotCondition(mgr, index);
		return cond;
	}
	
	public void setCondition(int index, SlotCondition cond)
	{
		rules[index] = cond;
	}
	
	public void update(EntityPlayerMP player, MinecraftServer server)
	{
		for(int i = 0; i < rules.length; ++i)
		{
			SlotCondition rule = getCondition(i);
			rule.player = player;
			rule.server = server;
			rule.update();
		}
	}
	
	public NBTTagCompound writeNBT(NBTTagCompound nbt)
	{
		NBTTagList rs = new NBTTagList();
		for(int i = 0; i < rules.length; ++i)
		{
			NBTTagCompound tag = new NBTTagCompound();
			rules[i].writeNBT(tag);
			rs.appendTag(tag);
		}
		nbt.setTag("Rules", rs);
		return nbt;
	}
	
	public void readNBT(NBTTagCompound nbt)
	{
		NBTTagList rs = nbt.getTagList("Rules", NBT.TAG_COMPOUND);
		int ruleCount = rs.tagCount();
		
		rules = new SlotCondition[ruleCount];
		for(int i = 0; i < rs.tagCount(); ++i)
		{
			NBTTagCompound tag = rs.getCompoundTagAt(i);
			setCondition(i, new SlotCondition(mgr, tag));
		}
	}
}