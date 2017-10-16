package com.pengu.vanillatech.blocks;

import com.pengu.hammercore.core.gui.GuiManager;
import com.pengu.hammercore.utils.iRegisterListener;
import com.pengu.vanillatech.init.GuiCallbacksVT;

import net.minecraft.block.BlockWorkbench;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.stats.StatList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

public class BlockCustomCraftingTable extends BlockWorkbench implements iRegisterListener
{
	public BlockCustomCraftingTable(String type)
	{
		setUnlocalizedName(type + "_crafting_table");
		setHardness(2.5F);
		setSoundType(SoundType.WOOD);
	}
	
	/**
	 * Called when the block is right clicked by a player.
	 */
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		if(worldIn.isRemote)
			return true;
		else
		{
			GuiManager.openGuiCallback(GuiCallbacksVT.CUSTOM_CRAFTING_TABLE.getGuiID(), playerIn, worldIn, pos);
			playerIn.addStat(StatList.CRAFTING_TABLE_INTERACTION);
			return true;
		}
	}
	
	@Override
	public void onRegistered()
	{
		OreDictionary.registerOre("workbench", this);
	}
}