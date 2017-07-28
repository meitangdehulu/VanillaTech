package com.pengu.vanillatech;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.furnace.FurnaceFuelBurnTimeEvent;
import net.minecraftforge.fml.common.IFuelHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import com.pengu.hammercore.annotations.MCFBus;
import com.pengu.hammercore.common.utils.WorldUtil;
import com.pengu.vanillatech.blocks.IBurnableBlock;

@MCFBus
public class FuelHandler implements IFuelHandler
{
	@Override
	public int getBurnTime(ItemStack stack)
	{
		IBurnableBlock burnable = WorldUtil.cast(Block.getBlockFromItem(stack.getItem()), IBurnableBlock.class);
		if(burnable != null)
			return burnable.getBurnTime(stack);
		if(stack.getItem() == Items.WHEAT)
			return 200;
		if(stack.getItem() == Items.MAGMA_CREAM)
			return 3200;
		if(stack.getItem() == Item.getItemFromBlock(Blocks.MAGMA))
			return 800;
		if(stack.getItem() == Item.getItemFromBlock(Blocks.HAY_BLOCK))
			return 2000;
		return 0;
	}
	
	@SubscribeEvent
	public void $_getBurnTime(FurnaceFuelBurnTimeEvent evt)
	{
		int b = getBurnTime(evt.getItemStack());
		if(b > 0)
			evt.setBurnTime(b);
	}
}