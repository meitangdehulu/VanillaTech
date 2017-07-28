package com.pengu.vanillatech.blocks.items;

import java.util.UUID;

import com.pengu.vanillatech.client.render.generic.RenderZapLayered;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

public class ItemBlockRedstoneTeleporter extends ItemBlock
{
	public ItemBlockRedstoneTeleporter(Block b)
	{
		super(b);
	}
	
	@Override
	public void onCreated(ItemStack stack, World worldIn, EntityPlayer playerIn)
	{
		if(!stack.hasTagCompound())
			stack.setTagCompound(new NBTTagCompound());
		NBTTagCompound nbt = stack.getTagCompound();
		if(!nbt.hasKey("UUIDMost") || !nbt.hasKey("UUIDLeast"))
			nbt.setUniqueId("UUID", UUID.randomUUID());
	}
}