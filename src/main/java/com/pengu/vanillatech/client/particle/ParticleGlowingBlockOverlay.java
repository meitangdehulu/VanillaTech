package com.pengu.vanillatech.client.particle;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.IntSupplier;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.opengl.GL11;

import com.pengu.hammercore.client.particle.api.SimpleParticle;
import com.pengu.hammercore.client.render.vertex.SimpleBlockRendering;
import com.pengu.hammercore.client.utils.RenderBlocks;
import com.pengu.hammercore.proxy.ParticleProxy_Client;
import com.pengu.vanillatech.InfoVT;

@SideOnly(Side.CLIENT)
public class ParticleGlowingBlockOverlay extends SimpleParticle
{
	public static final Map<Long, ParticleGlowingBlockOverlay> particles = new HashMap<>();
	
	public static final IntSupplier DEF_COLOR = () -> 0xFFFFFF;
	public static final IntSupplier DEF_BRIGHTNESS = () -> 15 << 20 | 15 << 4;
	
	private final BlockPos pos;
	private final IBlockState state;
	private final String glowSprite;
	private AxisAlignedBB aabb = Block.FULL_BLOCK_AABB;
	private IntSupplier color = DEF_COLOR, brightness = DEF_BRIGHTNESS;
	private int originalDimension;
	
	@Nullable
	public static ParticleGlowingBlockOverlay ensureGlowing(World world, BlockPos pos, String glowSprite)
	{
		IBlockState state = world.getBlockState(pos);
		boolean noFaces = true;
		
		for(EnumFacing f : EnumFacing.VALUES)
			if(state.shouldSideBeRendered(world, pos, f))
			{
				noFaces = false;
				break;
			}
		
		if(!noFaces)
		{
			if(particles.get(pos.toLong()) == null || particles.get(pos.toLong()).isExpired)
				new ParticleGlowingBlockOverlay(world, pos, glowSprite).summon();
			return particles.get(pos.toLong());
		} else
		{
			if(particles.get(pos.toLong()) != null && particles.get(pos.toLong()).isExpired)
				particles.remove(pos.toLong()).setExpired();
			return null;
		}
	}
	
	public static void ensureNotGlowing(World world, BlockPos pos)
	{
		if(particles.get(pos.toLong()) != null)
			particles.remove(pos.toLong()).setExpired();
	}
	
	@Nullable
	public static ParticleGlowingBlockOverlay getFromPos(BlockPos pos)
	{
		return particles.get(pos.toLong());
	}
	
	public ParticleGlowingBlockOverlay(World worldIn, BlockPos pos, String glowSprite)
	{
		super(worldIn, pos.getX(), pos.getY(), pos.getZ());
		originalDimension = worldIn.provider.getDimension();
		state = worldIn.getBlockState(pos);
		this.glowSprite = glowSprite;
		setAABB(Block.FULL_BLOCK_AABB);
		this.pos = new BlockPos(pos.getX(), pos.getY(), pos.getZ());
		setSize(1, 1);
	}
	
	public void setColor(IntSupplier color)
	{
		this.color = color;
	}
	
	public void setBrightness(IntSupplier brightness)
	{
		this.brightness = brightness;
	}
	
	public IntSupplier getColor()
	{
		return color;
	}
	
	public IntSupplier getBrightness()
	{
		return brightness;
	}
	
	public void setAABB(AxisAlignedBB aabb)
	{
		this.aabb = aabb.grow(.01 / 16);
	}
	
	public void summon()
	{
		if(particles.get(pos.toLong()) == null || particles.get(pos.toLong()).isExpired)
		{
			particles.put(pos.toLong(), this);
			ParticleProxy_Client.queueParticleSpawn(this);
		}
	}
	
	@Override
	public void setExpired()
	{
		if(particles.get(pos.toLong()) == this)
			particles.remove(pos.toLong());
		super.setExpired();
	}
	
	@Override
	public void onUpdate()
	{
		world.profiler.startSection("Glowing Block Overlays");
		posX = pos.getX();
		posY = pos.getY();
		posZ = pos.getZ();
		
		if(particles.get(pos.toLong()) == null)
		{
			setExpired();
			return;
		}
		
		if(!world.isBlockLoaded(pos))
		{
			setExpired();
			return;
		}
		
		if(!world.getBlockState(pos).equals(state))
		{
			setExpired();
			return;
		}
		
		if(Minecraft.getMinecraft().world.provider.getDimension() != originalDimension)
		{
			setExpired();
			return;
		}
		
		isExpired = false;
		world.profiler.endSection();
	}
	
	@Override
	public void move(double x, double y, double z)
	{
	}
	
	@Override
	public void doRenderParticle(double x, double y, double z, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ)
	{
		world.profiler.startSection("Glowing Block Overlays");
		if(isExpired)
			return;
		if(color == null)
			color = DEF_COLOR;
		if(brightness == null)
			brightness = DEF_BRIGHTNESS;
		
		GL11.glPushMatrix();
		GL11.glTranslated(x, y, z);
		SimpleBlockRendering sbr = RenderBlocks.forMod(InfoVT.MOD_ID).simpleRenderer;
		
		sbr.begin();
		GlStateManager.enableRescaleNormal();
		GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		sbr.setSprite(Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(glowSprite));
		boolean noFaces = true;
		for(EnumFacing f : EnumFacing.VALUES)
			if(!world.getBlockState(pos).shouldSideBeRendered(world, pos, f))
				sbr.disableFace(f);
			else
				noFaces = false;
		sbr.setRenderBounds(aabb);
		Arrays.fill(sbr.rgb, color.getAsInt());
		sbr.setBrightness(brightness.getAsInt());
		sbr.drawBlock(0, 0, 0);
		sbr.enableFaces();
		sbr.end();
		
		GL11.glPopMatrix();
		if(noFaces)
			setExpired();
		world.profiler.endSection();
	}
}