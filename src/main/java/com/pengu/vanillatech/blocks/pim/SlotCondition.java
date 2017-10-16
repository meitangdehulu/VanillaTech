package com.pengu.vanillatech.blocks.pim;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.util.Constants.NBT;

import com.pengu.vanillatech.utils.AtomicTuple;

public class SlotCondition implements ITickable
{
	public final TilePlayerInventoryManager inv;
	
	public int ruleSlot;
	public EntityPlayerMP player;
	public MinecraftServer server;
	
	public List<AtomicTuple<NBTItemComparator, SlotRule>> rules = new ArrayList<>();
	
	public SlotCondition(TilePlayerInventoryManager inv, int ruleSlot)
	{
		this.inv = inv;
		this.ruleSlot = ruleSlot;
	}
	
	public SlotCondition(TilePlayerInventoryManager inv, NBTTagCompound nbt)
	{
		this.inv = inv;
		readNBT(nbt);
	}
	
	@Override
	public void update()
	{
		if(player == null)
			return;
		
		for(int i = 0; i < rules.size(); ++i)
		{
			AtomicTuple<NBTItemComparator, SlotRule> tuple = rules.get(i);
			if(tuple != null && tuple.getFirst() != null && tuple.getSecond() != null && NBTItemComparator.matches(tuple.getFirst(), player.inventory.getStackInSlot(ruleSlot)))
				tuple.getSecond().update();
		}
	}
	
	public NBTTagCompound writeNBT(NBTTagCompound nbt)
	{
		nbt.setInteger("RuleSlot", ruleSlot);
		
		NBTTagList list = new NBTTagList();
		
		for(int i = 0; i < rules.size(); ++i)
		{
			AtomicTuple<NBTItemComparator, SlotRule> tuple = rules.get(i);
			NBTTagCompound tag = new NBTTagCompound();
			tag.setTag("Comparator", tuple.getFirst().writeNBT(new NBTTagCompound()));
			tag.setTag("Rule", tuple.getSecond().writeToNBT(new NBTTagCompound()));
			list.appendTag(tag);
		}
		
		nbt.setTag("Rules", list);
		
		return nbt;
	}
	
	public void readNBT(NBTTagCompound nbt)
	{
		ruleSlot = nbt.getInteger("RuleSlot");
		
		rules.clear();
		
		NBTTagList list = nbt.getTagList("Rules", NBT.TAG_COMPOUND);
		for(int i = 0; i < list.tagCount(); ++i)
		{
			NBTTagCompound tag = list.getCompoundTagAt(i);
			NBTItemComparator comp = new NBTItemComparator(tag.getCompoundTag("Comparator"));
			SlotRule rule = new SlotRule(inv, tag.getCompoundTag("Rule"));
			rules.add(new AtomicTuple<NBTItemComparator, SlotRule>(comp, rule));
		}
	}
}