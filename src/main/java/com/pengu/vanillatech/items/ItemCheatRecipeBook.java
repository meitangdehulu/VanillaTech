package com.pengu.vanillatech.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

import com.pengu.hammercore.HammerCore;

public class ItemCheatRecipeBook extends Item
{
	public ItemCheatRecipeBook()
    {
		setUnlocalizedName("creative_recipe_book");
		setMaxStackSize(1);
    }
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
	{
		playerIn.unlockRecipes(GameRegistry.findRegistry(IRecipe.class).getValues());
		
		if(!worldIn.isRemote)
			HammerCore.audioProxy.playSoundAt(worldIn, "ui.toast.challenge_complete", playerIn.getPosition(), 2, 1, SoundCategory.PLAYERS);
	    return super.onItemRightClick(worldIn, playerIn, handIn);
	}
}