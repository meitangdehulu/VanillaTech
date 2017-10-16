package com.pengu.vanillatech.blocks.pim;

import java.util.function.Predicate;

import com.pengu.hammercore.common.inventory.InventoryDummy;
import com.pengu.hammercore.net.utils.NetPropertyUUID;
import com.pengu.hammercore.tile.TileSyncableTickable;
import com.pengu.hammercore.tile.iTileDroppable;
import com.pengu.vanillatech.blocks.pim.gui.ContainerPlayerInventoryManager;
import com.pengu.vanillatech.blocks.pim.gui.GuiPlayerInventoryManager;
import com.pengu.vanillatech.utils.InventoryHelpers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class TilePlayerInventoryManager extends TileSyncableTickable implements iTileDroppable
{
	public final NetPropertyUUID placer;
	public final InventoryDummy inventory = new InventoryDummy(9);
	public final SlotMapping rules;
	
	{
		placer = new NetPropertyUUID(this);
	}
	
	public TilePlayerInventoryManager(InventoryPlayer player)
	{
		placer.set(player.player.getGameProfile().getId());
		rules = new SlotMapping(this, player);
	}
	
	public TilePlayerInventoryManager()
	{
		rules = new SlotMapping(this, null);
	}
	
	@Override
	public void tick()
	{
		if(world.isRemote)
			return;
		
		MinecraftServer server = null;
		if(world instanceof WorldServer)
			server = ((WorldServer) world).getMinecraftServer();
		if(server == null)
			server = FMLCommonHandler.instance().getMinecraftServerInstance();
		if(server == null)
			return;
		EntityPlayerMP player = server.getPlayerList().getPlayerByUUID(placer.get());
		if(player == null)
			return;
		
		rules.update(player, server);
		if(atTickRate(20))
			sync();
	}
	
	@Override
	public void writeNBT(NBTTagCompound nbt)
	{
		nbt.setTag("Items", inventory.writeToNBT(new NBTTagCompound()));
		nbt.setTag("Mappings", rules.writeNBT(new NBTTagCompound()));
	}
	
	@Override
	public void readNBT(NBTTagCompound nbt)
	{
		inventory.readFromNBT(nbt.getCompoundTag("Items"));
		rules.readNBT(nbt.getCompoundTag("Mappings"));
	}
	
	public void addInternItem(ItemStack stack, int maxAdd)
	{
		ItemStack ns = stack.copy();
		int constCount = Math.min(ns.getCount(), maxAdd);
		if(maxAdd < 1)
			constCount = ns.getCount();
		ns.setCount(constCount);
		int slot = InventoryHelpers.queryItemOrEmptySlot(inventory, stack, true, null);
		if(slot >= 0)
			InventoryHelpers.putItem(inventory, slot, stack);
		stack.shrink(constCount - ns.getCount());
	}
	
	public ItemStack getInternItem(Predicate<ItemStack> matcher)
	{
		for(int i = 0; i < inventory.getSizeInventory(); ++i)
			if(matcher.test(inventory.getStackInSlot(i)))
				return inventory.getStackInSlot(i);
		return ItemStack.EMPTY;
	}
	
	@Override
	public void createDrop(EntityPlayer player, World world, BlockPos pos)
	{
		inventory.drop(world, pos);
	}
	
	@Override
	public boolean hasGui()
	{
		return true;
	}
	
	@Override
	public Object getClientGuiElement(EntityPlayer player)
	{
		return new GuiPlayerInventoryManager(this, player);
	}
	
	@Override
	public Object getServerGuiElement(EntityPlayer player)
	{
		return new ContainerPlayerInventoryManager(this, player);
	}
}