package com.pengu.vanillatech.proxy;

import java.util.Random;
import java.util.function.IntSupplier;

import com.pengu.hammercore.client.render.item.ItemRenderingHandler;
import com.pengu.hammercore.client.render.item.iItemRender;
import com.pengu.hammercore.client.render.tesr.TESR;
import com.pengu.vanillatech.Info;
import com.pengu.vanillatech.blocks.BlockNetherstarOre;
import com.pengu.vanillatech.cfg.ConfigsVT;
import com.pengu.vanillatech.client.ClientTicker;
import com.pengu.vanillatech.client.particle.ParticleGlowingBlockOverlay;
import com.pengu.vanillatech.client.render.generic.RenderZapLayered;
import com.pengu.vanillatech.client.render.item.RenderBlockAnriatphyte;
import com.pengu.vanillatech.client.render.item.RenderBlockUnstableMetal;
import com.pengu.vanillatech.client.render.item.RenderGlowstoneOre;
import com.pengu.vanillatech.client.render.item.RenderItemAnriatphyteCrystal;
import com.pengu.vanillatech.client.render.item.RenderItemUnstableMetalIngot;
import com.pengu.vanillatech.client.render.item.RenderNetherstarOre;
import com.pengu.vanillatech.client.render.item.RenderRepeater;
import com.pengu.vanillatech.client.render.tesr.TESREnhancedFurnace;
import com.pengu.vanillatech.client.render.tesr.TESRFisher;
import com.pengu.vanillatech.client.render.tesr.TESRRedstoneTeleporter;
import com.pengu.vanillatech.init.BlocksVT;
import com.pengu.vanillatech.init.ItemsVT;
import com.pengu.vanillatech.tile.TileEnhancedFurnace;
import com.pengu.vanillatech.tile.TileFisher;
import com.pengu.vanillatech.tile.TileRedstoneTeleporter;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class ClientProxy extends CommonProxy
{
	@Override
	public void preInit()
	{
		MinecraftForge.EVENT_BUS.register(new ClientTicker());
		MinecraftForge.EVENT_BUS.register(new RenderZapLayered());
		
		bind(new TESRRedstoneTeleporter(), BlocksVT.REDSTONE_TELEPORTER, TileRedstoneTeleporter.class, true);
		bind(new RenderRepeater(), Items.REPEATER, true);
	}
	
	@Override
	public void init()
	{
		ItemRenderingHandler.INSTANCE.appendItemRender(Item.getItemFromBlock(BlocksVT.GLOWSTONE_ORE), new RenderGlowstoneOre());
		ItemRenderingHandler.INSTANCE.appendItemRender(Item.getItemFromBlock(BlocksVT.NETHERSTAR_ORE), new RenderNetherstarOre());
		ItemRenderingHandler.INSTANCE.appendItemRender(Item.getItemFromBlock(BlocksVT.UNSTABLE_METAL_BLOCK), new RenderBlockUnstableMetal());
		ItemRenderingHandler.INSTANCE.appendItemRender(ItemsVT.UNSTABLE_METAL_INGOT, new RenderItemUnstableMetalIngot());
		ItemRenderingHandler.INSTANCE.appendItemRender(Item.getItemFromBlock(BlocksVT.ANRIATPHYTE_BLOCK), new RenderBlockAnriatphyte());
		ItemRenderingHandler.INSTANCE.appendItemRender(ItemsVT.ANRIATPHYTE_CRYSTAL, new RenderItemAnriatphyteCrystal());
		
		ClientRegistry.bindTileEntitySpecialRenderer(TileEnhancedFurnace.class, new TESREnhancedFurnace());
		ClientRegistry.bindTileEntitySpecialRenderer(TileFisher.class, new TESRFisher());
	}
	
	private <T extends TileEntity> void bind(TESR<T> tesr, Block block, Class<T> tile, boolean redstoneItem)
	{
		if(tile != null)
			ClientRegistry.bindTileEntitySpecialRenderer(tile, tesr);
		if(!redstoneItem || ConfigsVT.client_3D_redstone)
			ItemRenderingHandler.INSTANCE.setItemRender(Item.getItemFromBlock(block), tesr);
	}
	
	private <T extends TileEntity> void bind(iItemRender tesr, Item block, boolean redstoneItem)
	{
		if(!redstoneItem || ConfigsVT.client_3D_redstone)
			ItemRenderingHandler.INSTANCE.setItemRender(block, tesr);
	}
	
	private static final Random netherStarOreRand = new Random();
	
	@Override
	public void setBlockGlowing(World world, BlockPos pos)
	{
		Block b = world.getBlockState(pos).getBlock();
		
		if(b == BlocksVT.GLOWSTONE_ORE)
			ParticleGlowingBlockOverlay.ensureGlowing(world, pos, Info.MOD_ID + ":blocks/glowstone_ore_overlay");
		
		if(b == BlocksVT.NETHERSTAR_ORE)
		{
			EntityPlayerSP player = Minecraft.getMinecraft().player;
			ItemStack held = player.getHeldItemMainhand();
			netherStarOreRand.setSeed(pos.toLong() + player.world.provider.getDimension());
			if(BlockNetherstarOre.canBeSeenWith(held, netherStarOreRand) || player.capabilities.isCreativeMode)
			{
				ParticleGlowingBlockOverlay.ensureGlowing(world, pos, Info.MOD_ID + ":blocks/netherstar_ore");
				setBlockGlowingBrightness(pos, BlockNetherstarOre.BRIGHTNESS);
			} else
				ParticleGlowingBlockOverlay.ensureNotGlowing(world, pos);
		}
	}
	
	@Override
	public void setBlockNotGlowing(World world, BlockPos pos)
	{
		ParticleGlowingBlockOverlay.ensureNotGlowing(world, pos);
	}
	
	@Override
	public void setBlockGlowingBrightness(BlockPos pos, IntSupplier bright)
	{
		ParticleGlowingBlockOverlay p = ParticleGlowingBlockOverlay.getFromPos(pos);
		if(p != null)
			p.setBrightness(bright);
	}
	
	@Override
	public void setBlockGlowingColor(BlockPos pos, IntSupplier color)
	{
		ParticleGlowingBlockOverlay p = ParticleGlowingBlockOverlay.getFromPos(pos);
		if(p != null)
			p.setColor(color);
	}
}