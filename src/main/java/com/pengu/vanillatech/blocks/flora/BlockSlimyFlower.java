package com.pengu.vanillatech.blocks.flora;

import java.util.Random;

import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Biomes;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemShears;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;

import com.pengu.hammercore.common.HolidayTrigger;
import com.pengu.hammercore.utils.AdvancementUtils;
import com.pengu.vanillatech.Info;

public class BlockSlimyFlower extends BlockWorldgenFlower
{
	public BlockSlimyFlower()
	{
		super("slimy_flower");
		setSoundType(SoundType.SLIME);
		worldgen_spawnableBiomes = biome -> biome == Biomes.SWAMPLAND;
		worldgen_maxSpread = 5;
		worldgen_minCount = 2;
		worldgen_maxCount = 6;
	}
	
	@Override
	public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player)
	{
		return true;
	}
	
	@Override
	public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, TileEntity te, ItemStack stack)
	{
		player.addStat(StatList.getBlockStats(this));
		player.addExhaustion(0.005F);
		
		if(EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, stack) > 0 || stack.getItem() instanceof ItemShears)
		{
			java.util.List<ItemStack> items = new java.util.ArrayList<ItemStack>();
			ItemStack itemstack = this.getSilkTouchDrop(state);
			if(!itemstack.isEmpty())
				items.add(itemstack);
			ForgeEventFactory.fireBlockHarvesting(items, worldIn, pos, state, 0, 1.0f, true, player);
			for(ItemStack item : items)
				spawnAsEntity(worldIn, pos, item);
		} else
		{
			harvesters.set(player);
			int i = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, stack);
			this.dropBlockAsItem(worldIn, pos, state, i);
			harvesters.set(null);
		}
	}
	
	@Override
	public float getBlockHardness(IBlockState blockState, World worldIn, BlockPos pos)
	{
		return HolidayTrigger.isAprilFools() ? 50 : super.getBlockHardness(blockState, worldIn, pos);
	}
	
	@Override
	public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn)
	{
		entityIn.motionX *= HolidayTrigger.isAprilFools() ? .00001 : .5;
		entityIn.motionZ *= HolidayTrigger.isAprilFools() ? .00001 : .5;
	}
	
	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune)
	{
		return Items.SLIME_BALL;
	}
	
	@Override
	public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player)
	{
		AdvancementUtils.completeAdvancement(new ResourceLocation(Info.MOD_ID, "flora/slimy_flower"), player);
	}
}