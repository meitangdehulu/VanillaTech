package com.pengu.vanillatech.blocks;

import com.pengu.hammercore.utils.iRegisterListener;

import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraftforge.oredict.OreDictionary;

public class BlockOD extends Block implements iRegisterListener
{
	public final String od;
	
	public BlockOD(Material blockMaterialIn, MapColor blockMapColorIn, String od)
	{
		super(blockMaterialIn, blockMapColorIn);
		this.od = od;
	}
	
	public BlockOD(Material materialIn, String od)
	{
		super(materialIn);
		this.od = od;
	}
	
	@Override
	public void onRegistered()
	{
		OreDictionary.registerOre(od, this);
	}
}