package com.pengu.vanillatech.enchantments;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import com.pengu.hammercore.common.utils.XPUtil;
import com.pengu.hammercore.net.HCNetwork;
import com.pengu.vanillatech.init.EnchantmentsVT;
import com.pengu.vanillatech.net.PacketSendXP;

public class EnchantmentRepair extends Enchantment
{
	public EnchantmentRepair()
	{
		super(Rarity.RARE, EnumEnchantmentType.BREAKABLE, EntityEquipmentSlot.values());
		setName("repair");
	}
	
	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack)
	{
		return super.canApplyAtEnchantingTable(stack) && canApply(stack);
	}
	
	@Override
	public boolean canApply(ItemStack stack)
	{
		return stack.getItem().isDamageable();
	}
	
	@Override
	protected boolean canApplyTogether(Enchantment ench)
	{
		return ench != Enchantments.MENDING && ench != this;
	}
	
	@Override
	public int getMaxLevel()
	{
		return 5;
	}
	
	@SubscribeEvent
	public void livingTick(LivingUpdateEvent e)
	{
		EntityLivingBase ent = e.getEntityLiving();
		if(ent != null && ent.world.isRemote)
			return;
		if(ent instanceof EntityPlayerMP)
		{
			EntityPlayerMP player = (EntityPlayerMP) ent;
			InventoryPlayer inv = player.inventory;
			for(int i = 0; i < inv.getSizeInventory(); ++i)
			{
				ItemStack stack = inv.getStackInSlot(i);
				int repair = EnchantmentHelper.getEnchantmentLevel(EnchantmentsVT.REPAIR, stack);
				if(repair <= 0)
					continue;
				int maxRepair = EnchantmentsVT.REPAIR.getMaxLevel();
				int delta = (maxRepair - repair + 2) * 20;
				if(stack.getItemDamage() > 0 && player.ticksExisted % delta == 0)
				{
					int xp = XPUtil.getXPTotal(player);
					int maxDmg = (int) Math.ceil(stack.getMaxDamage() / 1000D) + 1;
					if(xp >= maxDmg)
					{
						stack.setItemDamage(stack.getItemDamage() - 1);
						xp -= maxDmg;
						XPUtil.setPlayersExpTo(player, xp);
						HCNetwork.manager.sendTo(new PacketSendXP(xp), player);
					}
				}
			}
		}
	}
}