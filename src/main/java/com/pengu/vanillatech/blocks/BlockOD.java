package com.pengu.vanillatech.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraftforge.oredict.OreDictionary;

import com.pengu.hammercore.utils.IRegisterListener;

public class BlockOD extends Block implements IRegisterListener
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