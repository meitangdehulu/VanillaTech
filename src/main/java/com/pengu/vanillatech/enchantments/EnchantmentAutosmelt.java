package com.pengu.vanillatech.enchantments;

import java.util.List;
import java.util.Map;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.OreDictionary;

import com.pengu.hammercore.HammerCore;
import com.pengu.hammercore.common.InterItemStack;
import com.pengu.hammercore.net.HCNetwork;
import com.pengu.hammercore.net.pkt.PacketParticle;
import com.pengu.hammercore.utils.WorldLocation;
import com.pengu.vanillatech.InfoVT;

public class EnchantmentAutosmelt extends Enchantment
{
	public EnchantmentAutosmelt()
	{
		super(Rarity.RARE, EnumEnchantmentType.DIGGER, new EntityEquipmentSlot[] { EntityEquipmentSlot.MAINHAND, EntityEquipmentSlot.OFFHAND });
		setName("auto_smelt");
	}
	
	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack)
	{
		return super.canApplyAtEnchantingTable(stack) && canApply(stack);
	}
	
	@Override
	public boolean canApply(ItemStack stack)
	{
		return stack.getItem() instanceof ItemTool || stack.getItem() instanceof ItemSword;
	}
	
	@Override
	protected boolean canApplyTogether(Enchantment ench)
	{
		return ench != Enchantments.SILK_TOUCH && ench != this;
	}
	
	@SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = false)
	public void drops(BlockEvent.HarvestDropsEvent e)
	{
		EntityPlayer player = e.getHarvester();
		if(player == null)
			return;
		Map<Enchantment, Integer> lvls = EnchantmentHelper.getEnchantments(player.getHeldItemMainhand());
		int fortune = lvls.get(Enchantments.FORTUNE) != null ? lvls.get(Enchantments.FORTUNE).intValue() : 0;
		
		boolean smelted = false;
		Integer lvl = lvls.get(this);
		if(lvl != null && lvl.intValue() > 0)
		{
			List<ItemStack> stacks = e.getDrops();
			for(int i = 0; i < stacks.size(); ++i)
			{
				ItemStack stack = stacks.get(i);
				int[] ids = OreDictionary.getOreIDs(stack);
				boolean isOre = false;
				for(int id : ids)
					if(OreDictionary.getOreName(id).startsWith("ore"))
					{
						isOre = true;
						break;
					}
				if(fortune > 0 && isOre)
					stack.grow(player.world.rand.nextInt(1 + fortune));
				ItemStack result = FurnaceRecipes.instance().getSmeltingResult(stack);
				if(!InterItemStack.isStackNull(result))
				{
					result = result.copy();
					result.setCount(result.getCount() * stack.getCount());
					stacks.set(i, result);
					smelted = true;
					float xp = FurnaceRecipes.instance().getSmeltingExperience(stack);
					if(!player.world.isRemote)
					{
						int j = result.getCount();
						
						if(xp == 0F)
							j = 0;
						else if(xp < 1F)
						{
							int k = MathHelper.floor((float) j * xp);
							if(k < MathHelper.ceil((float) j * xp) && Math.random() < (double) ((float) j * xp - (float) k))
								++k;
							j = k;
						}
						while(j > 0)
						{
							int k = EntityXPOrb.getXPSplit(j);
							j -= k;
							if(!player.world.isRemote)
								player.world.spawnEntity(new EntityXPOrb(player.world, player.posX, player.posY + .5, player.posZ + .5, k));
						}
					}
				}
			}
		}
		
		if(!player.world.isRemote && smelted)
		{
			int p = 12 + player.world.rand.nextInt(32);
			for(int i = 0; i < p; ++i)
				HCNetwork.manager.sendToAllAround(new PacketParticle(player.world, EnumParticleTypes.FLAME, new Vec3d(e.getPos()).addVector(player.world.rand.nextDouble(), player.world.rand.nextDouble(), player.world.rand.nextDouble()), new Vec3d(0, 0, 0)), new WorldLocation(e.getWorld(), e.getPos()).getPointWithRad(64));
			HammerCore.audioProxy.playSoundAt(e.getWorld(), SoundEvents.BLOCK_FIRE_AMBIENT.getRegistryName().toString(), e.getPos(), 2F, 1F, SoundCategory.PLAYERS);
		}
	}
}