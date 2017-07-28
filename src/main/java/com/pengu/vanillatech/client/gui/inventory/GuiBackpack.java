package com.pengu.vanillatech.client.gui.inventory;

import java.util.UUID;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.pengu.vanillatech.init.ItemsVT;
import com.pengu.vanillatech.inventory.ContainerBackpack;

@SideOnly(Side.CLIENT)
public class GuiBackpack extends GuiContainer
{
	private static final ResourceLocation CHEST_GUI_TEXTURE = new ResourceLocation("textures/gui/container/generic_54.png");
	
	public GuiBackpack(UUID uuid)
	{
		super(new ContainerBackpack(Minecraft.getMinecraft().player, uuid));
		this.allowUserInput = false;
		int i = 222;
		int j = 114;
		this.ySize = 222;
	}
	
	/**
	 * Draws the screen and all the components in it.
	 */
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
		drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		renderHoveredToolTip(mouseX, mouseY);
	}
	
	/**
	 * Draw the foreground layer for the GuiContainer (everything in front of
	 * the items)
	 */
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		fontRenderer.drawString(I18n.translateToLocal(ItemsVT.BACKPACK.getUnlocalizedName() + ".name"), 8, 6, 4210752);
		fontRenderer.drawString(mc.player.inventory.getDisplayName().getUnformattedText(), 8, ySize - 96 + 2, 4210752);
	}
	
	/**
	 * Draws the background layer of this container (behind the items).
	 */
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(CHEST_GUI_TEXTURE);
		int i = (width - xSize) / 2;
		int j = (height - ySize) / 2;
		drawTexturedModalRect(i, j, 0, 0, xSize, 6 * 18 + 17);
		drawTexturedModalRect(i, j + 6 * 18 + 17, 0, 126, xSize, 96);
	}
}