package com.pengu.vanillatech.blocks;

import java.util.Random;
import java.util.function.IntSupplier;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.pengu.vanillatech.VanillaTech;

public class BlockGlowstoneOre extends BlockOD
{
	public static IntSupplier BRIGHTNESS = () ->
	{
		int bright = 20 + (int) Math.abs(Math.sin(System.currentTimeMillis() / 500D) * 10);
		return (int) (Math.min(bright, 15) << 20 | (bright - 15) << 4);
	};
	
	public BlockGlowstoneOre()
	{
		super(Material.ROCK, "oreGlowstone");
		setUnlocalizedName("glowstone_ore");
		setHardness(3F);
		setResistance(5F);
		setHarvestLevel("pickaxe", 1);
	}
	
	@Override
	public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player)
	{
		return true;
	}
	
	@Override
	public int quantityDropped(IBlockState state, int fortune, Random random)
	{
		return 1 + random.nextInt(fortune + 2);
	}
	
	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune)
	{
		return Items.GLOWSTONE_DUST;
	}
	
	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
	{
		if(worldIn.isRemote)
		{
			VanillaTech.proxy.setBlockGlowing(worldIn, pos);
			VanillaTech.proxy.setBlockGlowingBrightness(pos, BRIGHTNESS);
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand)
	{
		VanillaTech.proxy.setBlockGlowing(worldIn, pos);
		VanillaTech.proxy.setBlockGlowingBrightness(pos, BRIGHTNESS);
	}
}