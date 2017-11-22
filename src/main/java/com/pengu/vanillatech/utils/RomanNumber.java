package com.pengu.vanillatech.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class RomanNumber
{
	private static final TreeMap<Integer, String> map = new TreeMap<Integer, String>();
	private static final Map<Integer, String> cached = new HashMap<>();
	
	static
	{
		
		map.put(1000, "M");
		map.put(900, "CM");
		map.put(500, "D");
		map.put(400, "CD");
		map.put(100, "C");
		map.put(90, "XC");
		map.put(50, "L");
		map.put(40, "XL");
		map.put(10, "X");
		map.put(9, "IX");
		map.put(5, "V");
		map.put(4, "IV");
		map.put(1, "I");
	}
	
	public final static String toRoman(int number)
	{
		if(number == 0)
			return "0";
		
		if(cached.containsKey(number))
			return cached.get(number);
		
		int l = map.floorKey(number);
		
		if(number == l)
			return map.get(number);
		
		String v = map.get(l) + toRoman(number - l);
		
		cached.put(number, v);
		
		return v;
	}
	
	static
	{
		for(int i = 1; i < 256; ++i)
			toRoman(i);
	}
}