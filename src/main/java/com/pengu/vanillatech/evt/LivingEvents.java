package com.pengu.vanillatech.evt;

import java.util.Map;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import com.pengu.hammercore.HammerCore;
import com.pengu.hammercore.annotations.MCFBus;
import com.pengu.hammercore.common.utils.WorldUtil;
import com.pengu.vanillatech.init.ItemsVT;
import com.pengu.vanillatech.items.ItemShieldTotem;

@MCFBus
public class LivingEvents
{
	@SubscribeEvent
	public void shieldTotemLogic(LivingHurtEvent evt)
	{
		EntityLivingBase entity = evt.getEntityLiving();
		
		ItemStack shield = ItemStack.EMPTY;
		
		shield = entity.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND);
		
		if(!shield.isEmpty() && shield.getItem() != ItemsVT.SHIELD_TOTEM)
			shield = ItemStack.EMPTY;
		
		if(shield.isEmpty())
			shield = entity.getItemStackFromSlot(EntityEquipmentSlot.OFFHAND);
		
		if(!shield.isEmpty() && shield.getItem() != ItemsVT.SHIELD_TOTEM)
			shield = ItemStack.EMPTY;
		
		if(shield.isEmpty() && entity instanceof EntityPlayer)
			for(int i = 0; i < 9; ++i)
			{
				ItemStack slot = ((EntityPlayer) entity).inventory.getStackInSlot(i);
				if(!slot.isEmpty() && slot.getItem() == ItemsVT.SHIELD_TOTEM)
				{
					shield = slot;
					break;
				}
			}
		
		if(!shield.isEmpty())
		{
			int dmg = ItemShieldTotem.getTotemDamage(shield);
			int amt = MathHelper.ceil(evt.getAmount());
			int maxTake = Math.min(15_000 - dmg, amt);
			
			ItemShieldTotem.setTotemDamage(shield, dmg + maxTake);
			
			evt.setAmount(evt.getAmount() - maxTake);
			evt.setCanceled(true);
			if(!entity.world.isRemote)
				HammerCore.audioProxy.playSoundAt(entity.world, "ui.toast.in", entity.getPosition(), 2, 1, SoundCategory.NEUTRAL);
			try
			{
				entity.setLastAttackedEntity(evt.getSource().getTrueSource());
			} catch(Throwable err)
			{
			} // should be fine
		}
	}
	
	@SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = false)
	public void entityDie(LivingDeathEvent evt)
	{
		if(evt.getEntityLiving() instanceof EntityWither)
		{
			EntityWither wither = (EntityWither) evt.getEntityLiving();
			int shards = 4 + wither.world.rand.nextInt(5);
			DamageSource ds = evt.getSource();
			EntityLivingBase murderer = ds != null ? WorldUtil.cast(ds.getTrueSource(), EntityLivingBase.class) : null;
			if(murderer != null)
			{
				Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(murderer.getHeldItemMainhand());
				Integer looting = enchantments.get(Enchantments.LOOTING);
				if(looting != null && looting > 0)
					shards += wither.world.rand.nextInt(looting.intValue() + 1);
			}
			if(!wither.world.isRemote)
				wither.dropItem(ItemsVT.NETHERSTAR_SHARD, shards);
		}
	}
}