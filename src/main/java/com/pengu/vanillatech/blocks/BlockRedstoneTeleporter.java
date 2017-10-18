package com.pengu.vanillatech.blocks;

import java.util.List;
import java.util.UUID;

import com.pengu.hammercore.api.iTileBlock;
import com.pengu.hammercore.common.blocks.iItemBlock;
import com.pengu.hammercore.common.utils.WorldUtil;
import com.pengu.vanillatech.blocks.items.ItemBlockRedstoneTeleporter;
import com.pengu.vanillatech.tile.TileRedstoneTeleporter;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockRedstoneTeleporter extends BlockContainer implements iItemBlock, iTileBlock<TileRedstoneTeleporter>
{
	public static final AxisAlignedBB aabb = new AxisAlignedBB(0, 0, 0, 1, 2 / 16D, 1);
	public final ItemBlockRedstoneTeleporter item = new ItemBlockRedstoneTeleporter(this);
	
	public BlockRedstoneTeleporter()
	{
		super(Material.CIRCUITS);
		setUnlocalizedName("redstone_teleporter");
	}
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		return new TileRedstoneTeleporter();
	}
	
	@Override
	public Class<TileRedstoneTeleporter> getTileClass()
	{
		return TileRedstoneTeleporter.class;
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
	public boolean canConnectRedstone(IBlockState state, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
	{
		TileRedstoneTeleporter teleporter = WorldUtil.cast(blockAccess.getTileEntity(pos), TileRedstoneTeleporter.class);
		return teleporter != null && teleporter.direction == side.getOpposite();
	}
	
	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
	{
		TileRedstoneTeleporter teleporter = WorldUtil.cast(worldIn.getTileEntity(pos), TileRedstoneTeleporter.class);
		if(teleporter == null)
		{
			teleporter = new TileRedstoneTeleporter();
			worldIn.setTileEntity(pos, teleporter);
		}
		
		teleporter.direction = placer.getHorizontalFacing().getOpposite();
		
		if(stack.hasTagCompound() && stack.getTagCompound().hasKey("UUIDMost"))
			teleporter.uuid = stack.getTagCompound().getUniqueId("UUID");
		else
			teleporter.uuid = UUID.randomUUID();
	}
	
	@Override
	public int getWeakPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
	{
		TileRedstoneTeleporter teleporter = WorldUtil.cast(blockAccess.getTileEntity(pos), TileRedstoneTeleporter.class);
		if(teleporter != null && teleporter.isReceiving && teleporter.direction == side.getOpposite())
			return teleporter.redstone;
		return 0;
	}
	
	@Override
	public int getStrongPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
	{
		return getWeakPower(blockState, blockAccess, pos, side);
	}
	
	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player)
	{
		ItemStack stack = new ItemStack(this);
		stack.setTagCompound(new NBTTagCompound());
		TileRedstoneTeleporter tp = WorldUtil.cast(world.getTileEntity(pos), TileRedstoneTeleporter.class);
		if(tp != null)
			stack.getTagCompound().setUniqueId("UUID", tp.uuid);
		return stack;
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		TileRedstoneTeleporter tile = WorldUtil.cast(worldIn.getTileEntity(pos), TileRedstoneTeleporter.class);
		if(tile != null)
		{
//			if(!playerIn.getHeldItem(hand).isEmpty() && playerIn.getHeldItem(hand).getItem() == Item.getItemFromBlock(BlocksVT.REDSTONE_TELEPORTER))
//			{
//				ItemStack stack = playerIn.getHeldItem(hand);
//				if(!stack.hasTagCompound())
//					stack.setTagCompound(new NBTTagCompound());
//				stack.getTagCompound().setUniqueId("UUID", tile.uuid);
//				return true;
//			}
			
			tile.isReceiving = !tile.isReceiving;
			if(tile.isReceiving)
			{
				TileRedstoneTeleporter.ENABLED.put(tile.uuid.toString(), 0);
				tile.getLocation().markDirty(3);
			} else
				tile.getLocation().markDirty(3);
		}
		return true;
	}
	
	@Override
	public void addInformation(ItemStack stack, World player, List<String> tooltip, ITooltipFlag advanced)
	{
		if(stack.hasTagCompound() && stack.getTagCompound().hasKey("UUIDMost"))
			tooltip.add("Frequency: " + stack.getTagCompound().getUniqueId("UUID"));
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
	{
		return aabb;
	}
	
	@Override
	public ItemBlock getItemBlock()
	{
		return item;
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
		TileRedstoneTeleporter tp = WorldUtil.cast(tile, TileRedstoneTeleporter.class);
		if(tp != null && tp.uuid != null)
		{
			ItemStack stack = new ItemStack(this);
			stack.setTagCompound(new NBTTagCompound());
			stack.getTagCompound().setUniqueId("UUID", tp.uuid);
			drops.add(stack);
		}
		else
			drops.add(new ItemStack(this));
	}
}