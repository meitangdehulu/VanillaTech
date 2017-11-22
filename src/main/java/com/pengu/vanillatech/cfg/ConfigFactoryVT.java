package com.pengu.vanillatech.cfg;

import java.util.Set;

import com.pengu.hammercore.cfg.gui.HCConfigGui;
import com.pengu.vanillatech.InfoVT;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.IModGuiFactory;

public class ConfigFactoryVT implements IModGuiFactory
{
	@Override
	public void initialize(Minecraft minecraftInstance)
	{
	}
	
	@Override
	public boolean hasConfigGui()
	{
		return true;
	}
	
	@Override
	public GuiScreen createConfigGui(GuiScreen parentScreen)
	{
		return new HCConfigGui(parentScreen, ConfigsVT.cfgs, InfoVT.MOD_ID);
	}
	
	@Override
	public Set<RuntimeOptionCategoryElement> runtimeGuiCategories()
	{
		return null;
	}
}