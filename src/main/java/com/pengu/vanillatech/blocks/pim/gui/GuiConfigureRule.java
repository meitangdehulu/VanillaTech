package com.pengu.vanillatech.blocks.pim.gui;

import java.io.IOException;

import org.lwjgl.input.Keyboard;

import com.pengu.hammercore.net.HCNetwork;
import com.pengu.vanillatech.InfoVT;
import com.pengu.vanillatech.blocks.pim.SlotRule;
import com.pengu.vanillatech.net.PacketChangeSlot;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;

public class GuiConfigureRule extends GuiScreen
{
	public final ResourceLocation texture = new ResourceLocation(InfoVT.MOD_ID, "textures/gui/player_inventory_manager_edit_slot.png");
	public final GuiConfigureSlot parent;
	
	public GuiConfigureRule(GuiConfigureSlot parent, SlotRule rule)
	{
		this.parent = parent;
	}
	
	@Override
	public void initGui()
	{
		super.initGui();
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
		drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	
	@Override
	public boolean doesGuiPauseGame()
	{
		return false;
	}
	
	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException
	{
		if(keyCode == Keyboard.KEY_ESCAPE)
		{
			mc.displayGuiScreen(parent);
			HCNetwork.manager.sendToServer(new PacketChangeSlot(parent.cond.inv, parent.cond));
		}
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
	{
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}
	
	@Override
	protected void actionPerformed(GuiButton button) throws IOException
	{
	}
}