package com.pengu.vanillatech.evt;

import java.util.List;
import java.util.Map;

import com.pengu.hammercore.HammerCore;
import com.pengu.hammercore.annotations.MCFBus;
import com.pengu.hammercore.common.InterItemStack;
import com.pengu.hammercore.common.utils.WorldUtil;
import com.pengu.hammercore.net.HCNetwork;
import com.pengu.hammercore.utils.WorldLocation;
import com.pengu.vanillatech.api.EnchancedPickaxeTreasures;
import com.pengu.vanillatech.api.EnchancedPickaxeTreasures.Drop;
import com.pengu.vanillatech.blocks.items.ItemBlockEnhancedFurnace;
import com.pengu.vanillatech.init.ItemsVT;
import com.pengu.vanillatech.items.ItemEnhancedPickaxe;
import com.pengu.vanillatech.items.ItemShieldTotem;
import com.pengu.vanillatech.net.PacketEnhancedBlockBroken;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.WeightedRandom;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

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
			}
		}
	}
	
	@SubscribeEvent
	public void breakBlock(BlockEvent.BreakEvent e)
	{
		if(e.getWorld().isRemote)
			return;
		ItemStack stack = e.getPlayer().getHeldItemMainhand();
		if(!stack.isEmpty() && stack.getItem() instanceof ItemEnhancedPickaxe)
		{
			NBTTagCompound nbt = stack.getTagCompound();
			if(nbt == null)
				stack.setTagCompound(nbt = new NBTTagCompound());
			if(e.getState().getBlockHardness(e.getWorld(), e.getPos()) > .5F)
			{
				nbt.setInteger("Heat", Math.max(20, nbt.getInteger("Heat")));
				nbt.setInteger("Add", nbt.getInteger("Add") + 1);
			}
			HCNetwork.manager.sendToAllAround(new PacketEnhancedBlockBroken(), new WorldLocation(e.getWorld(), e.getPos()).getPointWithRad(16));
		}
	}
	
	@SubscribeEvent
	public void getDigSpeed(PlayerEvent.BreakSpeed e)
	{
		EntityPlayer player = e.getEntityPlayer();
		ItemStack mainhand = player.getHeldItemMainhand();
		
		if(InterItemStack.isStackNull(mainhand) && mainhand.getItem() instanceof ItemEnhancedPickaxe)
		{
			int rank = ItemEnhancedPickaxe.getRank(mainhand);
			e.setNewSpeed(e.getNewSpeed() + e.getOriginalSpeed() * rank / 8F);
		}
	}
	
	@SubscribeEvent
	public void getDrops(BlockEvent.HarvestDropsEvent e)
	{
		List<ItemStack> drops = e.getDrops();
		
		EntityPlayer player = e.getHarvester();
		
		if(player == null)
			return;
		
		ItemStack mainhand = player.getHeldItemMainhand();
		
		if(!InterItemStack.isStackNull(mainhand) && mainhand.getItem() instanceof ItemEnhancedPickaxe)
		{
			if(e.getState().getBlock() == Blocks.STONE)
			{
				int chance = ItemEnhancedPickaxe.calcTreasureChance(mainhand, player);
				if(chance > 0 && player.getRNG().nextInt(100) < chance)
				{
					Drop drop = EnchancedPickaxeTreasures.choose(player.getRNG(), ItemEnhancedPickaxe.getRank(mainhand));
					
					if(drop != null)
						drops.add(drop.getStack());
				}
			}
			
			mainhand.setItemDamage(Math.max(0, mainhand.getItemDamage() - drops.size() * 2));
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