package com.pengu.vanillatech.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.pengu.hammercore.api.ITileBlock;
import com.pengu.vanillatech.tile.TileFisher;

public class BlockFisher extends Block implements ITileEntityProvider, ITileBlock<TileFisher>
{
	public BlockFisher()
	{
		super(Material.WOOD);
		setSoundType(SoundType.WOOD);
		setUnlocalizedName("fisher");
		setHardness(1.5F);
	}
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		return new TileFisher();
	}
	
	@Override
	public Class<TileFisher> getTileClass()
	{
		return TileFisher.class;
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}
	
	@Override
	public boolean isFullBlock(IBlockState state)
	{
		return false;
	}
	
	@Override
	public boolean isFullCube(IBlockState state)
	{
		return false;
	}
	
	@Override
	public BlockRenderLayer getBlockLayer()
	{
		return BlockRenderLayer.CUTOUT;
	}
	
	public final AxisAlignedBB bb = new AxisAlignedBB(0, -1.5 / 16, 0, 1, 4.5 / 16, 1);
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
	{
		return bb;
	}
}