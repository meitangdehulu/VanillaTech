package com.pengu.vanillatech;

import java.util.Arrays;
import java.util.function.Supplier;

import com.pengu.hammercore.common.SimpleRegistration;
import com.pengu.hammercore.common.match.item.ItemListContainerHelper;
import com.pengu.hammercore.common.match.item.ItemMatchParams;
import com.pengu.hammercore.intent.IntentManager;
import com.pengu.hammercore.recipeAPI.BrewingRecipe;
import com.pengu.hammercore.world.gen.WorldRetroGen;
import com.pengu.vanillatech.api.EnhancerRegistry;
import com.pengu.vanillatech.init.BlocksVT;
import com.pengu.vanillatech.init.EnchantmentsVT;
import com.pengu.vanillatech.init.GuiCallbacksVT;
import com.pengu.vanillatech.init.ItemsVT;
import com.pengu.vanillatech.init.PotionsVT;
import com.pengu.vanillatech.init.SoundsVT;
import com.pengu.vanillatech.init.VTLog;
import com.pengu.vanillatech.proxy.CommonProxy;
import com.pengu.vanillatech.worldgen.WorldGenNetherStarOre;
import com.pengu.vanillatech.worldgen.WorldGenOverworldGlowstone;
import com.pengu.vanillatech.worldgen.WorldGenOverworldQuartz;
import com.pengu.vanillatech.worldgen.WorldGenRedRock;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLConstructionEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod(modid = Info.MOD_ID, name = Info.MOD_NAME, version = Info.MOD_VERSION, dependencies = "required-after:hammercore", guiFactory = "com.pengu.vanillatech.cfg.ConfigFactoryVT")
public class VanillaTech
{
	public static final CreativeTabs tab = new CreativeTabVT(false);
	
	@SidedProxy(clientSide = Info.PROXY_BASE + "ClientProxy", serverSide = Info.PROXY_BASE + "CommonProxy")
	public static CommonProxy proxy;
	
	@Instance
	public static VanillaTech instance;
	
