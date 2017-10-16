package com.pengu.vanillatech.blocks;

import com.pengu.hammercore.api.iTileBlock;
import com.pengu.hammercore.common.utils.WorldUtil;
import com.pengu.vanillatech.tile.TileTimeBomb;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockTimeBomb extends Block implements ITileEntityProvider, iTileBlock<TileTimeBomb>
{
	public static final PropertyEnum<EnumBombPhase> PHASE = PropertyEnum.create("phase", EnumBombPhase.class);
	
	public BlockTimeBomb()
	{
		super(Material.ROCK);
		setUnlocalizedName("time_bomb");
		setSoundType(SoundType.PLANT);
		setHardness(2F);
	}
	
	@Override
	public Class<TileTimeBomb> getTileClass()
	{
		return TileTimeBomb.class;
	}
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		return new TileTimeBomb();
	}
	
	@Override
	public int getMetaFromState(IBlockState state)
	{
		return state.getValue(PHASE).ordinal();
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return getDefaultState().withProperty(PHASE, EnumBombPhase.values()[meta % 3]);
	}
	
	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, PHASE);
	}
	
	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
	{
		TileTimeBomb bomb = WorldUtil.cast(worldIn.getTileEntity(pos), TileTimeBomb.class);
		if(bomb == null)
		{
			bomb = new TileTimeBomb(placer);
			worldIn.setTileEntity(pos, bomb);
		}
		bomb.PLACER_ID.set(placer.getEntityId());
	}
	
	public static enum EnumBombPhase implements IStringSerializable
	{
		GREEN, YELLOW, RED;
		
		public String getName()
		{
			return name().toLowerCase();
		}
	}
}