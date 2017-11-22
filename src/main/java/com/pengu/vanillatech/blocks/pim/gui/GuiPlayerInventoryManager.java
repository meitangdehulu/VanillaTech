package com.pengu.vanillatech.blocks.pim.gui;

import java.io.IOException;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.util.ResourceLocation;

import com.pengu.vanillatech.InfoVT;
import com.pengu.vanillatech.blocks.pim.TilePlayerInventoryManager;
import com.pengu.vanillatech.client.gui.inventory.GuiTexturedContainer;

public class GuiPlayerInventoryManager extends GuiTexturedContainer<ContainerPlayerInventoryManager>
{
	public GuiPlayerInventoryManager(TilePlayerInventoryManager inv, EntityPlayer player)
	{
		super(new ContainerPlayerInventoryManager(inv, player), new ResourceLocation(InfoVT.MOD_ID, "textures/gui/player_inventory_manager_main.png"));
		
		xSize = 194;
		ySize = 132;
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
		drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		renderHoveredToolTip(mouseX, mouseY);
		
		Slot slot;
		if((slot = getSlotUnderMouse()) != null && slot.inventory instanceof InventoryPlayer)
		{
			int len = slot.getStack().isEmpty() ? 0 : 17;
			drawHoveringText("Press 'SPACE' to edit", mouseX, mouseY - len);
		}
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{
		super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
		
		Slot slot = getSlotUnderMouse();
		if(slot != null)
		{
			int x = slot.xPos;
			int y = slot.yPos;
			mc.getTextureManager().bindTexture(texture);
			drawTexturedModalRect(guiLeft + x - 1, guiTop + y - 1, slot.inventory instanceof InventoryPlayer ? 0 : 18, ySize, 18, 18);
		}
	}
	
	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException
	{
		super.keyTyped(typedChar, keyCode);
		
		if(keyCode == Keyboard.KEY_SPACE)
		{
			Slot s = getSlotUnderMouse();
			if(s != null && s.inventory instanceof InventoryPlayer)
				mc.displayGuiScreen(new GuiConfigureSlot(this, s.getSlotIndex()));
//				mc.playerController.sendEnchantPacket(getContainer().windowId, s.slotNumber);
		}
	}
}