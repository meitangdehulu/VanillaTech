package com.pengu.vanillatech.blocks.pim;

import com.pengu.hammercore.api.iTileBlock;
import com.pengu.hammercore.common.utils.WorldUtil;
import com.pengu.hammercore.core.gui.GuiManager;
import com.pengu.hammercore.tile.TileSyncable;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockPlayerInventoryManager extends Block implements iTileBlock<TilePlayerInventoryManager>, ITileEntityProvider
{
	public BlockPlayerInventoryManager()
	{
		super(Material.IRON);
		setSoundType(SoundType.METAL);
		setUnlocalizedName("player_inventory_manager");
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		GuiManager.openGui(playerIn, WorldUtil.cast(worldIn.getTileEntity(pos), TileSyncable.class));
		return true;
	}
	
	@Override
	public Class<TilePlayerInventoryManager> getTileClass()
	{
		return TilePlayerInventoryManager.class;
	}
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		return new TilePlayerInventoryManager();
	}
	
	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
	{
		if(placer instanceof EntityPlayer)
			worldIn.setTileEntity(pos, new TilePlayerInventoryManager(((EntityPlayer) placer).inventory));
		else
			worldIn.destroyBlock(pos, true);
	}
}