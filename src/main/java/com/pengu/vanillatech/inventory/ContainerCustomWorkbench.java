package com.pengu.vanillatech.inventory;

import net.minecraft.block.BlockWorkbench;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ContainerWorkbench;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ContainerCustomWorkbench extends ContainerWorkbench
{
	private final World world_;
	/** Position of the workbench */
	private final BlockPos pos_;
	private final EntityPlayer player_;
	
	public ContainerCustomWorkbench(InventoryPlayer playerInventory, World worldIn, BlockPos posIn)
	{
		super(playerInventory, worldIn, posIn);
		this.world_ = worldIn;
		this.pos_ = posIn;
		this.player_ = playerInventory.player;
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer playerIn)
	{
		if(!(world_.getBlockState(pos_).getBlock() instanceof BlockWorkbench))
			return false;
		else
			return playerIn.getDistanceSq(pos_.getX() + .5, pos_.getY() + .5, pos_.getZ() + .5) <= 64;
	}
}