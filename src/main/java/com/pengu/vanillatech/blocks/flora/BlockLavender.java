package com.pengu.vanillatech.blocks.flora;


public class BlockLavender extends BlockWorldgenFlower
{
	public BlockLavender()
	{
		super("lavender");
		worldgen_maxSpread = 6;
		worldgen_minCount = 2;
		worldgen_maxCount = 7;
	}
}