package com.pengu.vanillatech.items;

import java.util.List;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.pengu.hammercore.command.CommandTimeToTicks;

public class ItemShieldTotem extends Item
{
	public ItemShieldTotem()
	{
		setUnlocalizedName("shield_totem");
		setMaxDamage(4);
		setMaxStackSize(1);
	}
	
	@Override
	public boolean showDurabilityBar(ItemStack stack)
	{
		if(stack.hasTagCompound() && stack.getTagCompound().getInteger("Damage") > 0)
			return true;
		return false;
	}
	
	@Override
	public double getDurabilityForDisplay(ItemStack stack)
	{
		if(stack.hasTagCompound() && stack.getTagCompound().getInteger("Damage") > 0)
			return stack.getTagCompound().getInteger("Damage") / 15_000D;
		return 0;
	}
	
	public static int getTotemDamage(ItemStack stack)
	{
		if(stack.hasTagCompound() && stack.getTagCompound().getInteger("Damage") > 0)
			return stack.getTagCompound().getInteger("Damage");
		return 0;
	}
	
	public static void setTotemDamage(ItemStack stack, int dmg)
	{
		if(dmg == 15_000)
		{
			stack.setCount(0);
			return;
		}
		if(!stack.hasTagCompound())
			stack.setTagCompound(new NBTTagCompound());
		stack.getTagCompound().setInteger("Damage", dmg);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn)
	{
		if(flagIn.isAdvanced())
			tooltip.add("Damage: " + CommandTimeToTicks.fancyFormat(15000L - getTotemDamage(stack)) + " / " + CommandTimeToTicks.fancyFormat(15000));
	}
	
	@Override
	public EnumRarity getRarity(ItemStack stack)
	{
		return EnumRarity.EPIC;
	}
	
	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items)
	{
		if(isInCreativeTab(tab))
			items.add(new ItemStack(this));
	}
	
	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
	{
		boolean isHotbarOrOffhand = isSelected || (itemSlot >= 0 && itemSlot < 9);
		
		if(worldIn.isRemote)
			return;
		
		if(isHotbarOrOffhand)
		{
			boolean shouldAnimate = false;
			
			if(entityIn instanceof EntityLivingBase)
			{
				EntityLivingBase e = (EntityLivingBase) entityIn;
				shouldAnimate = Math.round(e.getHealth()) < e.getMaxHealth();
				if(!shouldAnimate)
					shouldAnimate = e.ticksExisted - e.getLastAttackedEntityTime() < 40;
			}
			
			if(entityIn.ticksExisted % 5 == 0)
			{
				if(shouldAnimate)
				{
					if(stack.getItemDamage() < 4)
						stack.setItemDamage(stack.getItemDamage() + 1);
					else
						stack.setItemDamage(stack.getItemDamage() - 1);
				} else if(stack.getItemDamage() > 0)
					stack.setItemDamage(stack.getItemDamage() - 1);
			}
		} else if(stack.getItemDamage() > 0 && entityIn.ticksExisted % 5 == 0)
			stack.setItemDamage(stack.getItemDamage() - 1);
	}
}