package com.pengu.vanillatech.client.gui.inventory;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;

public class GuiTexturedContainer<CONTAINER extends Container> extends GuiContainer
{
	public ResourceLocation texture;
	
	public GuiTexturedContainer(CONTAINER c, ResourceLocation tex)
	{
		super(c);
		texture = tex;
	}
	
	public void setContainer(CONTAINER c)
	{
		inventorySlots = c;
	}
	
	public CONTAINER getContainer()
	{
		return (CONTAINER) inventorySlots;
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
		GlStateManager.color(1, 1, 1, 1);
		drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		renderHoveredToolTip(mouseX, mouseY);
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{
		GlStateManager.color(1, 1, 1, 1);
		
		mc.getTextureManager().bindTexture(texture);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
	}
}