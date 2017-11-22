package com.pengu.vanillatech.items;

import java.util.List;

import org.lwjgl.input.Keyboard;

import com.mojang.realmsclient.gui.ChatFormatting;
import com.pengu.hammercore.HammerCore;
import com.pengu.hammercore.api.iProcess;
import com.pengu.hammercore.common.utils.SoundUtil;
import com.pengu.hammercore.common.utils.WorldUtil;
import com.pengu.vanillatech.init.ItemMaterialsVT;
import com.pengu.vanillatech.utils.ItemUtils;
import com.pengu.vanillatech.utils.RomanNumber;
import com.pengu.vanillatech.utils.TextUtils;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemEnhancedPickaxe extends ItemPickaxe
{
	public ItemEnhancedPickaxe()
	{
		super(ItemMaterialsVT.TOOL_ENHANCED);
		setUnlocalizedName("enhanced_pickaxe");
	}
	
	public static int getRank(ItemStack stack)
	{
		return stack.hasTagCompound() ? stack.getTagCompound().getInteger("Rank") : 0;
	}
	
	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
	{
		NBTTagCompound nbt = stack.getTagCompound();
		if(nbt == null)
			stack.setTagCompound(nbt = new NBTTagCompound());
		int heat = nbt.getInteger("Heat");
		int crank = nbt.getInteger("Rank");
		int req = 0;
		if(crank == 0)
			req = 16;
		if(crank == 1)
			req = 32;
		if(crank == 2)
			req = 64;
		if(crank == 3)
			req = 128;
		if(crank == 4)
			req = 192;
		if(crank == 5)
			req = 256;
		if(crank == 6)
			req = 512;
		if(heat > 0)
		{
			nbt.setInteger("Heat", heat - 1);
			EntityPlayer player = WorldUtil.cast(entityIn, EntityPlayer.class);
			if(player != null && heat - 1 <= 0 && crank < 7)
				player.sendStatusMessage(new TextComponentString(ChatFormatting.RED + ChatFormatting.BOLD.toString() + "Progress reset!"), true);
			int add = nbt.getInteger("Add");
			
			if(add >= req && crank < 7)
			{
				nbt.setInteger("Add", 0);
				nbt.setInteger("Heat", 100);
				nbt.setInteger("Rank", crank + 1);
				
				iProcess proc = new iProcess()
				{
					public int ticks = 0;
					public float pitch = 0;
					
					@Override
					public void update()
					{
						if(ticks % (int) (20 - pitch * 4) == 0)
						{
							pitch += .25F;
							SoundUtil.playSoundEffect(worldIn, "minecraft:entity.elder_guardian.curse", player.getPosition(), 3F, pitch, SoundCategory.PLAYERS);
						}
						
						++ticks;
					}
					
					@Override
					public boolean isAlive()
					{
						return pitch < 2;
					}
				};
				
				proc.start();
			}
		} else
			nbt.setInteger("Add", 0);
		
		EntityPlayer player = WorldUtil.cast(entityIn, EntityPlayer.class);
		
		if(player != null && nbt.getInteger("Heat") > 0 && crank < 7)
			player.sendStatusMessage(new TextComponentString(ChatFormatting.GREEN + ChatFormatting.BOLD.toString() + "\u2191 " + nbt.getInteger("Add") + " / " + req), true);
	}
	
	@Override
	public boolean shouldCauseBlockBreakReset(ItemStack oldStack, ItemStack newStack)
	{
		if(ItemUtils.tagChanged(oldStack, newStack, "Heat"))
			return false;
		if(ItemUtils.tagChanged(oldStack, newStack, "Add"))
			return false;
		return super.shouldCauseBlockBreakReset(oldStack, newStack);
	}
	
	public static int calcTreasureChance(ItemStack stack, EntityPlayer player)
	{
		int r = getRank(stack);
		int base = r == 5 ? 1 : r == 6 ? 2 : r == 7 ? 5 : 0;
		if(base == 0)
			return 0;
		
		int fortune = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, stack);
		PotionEffect luck = player.getActivePotionEffect(MobEffects.LUCK);
		PotionEffect unluck = player.getActivePotionEffect(MobEffects.UNLUCK);
		
		base += luck != null ? luck.getAmplifier() + 1 : 0;
		base += fortune * 2;
		base -= unluck != null ? (unluck.getAmplifier() + 1) * 2 : 0;
		
		return base;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn)
	{
		int r = getRank(stack);
		
		String rank = I18n.format("vanillatech:ranks." + r);
		
		if(r == 0)
			rank = TextFormatting.AQUA + rank;
		
		if(r == 1)
			rank = TextFormatting.DARK_PURPLE + rank;
		
		if(r == 2)
			rank = TextFormatting.LIGHT_PURPLE + rank;
		
		if(r == 3)
			rank = TextFormatting.DARK_GREEN + rank;
		
		if(r == 4)
			rank = TextFormatting.GREEN + rank;
		
		if(r == 5)
			rank = TextFormatting.BLUE + rank;
		
		if(r == 6)
			rank = TextFormatting.YELLOW + rank;
		
		if(r == 7)
			rank = TextFormatting.GOLD + rank;
		
		tooltip.add("Rank: " + rank);
		
		if(r > 0)
		{
			tooltip.add("Perks:");
			tooltip.add(" -Efficiency " + RomanNumber.toRoman(r) + " " + TextUtils.SHIFTFORDETAILS());
			boolean shift = TextUtils.isShift();
			if(shift)
				tooltip.add("  -Mine Speed Increased by " + (int) (r / 8F * 100F) + "%");
			int trch = calcTreasureChance(stack, Minecraft.getMinecraft().player);
			if(trch > 0)
			{
				tooltip.add(" -Treasure Sniffing " + TextUtils.SHIFTFORDETAILS());
				if(shift)
					tooltip.add("  -Adds " + trch + "% chance to find a treasure while mining.");
			}
		}
	}
	
	@Override
	public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged)
	{
		return slotChanged;
	}
}