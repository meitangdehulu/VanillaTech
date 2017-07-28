package com.pengu.vanillatech.blocks.flora;


public class BlockDaisy extends BlockWorldgenFlower
{
	public BlockDaisy()
	{
		super("daisy");
		worldgen_maxSpread = 5;
		worldgen_minCount = 2;
		worldgen_maxCount = 4;
	}
}