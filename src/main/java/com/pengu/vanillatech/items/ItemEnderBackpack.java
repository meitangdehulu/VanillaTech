package com.pengu.vanillatech.items;

import java.util.UUID;

import com.pengu.hammercore.core.gui.GuiManager;
import com.pengu.hammercore.net.HCNetwork;
import com.pengu.vanillatech.init.GuiCallbacksVT;
import com.pengu.vanillatech.inventory.ContainerBackpack;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class ItemEnderBackpack extends Item
{
	public ItemEnderBackpack()
	{
		setUnlocalizedName("ender_backpack");
		setMaxStackSize(1);
		addPropertyOverride(new ResourceLocation("open"), (stack, world, entityIn) ->
		{
			if(entityIn instanceof EntityPlayer && ((EntityPlayer) entityIn).openContainer instanceof ContainerBackpack)
			{
				ContainerBackpack back = (ContainerBackpack) ((EntityPlayer) entityIn).openContainer;
				UUID our = stack.getTagCompound().getUniqueId("UUID");
				if(our.equals(back.backpack))
					return 1;
			}
			return 0;
		});
	}
	
	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
	{
		if(!stack.hasTagCompound())
		{
			stack.setTagCompound(new NBTTagCompound());
			stack.getTagCompound().setUniqueId("UUID", UUID.randomUUID());
		}
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
	{
		GuiManager.openGuiCallback(GuiCallbacksVT.ENDER_BACKPACK.getGuiID(), playerIn, worldIn, playerIn.getPosition());
		HCNetwork.swingArm(playerIn, handIn);
		return new ActionResult<ItemStack>(EnumActionResult.PASS, playerIn.getHeldItem(handIn));
	}
}