package com.pengu.vanillatech.tile;

import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.SlotFurnaceFuel;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.oredict.OreDictionary;

import com.google.common.collect.Maps;
import com.pengu.hammercore.common.inventory.InventoryDummy;
import com.pengu.hammercore.tile.TileSyncableTickable;
import com.pengu.vanillatech.blocks.BlockEnhancedFurnace;
import com.pengu.vanillatech.client.gui.inventory.GuiEnhancedFurnace;
import com.pengu.vanillatech.init.BlocksVT;
import com.pengu.vanillatech.inventory.ContainerEnhancedFurnace;

public class TileEnhancedFurnace extends TileSyncableTickable implements ISidedInventory
{
	private static final int[] SLOTS_TOP = new int[] { 0 };
	private static final int[] SLOTS_BOTTOM = new int[] { 2, 1 };
	private static final int[] SLOTS_SIDES = new int[] { 1 };
	
	public InventoryDummy inventory = new InventoryDummy(3);
	public Map<Enchantment, Integer> enchantments;
	public String furnaceCustomName;
	public int furnaceBurnTime;
	public int currentItemBurnTime;
	public int cookTime;
	public int totalCookTime;
	public boolean keepInventory = false;
	public boolean isActive = false;
	public EnumFacing face;
	public boolean enchanted = false;
	
	public TileEnhancedFurnace(ItemStack stack)
	{
		enchantments = EnchantmentHelper.getEnchantments(stack);
	}
	
	public TileEnhancedFurnace()
	{
		enchantments = Maps.<Enchantment, Integer> newLinkedHashMap();
	}
	
	@Override
	public boolean hasGui()
	{
		return true;
	}
	
	@Override
	public Object getClientGuiElement(EntityPlayer player)
	{
		return new GuiEnhancedFurnace(player.inventory, this);
	}
	
	public Object getServerGuiElement(EntityPlayer player)
	{
		return new ContainerEnhancedFurnace(player.inventory, this);
	}
	
	@Override
	public ITextComponent getDisplayName()
	{
		return new TextComponentTranslation(getName());
	}
	
	@Override
	public void tick()
	{
		enchanted = !enchantments.isEmpty();
		
		boolean flag = isBurning();
		boolean flag1 = false;
		
		IBlockState state = world.getBlockState(pos);
		if(state.getBlock() == BlocksVT.ENHANCED_FURNACE)
		{
			face = state.getValue(BlockEnhancedFurnace.FACING);
			flag = state.getValue(BlockEnhancedFurnace.ENABLED);
		}
		
		if((isActive = isBurning()))
			--furnaceBurnTime;
		
		if(!world.isRemote)
		{
			ItemStack itemstack = inventory.getStackInSlot(1);
			
			if(isBurning() || !itemstack.isEmpty() && !((ItemStack) inventory.getStackInSlot(0)).isEmpty())
			{
				if(!isBurning() && canSmelt())
				{
					furnaceBurnTime = TileEntityFurnace.getItemBurnTime(itemstack);
					currentItemBurnTime = furnaceBurnTime;
					
					int unbreaking = enchantments.get(Enchantments.UNBREAKING) != null ? enchantments.get(Enchantments.UNBREAKING).intValue() : 0;
					float burnMod = 1;
					if(unbreaking > 0)
						burnMod = (unbreaking / (float) Enchantments.UNBREAKING.getMaxLevel()) * 2F;
					furnaceBurnTime *= burnMod;
					currentItemBurnTime *= burnMod;
					
					if(isBurning())
					{
						flag1 = true;
						
						if(!itemstack.isEmpty())
						{
							Item item = itemstack.getItem();
							itemstack.shrink(1);
							
							if(itemstack.isEmpty())
							{
								ItemStack item1 = item.getContainerItem(itemstack);
								inventory.setInventorySlotContents(1, item1);
							}
						}
					}
				}
				
				if(isBurning() && canSmelt())
				{
					++cookTime;
					
					totalCookTime = getCookTime(inventory.getStackInSlot(0));
					
					if(cookTime >= totalCookTime)
					{
						cookTime = 0;
						smeltItem();
					}
					
					flag1 = true;
				} else
					cookTime = 0;
			} else if(!isBurning() && cookTime > 0)
				cookTime = MathHelper.clamp(cookTime - 2, 0, totalCookTime);
		}
		
		if(flag != isBurning())
		{
			flag1 = true;
			BlockEnhancedFurnace.setState(isBurning(), world, pos);
		}
		
		if(flag1 || atTickRate(5))
			sendChangesToNearby();
	}
	
