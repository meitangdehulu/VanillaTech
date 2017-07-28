package com.pengu.vanillatech.client.gui.inventory;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.pengu.hammercore.client.utils.RenderUtil;
import com.pengu.hammercore.color.ColorARGB;
import com.pengu.vanillatech.inventory.ContainerEnhancedFurnace;
import com.pengu.vanillatech.tile.TileEnhancedFurnace;

@SideOnly(Side.CLIENT)
public class GuiEnhancedFurnace extends GuiContainer
{
	private static final ResourceLocation FURNACE_GUI_TEXTURES = new ResourceLocation("textures/gui/container/furnace.png");
	private final InventoryPlayer playerInventory;
	private final TileEnhancedFurnace tileFurnace;
	
	public GuiEnhancedFurnace(InventoryPlayer playerInv, TileEnhancedFurnace furnaceInv)
	{
		super(new ContainerEnhancedFurnace(playerInv, furnaceInv));
		playerInventory = playerInv;
		tileFurnace = furnaceInv;
	}
	
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
		drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		renderHoveredToolTip(mouseX, mouseY);
	}
	
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		String s = I18n.translateToLocal(tileFurnace.getName());
		fontRenderer.drawString(s, xSize / 2 - fontRenderer.getStringWidth(s) / 2, 6, 4210752);
		fontRenderer.drawString(playerInventory.getDisplayName().getUnformattedText(), 8, ySize - 96 + 2, 4210752);
		
		String tip = null;
		int i = 0;
		for(Enchantment e : tileFurnace.enchantments.keySet())
		{
			int y = i * (fontRenderer.FONT_HEIGHT + 1) + 12;
			int ymx = (i + 1) * (fontRenderer.FONT_HEIGHT + 1) + 12;
			int lvl = 0;
			
			String ench = e.getTranslatedName(lvl = tileFurnace.enchantments.get(e) != null ? tileFurnace.enchantments.get(e).intValue() : 0);
			String sub = ench.substring(0, Math.min(ench.length(), 1)) + ".";
			sub = lvl == 1 && e.getMaxLevel() == 1 ? sub : sub + " " + I18n.translateToLocal("enchantment.level." + lvl);
			
			boolean sel = mouseX > guiLeft + 6 && mouseX < guiLeft + 6 + fontRenderer.getStringWidth(sub) && mouseY >= y + guiTop && mouseY < ymx + guiTop;
			
			sub = (sel ? TextFormatting.AQUA : TextFormatting.BLUE) + sub;
			
			ColorARGB.glColourRGB(0x666666);
			fontRenderer.drawString(sub, 6, y, 0x666666, true);
			if(sel)
				tip = TextFormatting.AQUA + ench;
			++i;
		}
		if(tip != null)
			drawHoveringText(tip, mouseX - guiLeft, mouseY - guiTop);
	}
	
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(FURNACE_GUI_TEXTURES);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
		
		if(tileFurnace.isBurning())
		{
			float k = getBurnLeftScaled(13);
			RenderUtil.drawTexturedModalRect(guiLeft + 56, guiTop + 36 + 12 - k, 176, 12 - k, 14, k + 1);
		}
		
		float l = getCookProgressScaled(24);
		RenderUtil.drawTexturedModalRect(guiLeft + 79, guiTop + 34, 176, 14, l + 1, 16);
	}
	
	private float getCookProgressScaled(int pixels)
	{
		float i = tileFurnace.cookTime;
		float j = tileFurnace.totalCookTime;
		return j != 0 && i != 0 ? i * pixels / j : 0;
	}
	
	private float getBurnLeftScaled(int pixels)
	{
		float i = tileFurnace.currentItemBurnTime;
		if(i == 0)
			i = 200;
		return tileFurnace.furnaceBurnTime * pixels / i;
	}
}