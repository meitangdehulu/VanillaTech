package com.pengu.vanillatech.client.gui.inventory;

import java.util.UUID;

import com.pengu.vanillatech.init.ItemsVT;
import com.pengu.vanillatech.inventory.ContainerBackpack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiBackpack extends GuiContainer
{
	private static final ResourceLocation CHEST_GUI_TEXTURE = new ResourceLocation("textures/gui/container/generic_54.png");
	private static final ResourceLocation SHULKER_GUI_TEXTURE = new ResourceLocation("textures/gui/container/shulker_box.png");
	
	public boolean ender;
	
	public GuiBackpack(UUID uuid, boolean ender)
	{
		super(new ContainerBackpack(Minecraft.getMinecraft().player, uuid, ender));
		this.allowUserInput = false;
		if(!ender)
			this.ySize = 222;
		this.ender = ender;
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
		drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		renderHoveredToolTip(mouseX, mouseY);
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		fontRenderer.drawString(I18n.format(ItemsVT.BACKPACK.getUnlocalizedName() + ".name"), 8, 6, 4210752);
		fontRenderer.drawString(mc.player.inventory.getDisplayName().getUnformattedText(), 8, ySize - 96 + 2, 4210752);
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(ender ? SHULKER_GUI_TEXTURE : CHEST_GUI_TEXTURE);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, 6 * 18 + 17);
		drawTexturedModalRect(guiLeft, guiTop + 6 * 18 + 17, 0, 126, xSize, 96);
	}
}