	public boolean isBurning()
	{
		return this.furnaceBurnTime > 0;
	}
	
	public int getCookTime(ItemStack stack)
	{
		int efficiency = enchantments.get(Enchantments.EFFICIENCY) != null ? enchantments.get(Enchantments.EFFICIENCY).intValue() : 0;
		int maxEff = Enchantments.EFFICIENCY.getMaxLevel();
		int tickReduction = MathHelper.clamp((int) ((efficiency / (double) maxEff) * 180D), 0, 190);
		return 200 - tickReduction;
	}
	
	private boolean canSmelt()
	{
		if(inventory.getStackInSlot(0).isEmpty())
			return false;
		else
		{
			ItemStack itemstack = FurnaceRecipes.instance().getSmeltingResult(inventory.getStackInSlot(0));
			
			if(itemstack.isEmpty())
				return false;
			else
			{
				ItemStack itemstack1 = inventory.getStackInSlot(2);
				
				if(itemstack1.isEmpty())
					return true;
				else if(!itemstack1.isItemEqual(itemstack))
					return false;
				else if(itemstack1.getCount() + itemstack.getCount() <= inventory.getInventoryStackLimit() && itemstack1.getCount() + itemstack.getCount() <= itemstack1.getMaxStackSize())
					return true;
				else
					return itemstack1.getCount() + itemstack.getCount() <= itemstack.getMaxStackSize();
			}
		}
	}
	
	public void smeltItem()
	{
		if(canSmelt())
		{
			ItemStack itemstack = inventory.getStackInSlot(0);
			ItemStack itemstack1 = FurnaceRecipes.instance().getSmeltingResult(itemstack).copy();
			ItemStack itemstack2 = inventory.getStackInSlot(2);
			
			int maxGrow = Math.min(getInventoryStackLimit(), itemstack2.getMaxStackSize()) - itemstack1.getCount();
			int grow = itemstack1.getCount();
			int[] ids = OreDictionary.getOreIDs(itemstack);
			boolean isOre = false;
			for(int i : ids)
				if(OreDictionary.getOreName(i).startsWith("ore"))
				{
					isOre = true;
					break;
				}
			int luck = enchantments.get(Enchantments.FORTUNE) != null ? enchantments.get(Enchantments.FORTUNE).intValue() : 0;
			
			if(isOre && luck > 0)
				grow += rand.nextInt(luck + 1);
			
			itemstack1.setCount(Math.min(maxGrow, grow));
			
			if(itemstack2.isEmpty())
				inventory.setInventorySlotContents(2, itemstack1);
			else if(itemstack2.getItem() == itemstack1.getItem())
				itemstack2.grow(itemstack1.getCount());
			if(itemstack.getItem() == Item.getItemFromBlock(Blocks.SPONGE) && itemstack.getMetadata() == 1 && !((ItemStack) inventory.getStackInSlot(1)).isEmpty() && ((ItemStack) inventory.getStackInSlot(1)).getItem() == Items.BUCKET)
				inventory.setInventorySlotContents(1, new ItemStack(Items.WATER_BUCKET));
			itemstack.shrink(1);
		}
	}
	
	@Override
	public void writeNBT(NBTTagCompound nbt)
	{
		NBTTagList nbttaglist = new NBTTagList();
		for(Entry<Enchantment, Integer> entry : enchantments.entrySet())
		{
			Enchantment enchantment = entry.getKey();
			
			if(enchantment != null)
			{
				int i = ((Integer) entry.getValue()).intValue();
				NBTTagCompound nbttagcompound = new NBTTagCompound();
				nbttagcompound.setShort("id", (short) Enchantment.getEnchantmentID(enchantment));
				nbttagcompound.setShort("lvl", (short) i);
				nbttaglist.appendTag(nbttagcompound);
			}
		}
		nbt.setTag("Ench", nbttaglist);
		
		nbt.setInteger("BurnTime", furnaceBurnTime);
		nbt.setInteger("CookTime", cookTime);
		nbt.setInteger("CookTimeTotal", totalCookTime);
		nbt.setInteger("CurrentItemBurnItem", currentItemBurnTime);
		
		if(hasCustomName())
			nbt.setString("CustomName", furnaceCustomName);
	}
	
