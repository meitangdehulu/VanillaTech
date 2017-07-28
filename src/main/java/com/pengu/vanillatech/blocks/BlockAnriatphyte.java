package com.pengu.vanillatech.blocks;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.pengu.hammercore.HammerCore;
import com.pengu.hammercore.api.ITileBlock;
import com.pengu.vanillatech.tile.TileAnriatphyteBlock;

public class BlockAnriatphyte extends Block implements ITileEntityProvider, ITileBlock<TileAnriatphyteBlock>
{
	public BlockAnriatphyte()
	{
		super(Material.ROCK);
		setUnlocalizedName("anriatphyte_block");
		setHardness(4F);
	}
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		return new TileAnriatphyteBlock();
	}
	
	@Override
	public Class<TileAnriatphyteBlock> getTileClass()
	{
		return TileAnriatphyteBlock.class;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand)
	{
		if(rand.nextInt(8) == 0)
			HammerCore.particleProxy.spawnSlowZap(worldIn, new Vec3d(pos.getX() + .5, pos.getY() + .5, pos.getZ() + .5), new Vec3d(pos.getX() + .5 + (rand.nextDouble() - rand.nextDouble()) * 2, pos.getY() + .5 + (rand.nextDouble() - rand.nextDouble()) * 2, pos.getZ() + .5 + (rand.nextDouble() - rand.nextDouble()) * 2), 0x77FF77, 30, .5F);
	}
}