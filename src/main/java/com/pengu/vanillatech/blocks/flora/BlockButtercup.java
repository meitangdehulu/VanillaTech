package com.pengu.vanillatech.blocks.flora;


public class BlockButtercup extends BlockWorldgenFlower
{
	public BlockButtercup()
	{
		super("buttercup");
		worldgen_maxSpread = 5;
		worldgen_minCount = 2;
		worldgen_maxCount = 8;
	}
}