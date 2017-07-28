package com.pengu.vanillatech.items;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class ItemNetherStarShard extends Item
{
	public ItemNetherStarShard()
	{
		setUnlocalizedName("netherstar_shard");
		setMaxStackSize(8);
		addPropertyOverride(new ResourceLocation("count"), new IItemPropertyGetter()
		{
			@Override
			public float apply(ItemStack stack, World worldIn, EntityLivingBase entityIn)
			{
				return stack.getCount();
			}
		});
	}
	
	@Override
	public boolean hasEffect(ItemStack stack)
	{
		return true;
	}
}