	@EventHandler
	public void construct(FMLConstructionEvent evt)
	{
		IntentManager.registerIntentHandler(Info.MOD_ID, Object.class, (name, obj) -> true);
		
		/** Allows to access specific item from Vanilla Tech. */
		IntentManager.registerIntentHandler(Info.MOD_ID + ":item", String.class, (name, obj) -> GameRegistry.findRegistry(Item.class).getValue(new ResourceLocation(Info.MOD_ID, obj)));
		
		/** Allows to access specific block from Vanilla Tech. */
		IntentManager.registerIntentHandler(Info.MOD_ID + ":block", String.class, (name, obj) -> GameRegistry.findRegistry(Block.class).getValue(new ResourceLocation(Info.MOD_ID, obj)));
		
		new EnhancerRegistry();
	}
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent evt)
	{
		SimpleRegistration.registerFieldBlocksFrom(BlocksVT.class, Info.MOD_ID, tab);
		SimpleRegistration.registerFieldItemsFrom(ItemsVT.class, Info.MOD_ID, tab);
		GuiCallbacksVT.register();
		proxy.preInit();
		
		IntentManager.sendIntent(Info.MOD_ID + ":add_enhancer_fuel", new Tuple(new ItemStack(Items.NETHER_STAR), 800));
		IntentManager.sendIntent(Info.MOD_ID + ":add_enhancer_fuel", new Tuple(new ItemStack(ItemsVT.NETHERSTAR_SHARD), 80));
		
		Object added = IntentManager.sendIntent("simplequarry:quarry_blacklist", (Supplier) (() -> BlocksVT.NETHERSTAR_ORE.getDefaultState()));
		
		if(added instanceof Boolean)
			VTLog.info("Detected SimpleQuarry. " + (added == Boolean.TRUE ? "" : "Not ") + "Added Nether Star Ore to blacklist.");
		else
			VTLog.info("SimpleQuarry Not Detected!");
		
		evt.getModMetadata().logoFile = "assets/vanillatech/textures/logo.png";
	}
	
	@EventHandler
	public void init(FMLInitializationEvent evt)
	{
		proxy.init();
		EnchantmentsVT.init();
		PotionsVT.init();
		
		WorldRetroGen.addWorldFeature(new WorldGenRedRock());
		WorldRetroGen.addWorldFeature(new WorldGenOverworldQuartz());
		WorldRetroGen.addWorldFeature(new WorldGenOverworldGlowstone());
		WorldRetroGen.addWorldFeature(new WorldGenNetherStarOre());
		
		FurnaceRecipes.instance().addSmeltingRecipe(new ItemStack(BlocksVT.RED_COBBLESTONE), new ItemStack(BlocksVT.RED_STONE), .1F);
		FurnaceRecipes.instance().addSmeltingRecipe(new ItemStack(BlocksVT.QUARTZ_ORE), new ItemStack(Items.QUARTZ), 1F);
		
		SoundsVT.clinit();
		
		// addBrewing(PotionUtils.addPotionToItemStack(new
		// ItemStack(Items.POTIONITEM), PotionTypes.WATER), new
		// ItemStack(Items.CHORUS_FRUIT), PotionUtils.addPotionToItemStack(new
		// ItemStack(Items.POTIONITEM), PotionsVT.TYPE_LEVITATION_SHORT));
		// addBrewing(PotionUtils.addPotionToItemStack(new
		// ItemStack(Items.POTIONITEM), PotionsVT.TYPE_LEVITATION_SHORT), new
		// ItemStack(Items.REDSTONE), PotionUtils.addPotionToItemStack(new
		// ItemStack(Items.POTIONITEM), PotionsVT.TYPE_LEVITATION_LONG));
		// addBrewing(PotionUtils.addPotionToItemStack(new
		// ItemStack(Items.SPLASH_POTION), PotionTypes.WATER), new
		// ItemStack(Items.CHORUS_FRUIT), PotionUtils.addPotionToItemStack(new
		// ItemStack(Items.SPLASH_POTION), PotionsVT.TYPE_LEVITATION_SHORT));
		// addBrewing(PotionUtils.addPotionToItemStack(new
		// ItemStack(Items.SPLASH_POTION), PotionsVT.TYPE_LEVITATION_SHORT), new
		// ItemStack(Items.REDSTONE), PotionUtils.addPotionToItemStack(new
		// ItemStack(Items.SPLASH_POTION), PotionsVT.TYPE_LEVITATION_LONG));
		// addBrewing(PotionUtils.addPotionToItemStack(new
		// ItemStack(Items.POTIONITEM), PotionsVT.TYPE_LEVITATION_LONG), new
		// ItemStack(Items.GUNPOWDER), PotionUtils.addPotionToItemStack(new
		// ItemStack(Items.SPLASH_POTION), PotionsVT.TYPE_LEVITATION_LONG));
		// addBrewing(PotionUtils.addPotionToItemStack(new
		// ItemStack(Items.POTIONITEM), PotionsVT.TYPE_LEVITATION_SHORT), new
		// ItemStack(Items.GUNPOWDER), PotionUtils.addPotionToItemStack(new
		// ItemStack(Items.SPLASH_POTION), PotionsVT.TYPE_LEVITATION_SHORT));
		// addBrewing(PotionUtils.addPotionToItemStack(new
		// ItemStack(Items.LINGERING_POTION), PotionTypes.WATER), new
		// ItemStack(Items.CHORUS_FRUIT), PotionUtils.addPotionToItemStack(new
		// ItemStack(Items.LINGERING_POTION), PotionsVT.TYPE_LEVITATION_SHORT));
		// addBrewing(PotionUtils.addPotionToItemStack(new
		// ItemStack(Items.LINGERING_POTION), PotionsVT.TYPE_LEVITATION_SHORT),
		// new ItemStack(Items.REDSTONE), PotionUtils.addPotionToItemStack(new
		// ItemStack(Items.LINGERING_POTION), PotionsVT.TYPE_LEVITATION_LONG));
		// addBrewing(PotionUtils.addPotionToItemStack(new
		// ItemStack(Items.SPLASH_POTION), PotionsVT.TYPE_LEVITATION_LONG), new
		// ItemStack(Items.DRAGON_BREATH), PotionUtils.addPotionToItemStack(new
		// ItemStack(Items.LINGERING_POTION), PotionsVT.TYPE_LEVITATION_LONG));
		// addBrewing(PotionUtils.addPotionToItemStack(new
		// ItemStack(Items.SPLASH_POTION), PotionsVT.TYPE_LEVITATION_SHORT), new
		// ItemStack(Items.DRAGON_BREATH), PotionUtils.addPotionToItemStack(new
		// ItemStack(Items.LINGERING_POTION), PotionsVT.TYPE_LEVITATION_SHORT));
	}
	
	private void addBrewing(ItemStack in, ItemStack ing, ItemStack out)
	{
		ItemMatchParams pars = new ItemMatchParams();
		BrewingRecipe.INSTANCE.addRecipe(ItemListContainerHelper.stackPredicate(Arrays.asList(in), pars), ItemListContainerHelper.stackPredicate(Arrays.asList(ing), pars), out);
	}
}