	@Override
	public void readNBT(NBTTagCompound nbt)
	{
		enchantments = Maps.<Enchantment, Integer> newLinkedHashMap();
		NBTTagList nbttaglist = nbt.getTagList("Ench", NBT.TAG_COMPOUND);
		
		for(int i = 0; i < nbttaglist.tagCount(); ++i)
		{
			NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
			Enchantment enchantment = Enchantment.getEnchantmentByID(nbttagcompound.getShort("id"));
			int j = nbttagcompound.getShort("lvl");
			enchantments.put(enchantment, Integer.valueOf(j));
		}
		
		furnaceBurnTime = nbt.getInteger("BurnTime");
		cookTime = nbt.getInteger("CookTime");
		totalCookTime = nbt.getInteger("CookTimeTotal");
		currentItemBurnTime = nbt.getInteger("CurrentItemBurnItem");
		
		if(nbt.hasKey("CustomName", NBT.TAG_STRING))
			furnaceCustomName = nbt.getString("CustomName");
	}
	
	public ItemStack getDrop()
	{
		ItemStack furnace = new ItemStack(BlocksVT.ENHANCED_FURNACE);
		EnchantmentHelper.setEnchantments(enchantments, furnace);
		if(hasCustomName())
			furnace.setStackDisplayName(getName());
		return furnace;
	}
	
	public String getName()
	{
		return hasCustomName() ? furnaceCustomName : BlocksVT.ENHANCED_FURNACE.getLocalizedName();
	}
	
	public boolean hasCustomName()
	{
		return furnaceCustomName != null && !furnaceCustomName.isEmpty();
	}
	
	public void setCustomInventoryName(String name)
	{
		furnaceCustomName = name;
	}
	
	@Override
	public int getSizeInventory()
	{
		return inventory.getSizeInventory();
	}
	
	@Override
	public boolean isEmpty()
	{
		return inventory.isEmpty();
	}
	
	@Override
	public ItemStack getStackInSlot(int index)
	{
		return inventory.getStackInSlot(index);
	}
	
	@Override
	public ItemStack decrStackSize(int index, int count)
	{
		return inventory.decrStackSize(index, count);
	}
	
	@Override
	public ItemStack removeStackFromSlot(int index)
	{
		return inventory.removeStackFromSlot(index);
	}
	
	@Override
	public void setInventorySlotContents(int index, ItemStack stack)
	{
		inventory.setInventorySlotContents(index, stack);
	}
	
	@Override
	public int getInventoryStackLimit()
	{
		return inventory.getInventoryStackLimit();
	}
	
	@Override
	public boolean isUsableByPlayer(EntityPlayer player)
	{
		return inventory.isUsableByPlayer(player, pos);
	}
	
	@Override
	public void openInventory(EntityPlayer player)
	{
	}
	
	@Override
	public void closeInventory(EntityPlayer player)
	{
	}
	
	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack)
	{
		if(index == 2)
			return false;
		else if(index != 1)
			return true;
		else
		{
			ItemStack itemstack = inventory.getStackInSlot(1);
			return TileEntityFurnace.isItemFuel(stack) || SlotFurnaceFuel.isBucket(stack) && itemstack.getItem() != Items.BUCKET;
		}
	}
	
	public int[] getSlotsForFace(EnumFacing side)
	{
		if(side == EnumFacing.DOWN)
		{
			return SLOTS_BOTTOM;
		} else
		{
			return side == EnumFacing.UP ? SLOTS_TOP : SLOTS_SIDES;
		}
	}
	
	public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction)
	{
		return this.isItemValidForSlot(index, itemStackIn);
	}
	
	public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction)
	{
		if(direction == EnumFacing.DOWN && index == 1)
		{
			Item item = stack.getItem();
			if(item != Items.WATER_BUCKET && item != Items.BUCKET)
				return false;
		}
		
		return true;
	}
	
	@Override
	public int getField(int id)
	{
		return 0;
	}
	
	@Override
	public void setField(int id, int value)
	{
	}
	
	@Override
	public int getFieldCount()
	{
		return 0;
	}
	
	@Override
	public void clear()
	{
		inventory.clear();
	}
}