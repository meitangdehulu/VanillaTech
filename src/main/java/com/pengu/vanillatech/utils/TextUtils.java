package com.pengu.vanillatech.utils;

import org.lwjgl.input.Keyboard;

import net.minecraft.util.text.TextFormatting;

public class TextUtils
{
	public static boolean isShift()
	{
		try
		{
			return Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT);
		} catch(Throwable er)
		{
		}
		
		return false;
	}
	
	public static String SHIFTFORDETAILS()
	{
		return isShift() ? "" : "(Press " + SHIFT() + " for details...)";
	}
	
	public static String SHIFT()
	{
		return YUTR("SHIFT");
	}
	
	public static String CTRL()
	{
		return YUTR("CTRL");
	}
	
	public static String ALT()
	{
		return YUTR("ALT");
	}
	
	public static String YUTR(String t)
	{
		return TextFormatting.YELLOW.toString() + TextFormatting.UNDERLINE + t + TextFormatting.GRAY;
	}
}