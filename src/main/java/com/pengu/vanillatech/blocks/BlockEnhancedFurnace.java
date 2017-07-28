package com.pengu.vanillatech.blocks;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.Mirror;
import net.minecraft.util.NonNullList;
import net.minecraft.util.Rotation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.pengu.hammercore.api.ITileBlock;
import com.pengu.hammercore.common.blocks.IItemBlock;
import com.pengu.hammercore.common.utils.WorldUtil;
import com.pengu.hammercore.gui.GuiManager;
import com.pengu.hammercore.tile.TileSyncable;
import com.pengu.vanillatech.blocks.items.ItemBlockEnhancedFurnace;
import com.pengu.vanillatech.init.BlocksVT;
import com.pengu.vanillatech.tile.TileEnhancedFurnace;

public class BlockEnhancedFurnace extends Block implements ITileEntityProvider, ITileBlock<TileEnhancedFurnace>, IItemBlock
{
	public static final PropertyDirection FACING = BlockHorizontal.FACING;
	public static final PropertyBool ENABLED = PropertyBool.create("active");
	private final ItemBlockEnhancedFurnace item = new ItemBlockEnhancedFurnace(this);
	private static boolean keepInventory = false;
	
	public BlockEnhancedFurnace()
	{
		super(Material.IRON);
		setSoundType(SoundType.METAL);
		setUnlocalizedName("enhanced_furnace");
		setHardness(2F);
		setHarvestLevel("pickaxe", 0);
	}
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		return new TileEnhancedFurnace();
	}
	
	@Override
	public Class<TileEnhancedFurnace> getTileClass()
	{
		return TileEnhancedFurnace.class;
	}
	
	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand)
	{
		return getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite()).withProperty(ENABLED, false);
	}
	
	public boolean hasComparatorInputOverride(IBlockState state)
	{
		return true;
	}
	
	public int getComparatorInputOverride(IBlockState blockState, World worldIn, BlockPos pos)
	{
		return Container.calcRedstone(worldIn.getTileEntity(pos));
	}
	
	public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state)
	{
		TileEnhancedFurnace furnace = WorldUtil.cast(worldIn.getTileEntity(pos), TileEnhancedFurnace.class);
		if(furnace != null)
			return furnace.getDrop();
		return new ItemStack(BlocksVT.ENHANCED_FURNACE);
	}
	
	@Override
	public EnumBlockRenderType getRenderType(IBlockState state)
	{
		return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
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
	public int getLightValue(IBlockState state)
	{
		return state.getValue(ENABLED) ? 15 : 0;
	}
	
	public IBlockState getStateFromMeta(int meta)
	{
		boolean enabled = meta > 5;
		EnumFacing enumfacing = EnumFacing.getFront(enabled ? meta - 6 : meta);
		if(enumfacing.getAxis() == EnumFacing.Axis.Y)
			enumfacing = EnumFacing.NORTH;
		return getDefaultState().withProperty(FACING, enumfacing).withProperty(ENABLED, enabled);
	}
	
	public int getMetaFromState(IBlockState state)
	{
		int index = state.getValue(FACING).getIndex();
		if(state.getValue(ENABLED))
			index += 6;
		return index;
	}
	
	public IBlockState withRotation(IBlockState state, Rotation rot)
	{
		return state.withProperty(FACING, rot.rotate(state.getValue(FACING)));
	}
	
	public IBlockState withMirror(IBlockState state, Mirror mirrorIn)
	{
		return state.withRotation(mirrorIn.toRotation(state.getValue(FACING)));
	}
	
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, FACING, ENABLED);
	}
	
	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
	{
		worldIn.setBlockState(pos, state.withProperty(FACING, placer.getHorizontalFacing().getOpposite()), 2);
		worldIn.setTileEntity(pos, new TileEnhancedFurnace(stack));
		
		if(stack.hasDisplayName())
		{
			TileEntity tileentity = worldIn.getTileEntity(pos);
			if(tileentity instanceof TileEnhancedFurnace)
				((TileEnhancedFurnace) tileentity).setCustomInventoryName(stack.getDisplayName());
		}
	}
	
	@Override
	public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, TileEntity te, ItemStack stack)
	{
		player.addStat(StatList.getBlockStats(this));
		player.addExhaustion(0.005F);
		
		harvesters.set(player);
		dropBlockAsItemWithChance(worldIn, pos, state, 1, 0, te);
		harvesters.set(null);
	}
	
	public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune, TileEntity tile)
	{
		if(!worldIn.isRemote && !worldIn.restoringBlockSnapshots)
		{
			NonNullList<ItemStack> drops = NonNullList.<ItemStack> create();
			getDrops(drops, worldIn, pos, state, fortune, tile);
			chance = net.minecraftforge.event.ForgeEventFactory.fireBlockHarvesting(drops, worldIn, pos, state, fortune, chance, false, harvesters.get());
			for(ItemStack drop : drops)
				if(worldIn.rand.nextFloat() <= chance)
					spawnAsEntity(worldIn, pos, drop);
		}
	}
	
	public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune, TileEntity tile)
	{
		TileEnhancedFurnace furer = WorldUtil.cast(tile, TileEnhancedFurnace.class);
		if(furer != null)
			drops.add(furer.getDrop());
		else
			drops.add(new ItemStack(this));
	}
	
	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
	{
		TileEnhancedFurnace furer = WorldUtil.cast(worldIn.getTileEntity(pos), TileEnhancedFurnace.class);
		if(furer != null && !keepInventory)
			furer.inventory.drop(worldIn, pos);
		super.breakBlock(worldIn, pos, state);
	}
	
	public static void setState(boolean active, World worldIn, BlockPos pos)
	{
		IBlockState iblockstate = worldIn.getBlockState(pos);
		TileEntity tileentity = worldIn.getTileEntity(pos);
		keepInventory = true;
		
		worldIn.setBlockState(pos, BlocksVT.ENHANCED_FURNACE.getDefaultState().withProperty(FACING, iblockstate.getValue(FACING)).withProperty(ENABLED, active), 3);
		
		keepInventory = false;
		
		if(tileentity != null)
		{
			tileentity.validate();
			worldIn.setTileEntity(pos, tileentity);
		}
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		GuiManager.openGui(playerIn, WorldUtil.cast(worldIn.getTileEntity(pos), TileSyncable.class));
		return true;
	}
	
	@SideOnly(Side.CLIENT)
	@SuppressWarnings("incomplete-switch")
	public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand)
	{
		if(stateIn.getValue(ENABLED))
		{
			EnumFacing enumfacing = (EnumFacing) stateIn.getValue(FACING);
			double d0 = pos.getX() + .5;
			double d1 = pos.getY() + rand.nextDouble() * 6D / 16D;
			double d2 = pos.getZ() + .5;
			double d3 = .52;
			double d4 = rand.nextDouble() * .6 - .3;
			
			if(rand.nextDouble() < 0.1D)
				worldIn.playSound(pos.getX() + .5, pos.getY(), pos.getZ() + .5, SoundEvents.BLOCK_FURNACE_FIRE_CRACKLE, SoundCategory.BLOCKS, 1, 1, false);
			
			switch(enumfacing)
			{
			case WEST:
				worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0 - 0.52D, d1, d2 + d4, 0.0D, 0.0D, 0.0D);
				worldIn.spawnParticle(EnumParticleTypes.FLAME, d0 - 0.52D, d1, d2 + d4, 0.0D, 0.0D, 0.0D);
			break;
			case EAST:
				worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0 + 0.52D, d1, d2 + d4, 0.0D, 0.0D, 0.0D);
				worldIn.spawnParticle(EnumParticleTypes.FLAME, d0 + 0.52D, d1, d2 + d4, 0.0D, 0.0D, 0.0D);
			break;
			case NORTH:
				worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0 + d4, d1, d2 - 0.52D, 0.0D, 0.0D, 0.0D);
				worldIn.spawnParticle(EnumParticleTypes.FLAME, d0 + d4, d1, d2 - 0.52D, 0.0D, 0.0D, 0.0D);
			break;
			case SOUTH:
				worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0 + d4, d1, d2 + 0.52D, 0.0D, 0.0D, 0.0D);
				worldIn.spawnParticle(EnumParticleTypes.FLAME, d0 + d4, d1, d2 + 0.52D, 0.0D, 0.0D, 0.0D);
			}
		}
	}
	
	@Override
	public ItemBlock getItemBlock()
	{
		return item;
	}
}