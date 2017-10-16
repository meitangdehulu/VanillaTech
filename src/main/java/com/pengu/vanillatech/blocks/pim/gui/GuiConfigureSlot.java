package com.pengu.vanillatech.blocks.pim.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import org.lwjgl.input.Keyboard;

import com.pengu.hammercore.net.HCNetwork;
import com.pengu.vanillatech.Info;
import com.pengu.vanillatech.blocks.pim.NBTItemComparator;
import com.pengu.vanillatech.blocks.pim.SlotCondition;
import com.pengu.vanillatech.blocks.pim.SlotRule;
import com.pengu.vanillatech.blocks.pim.TilePlayerInventoryManager;
import com.pengu.vanillatech.net.PacketChangeSlot;
import com.pengu.vanillatech.utils.AtomicTuple;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.util.ResourceLocation;

public class GuiConfigureSlot extends GuiScreen
{
	public final GuiPlayerInventoryManager parent;
	public final ResourceLocation texture = new ResourceLocation(Info.MOD_ID, "textures/gui/player_inventory_manager_edit_slot.png");
	public final int slot;
	public SlotCondition cond;
	
	public GuiConfigureSlot(GuiPlayerInventoryManager parent, int slot)
	{
		this.parent = parent;
		this.slot = slot;
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
		drawDefaultBackground();
		
		RenderHelper.disableStandardItemLighting();
		
		mc.getTextureManager().bindTexture(texture);
		int guiLeft = (width - 116) / 2;
		int guiTop = (height - 122) / 2;
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, 116, 122);
		
		Random rand = new Random();
		
		int y = guiTop + 4;
		int id = 2;
		for(AtomicTuple<NBTItemComparator, SlotRule> rule : cond.rules)
		{
			rand.setSeed(id ^ 9 + cond.ruleSlot);
			
			if(Keyboard.isKeyDown(Keyboard.KEY_DELETE) && mouseX >= guiLeft && mouseX < guiLeft + 116 && mouseY >= y - 1 && mouseY < y + 21)
				drawRect(guiLeft + 4, y - 1, guiLeft + 112, y + 21, 0xAAFF0000);
			else
				drawRect(guiLeft + 4, y - 1, guiLeft + 112, y + 21, (255 << 24) | rand.nextInt(0xFFFFFF));
			
			id += 2;
			y += 22;
		}
		
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	
	@Override
	public void initGui()
	{
		super.initGui();
		
		TilePlayerInventoryManager inv = parent.getContainer().inv;
		cond = inv.rules.getCondition(slot);
		
		int guiLeft = (width - 116) / 2;
		int guiTop = (height - 122) / 2;
		
		GuiButton addRule;
		addButton(addRule = new GuiButton(0, guiLeft + 4, guiTop + 97, 50, 20, "Add Rule"));
		addButton(new GuiButton(1, guiLeft + 62, guiTop + 97, 50, 20, "Save"));
		
		addRule.enabled = cond.rules.size() < 4;
		
		int y = 0;
		int id = 2;
		for(AtomicTuple<NBTItemComparator, SlotRule> rule : cond.rules)
		{
			addButton(new GuiButton(id, guiLeft + 4, guiTop + y + 4, 50, 20, "Condition"));
			addButton(new GuiButton(id + 1, guiLeft + 62, guiTop + y + 4, 50, 20, "Rule"));
			
			id += 2;
			y += 22;
		}
	}
	
	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException
	{
		if(keyCode == Keyboard.KEY_ESCAPE)
		{
			mc.displayGuiScreen(parent);
			HCNetwork.manager.sendToServer(new PacketChangeSlot(cond.inv, cond));
		}
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
	{
		int guiLeft = (width - 116) / 2;
		int guiTop = (height - 122) / 2;
		
		int y = guiTop + 4;
		int id = 2;
		for(AtomicTuple<NBTItemComparator, SlotRule> rule : new ArrayList<>(cond.rules))
		{
			if(Keyboard.isKeyDown(Keyboard.KEY_DELETE) && mouseX >= guiLeft && mouseX < guiLeft + 116 && mouseY >= y - 1 && mouseY < y + 21)
			{
				SlotCondition c = cond;
				cond.rules.remove(rule);
				cond.inv.rules.setCondition(cond.ruleSlot, cond);
				setWorldAndResolution(mc, width, height);
				cond = c;
				HCNetwork.manager.sendToServer(new PacketChangeSlot(cond.inv, cond));
				return;
			}
			
			id += 2;
			y += 22;
		}
		
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}
	
	@Override
	protected void actionPerformed(GuiButton button) throws IOException
	{
		if(button.id == 0)
		{
			button.enabled = cond.rules.size() < 4;
			cond.rules.add(new AtomicTuple<NBTItemComparator, SlotRule>(new NBTItemComparator(), new SlotRule(cond.inv, slot)));
			cond.inv.rules.setCondition(cond.ruleSlot, cond);
			setWorldAndResolution(mc, width, height);
		}
		
		if(button.id == 1)
			HCNetwork.manager.sendToServer(new PacketChangeSlot(cond.inv, cond));
	}
	
	@Override
	public boolean doesGuiPauseGame()
	{
		return false;
	}
}