package com.pengu.vanillatech.init;

import net.minecraft.item.Item.ToolMaterial;
import net.minecraftforge.common.util.EnumHelper;

import com.pengu.hammercore.utils.NPEUtils;
import com.pengu.vanillatech.Info;

public class ItemMaterialsVT
{
	{
		NPEUtils.noInstancesError();
	}
	
	public static final ToolMaterial TOOL_ENHANCED = EnumHelper.addToolMaterial(Info.MOD_ID + ":enhanced", 10, 5000, 8, 12, 60);
}