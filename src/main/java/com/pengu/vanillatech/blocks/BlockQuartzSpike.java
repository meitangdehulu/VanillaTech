package com.pengu.vanillatech.blocks;

import java.util.List;
import java.util.Random;
import java.util.function.Predicate;

import com.pengu.hammercore.utils.iRegisterListener;
import com.pengu.hammercore.world.WorldGenHelper;
import com.pengu.hammercore.world.gen.WorldRetroGen;
import com.pengu.hammercore.world.gen.iWorldGenFeature;
import com.pengu.vanillatech.init.DamageSourcesVT;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.DimensionType;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockQuartzSpike extends Block implements iWorldGenFeature, iRegisterListener
{
	public static final AxisAlignedBB aabb = new AxisAlignedBB(1 / 8D, 0, 1 / 8D, 7 / 8D, 6 / 8D, 7 / 8D);
	
	public BlockQuartzSpike()
	{
		super(Material.ROCK);
		setUnlocalizedName("quartz_spike");
		setHardness(1.2F);
		setHarvestLevel("pickaxe", 1);
	}
	
	@Override
	public void onRegistered()
	{
		WorldRetroGen.addWorldFeature(this);
	}
	
	@Override
	public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn)
	{
		if(entityIn instanceof EntityLivingBase && entityIn.ticksExisted % 5 == 0)
			((EntityLivingBase) entityIn).attackEntityFrom(DamageSourcesVT.QUARTZ_SPIKE, 1);
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
	{
		return aabb;
	}
	
	@Override
	public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, Entity entityIn, boolean p_185477_7_)
	{
		
	}
	
	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos)
	{
		return null;
	}
	
	@Override
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
	{
		return worldIn.getBlockState(pos.down()).isSideSolid(worldIn, pos, EnumFacing.UP);
	}
	
	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
	{
		if(!canPlaceBlockAt(worldIn, pos))
			worldIn.destroyBlock(pos, true);
	}
	
	@Override
	public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
	{
		super.getDrops(drops, world, pos, state, fortune);
		Random rand = world instanceof World ? ((World) world).rand : RANDOM;
		
		if(rand.nextInt(4) == 0)
			drops.add(new ItemStack(Blocks.NETHERRACK));
	}
	
	@Override
	public int quantityDropped(IBlockState state, int fortune, Random random)
	{
		return 1 + random.nextInt(2 + Math.min(fortune, 5));
	}
	
	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune)
	{
		return Items.QUARTZ;
	}
	
	@Override
	public int getMaxChances(World world, ChunkPos chunk, Random rand)
	{
		return world.provider.getDimensionType() == DimensionType.NETHER ? 4 : 1;
	}
	
	@Override
	public int getMinY(World world, BlockPos pos, Random rand)
	{
		return 0;
	}
	
	@Override
	public int getMaxY(World world, BlockPos pos, Random rand)
	{
		return 128;
	}
	
	@Override
	public BlockRenderLayer getBlockLayer()
	{
		return BlockRenderLayer.CUTOUT;
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
	
	private Predicate<IBlockState> NETHERRACK_OR_QUARTZ = state -> state != null && (state.getBlock() == Blocks.NETHERRACK || state.getBlock() == Blocks.QUARTZ_ORE);
	
	@Override
	public void generate(World world, BlockPos pos, Random rand)
	{
		pos = lowest(world, pos);
		if(world.getBlockState(pos).getMaterial() != Material.LAVA)
			WorldGenHelper.generateFlowerOnSameY(getDefaultState(), rand, world, pos, 16, 8, 12, true, NETHERRACK_OR_QUARTZ);
	}
	
	private BlockPos lowest(World world, BlockPos pos)
	{
		BlockPos origin = pos;
		while(pos.getY() > 0 && world.getBlockState(pos).getBlock().isReplaceable(world, pos))
			pos = pos.down();
		if(world.getBlockState(pos.up()).getMaterial() == Material.LAVA)
			return origin;
		return pos.up();
	}
}