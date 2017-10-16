package com.pengu.vanillatech.blocks.pim;

import java.util.Objects;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.oredict.OreDictionary;

public class NBTItemComparator
{
	public NBTItemComparator child;
	
	public ItemStack matchStack = ItemStack.EMPTY;
	
	/**
	 * -2 = always TRUE -1 = always FALSE 0 = item equality 1 = NBT equality 2 =
	 * OreDict equality 3 = count equality 4 = damage equality
	 */
	public int matchMode = 0;
	
	/**
	 * Dummy number to be compared (ex. count/damage)
	 */
	public int matchOptNumb = 0;
	
/**
	 * 0 = "=="
	 * 1 = ">"
	 * 2 = "<"
	 * 3 = ">="
	 * 4 = "<="
	 * 5 = "!="
	 */
	public int numEqualityMode = 0;
	
	public NBTItemComparator(NBTItemComparator child)
	{
		this.child = child;
	}
	
	public NBTItemComparator()
	{
	}
	
	public NBTItemComparator(NBTTagCompound nbt)
	{
		readNBT(nbt);
	}
	
	public boolean compare(ItemStack val)
	{
		if(child != null && !child.compare(val))
			return false;
		
		if(matchMode == -2)
			return true;
		if(matchMode == -1)
			return false;
		
		if(matchMode == 0)
		{
			Item our = matchStack.isEmpty() ? null : matchStack.getItem();
			Item vit = val.isEmpty() ? null : val.getItem();
			if(numEqualityMode == 0 && our == vit)
				return true;
			if(numEqualityMode == 5 && our != vit)
				return true;
		}
		
		if(matchMode == 1)
		{
			NBTTagCompound our = matchStack.isEmpty() ? null : matchStack.getTagCompound();
			NBTTagCompound vit = val.isEmpty() ? null : val.getTagCompound();
			if(numEqualityMode == 0 && Objects.equals(our, vit))
				return true;
			if(numEqualityMode == 5 && !Objects.equals(our, vit))
				return true;
		}
		
		if(matchMode == 2)
		{
			int[] our = matchStack.isEmpty() ? null : OreDictionary.getOreIDs(matchStack);
			int[] vit = val.isEmpty() ? null : OreDictionary.getOreIDs(val);
			
			boolean oreEqual = our == vit;
			
			if(!oreEqual && our != null && vit != null)
				b: for(int i : our)
					for(int j : vit)
						if(i == j)
						{
							oreEqual = true;
							break b;
						}
			
			if(numEqualityMode == 0 && oreEqual)
				return true;
			if(numEqualityMode == 5 && !oreEqual)
				return true;
		}
		
		if(matchMode == 3 || matchMode == 4)
		{
			int our = matchOptNumb;
			int vit = matchMode == 3 ? val.getCount() : val.getItemDamage();
			
			if(matchMode == 0 && our == vit)
				return true;
			if(matchMode == 1 && our > vit)
				return true;
			if(matchMode == 2 && our < vit)
				return true;
			if(matchMode == 3 && our >= vit)
				return true;
			if(matchMode == 4 && our <= vit)
				return true;
			if(matchMode == 5 && our != vit)
				return true;
		}
		
		return false;
	}
	
	public int treeLength()
	{
		int l = 0;
		
		NBTItemComparator compar = this;
		
		while(compar != null)
		{
			l++;
			compar = compar.child;
		}
		
		return l;
	}
	
	public NBTTagCompound writeNBT(NBTTagCompound nbt)
	{
		if(child != null)
			nbt.setTag("Child", child.writeNBT(new NBTTagCompound()));
		if(matchMode == 3 || matchMode == 4)
			nbt.setInteger("MatchNumber", matchOptNumb);
		else
			nbt.setTag("Stack", matchStack.writeToNBT(new NBTTagCompound()));
		nbt.setInteger("MatchMode", matchMode);
		nbt.setInteger("Equality", numEqualityMode);
		return nbt;
	}
	
	public void readNBT(NBTTagCompound nbt)
	{
		if(nbt.hasKey("Child", NBT.TAG_COMPOUND))
			child = new NBTItemComparator(nbt.getCompoundTag("Child"));
		matchStack = new ItemStack(nbt.getCompoundTag("Stack"));
		matchMode = nbt.getInteger("MatchMode");
		numEqualityMode = nbt.getInteger("Equality");
	}
	
	public static boolean matches(NBTItemComparator comparator, ItemStack val)
	{
		if(comparator == null)
			return true;
		return comparator.compare(val);
	}
}