package com.pengu.vanillatech.blocks;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.pengu.hammercore.HammerCore;

public class BlockUnstableMetal extends Block
{
	public BlockUnstableMetal()
	{
		super(Material.IRON);
		setSoundType(SoundType.METAL);
		setHardness(5F);
		setUnlocalizedName("unstable_metal_block");
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand)
	{
		if(rand.nextInt(6) == 0)
			HammerCore.particleProxy.spawnSlowZap(worldIn, new Vec3d(pos.getX() + .5, pos.getY() + .5, pos.getZ() + .5), new Vec3d(pos.getX() + .5 + (rand.nextDouble() - rand.nextDouble()) * 2, pos.getY() + .5 + (rand.nextDouble() - rand.nextDouble()) * 2, pos.getZ() + .5 + (rand.nextDouble() - rand.nextDouble()) * 2), 0xFFFFFF, 10, 1F);
	}
}