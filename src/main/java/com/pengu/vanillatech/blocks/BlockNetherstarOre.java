package com.pengu.vanillatech.blocks;

import java.util.Random;
import java.util.function.IntSupplier;

import com.pengu.vanillatech.VanillaTech;
import com.pengu.vanillatech.init.EnchantmentsVT;
import com.pengu.vanillatech.init.ItemsVT;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockNetherstarOre extends Block
{
	public static int bright = 30;
	public static final IntSupplier BRIGHTNESS = () -> (int) (Math.min(bright, 15) << 20 | (bright - 15) << 4);
	
	public BlockNetherstarOre()
	{
		super(Material.ROCK);
		setUnlocalizedName("netherstar_ore");
		setHardness(1.5F);
		setHarvestLevel("pickaxe", 0);
	}
	
	@Override
	public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state)
	{
		return new ItemStack(Blocks.STONE);
	}
	
	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player)
	{
		netherStarOreRand.setSeed(pos.toLong() + player.world.provider.getDimension());
		if(player.capabilities.isCreativeMode || canBeSeenWith(player.getHeldItemMainhand(), netherStarOreRand))
			return new ItemStack(this);
		return new ItemStack(Blocks.STONE);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand)
	{
		VanillaTech.proxy.setBlockGlowing(worldIn, pos);
	}
	
	private static final Random netherStarOreRand = new Random();
	
	@Override
	public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
	{
		EntityPlayer player = harvesters.get();
		
		if(player == null)
		{
			drops.add(new ItemStack(Blocks.COBBLESTONE));
			return;
		}
		
		netherStarOreRand.setSeed(pos.toLong() + player.world.provider.getDimension());
		
		Item item = player.getHeldItemMainhand().getItem();
		Integer silk = EnchantmentHelper.getEnchantments(player.getHeldItemMainhand()).get(Enchantments.SILK_TOUCH);
		boolean silkTouch = silk != null && silk > 0;
		
		if(canBeSeenWith(player.getHeldItemMainhand(), netherStarOreRand) && !player.capabilities.isCreativeMode)
			drops.add(new ItemStack(ItemsVT.NETHERSTAR_SHARD, player.world.rand.nextInt(2) + 1));
		else if(!player.capabilities.isCreativeMode)
			drops.add(new ItemStack(silkTouch ? Blocks.STONE : Blocks.COBBLESTONE));
	}
	
	@Override
	public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player)
	{
		return false;
	}
	
	public static boolean canBeSeenWith(ItemStack stack, Random rand)
	{
		if(stack.getItem() == ItemsVT.ENHANCED_PICKAXE)
			return true;
		Integer predictor = EnchantmentHelper.getEnchantments(stack).get(EnchantmentsVT.INFERNAL_PREDICTOR);
		if(predictor != null && predictor > 0)
			if(rand == null)
				return true;
			else
				return rand.nextInt(EnchantmentsVT.INFERNAL_PREDICTOR.getMaxLevel()) < predictor.intValue();
		return false;
	}
}