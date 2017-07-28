package com.pengu.vanillatech.utils;

import net.minecraft.util.Tuple;

public class AtomicTuple<A, B> extends Tuple<A, B>
{
	protected A first;
	protected B second;
	
	public AtomicTuple(A aIn, B bIn)
	{
		super(aIn, bIn);
		this.first = aIn;
		this.second = bIn;
	}
	
	@Override
	public A getFirst()
	{
		return first;
	}
	
	@Override
	public B getSecond()
	{
		return second;
	}
	
	public void setFirst(A first)
	{
		this.first = first;
	}
	
	public void setSecond(B second)
	{
		this.second = second;
	}
}