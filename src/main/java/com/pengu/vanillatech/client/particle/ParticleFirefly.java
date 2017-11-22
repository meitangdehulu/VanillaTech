package com.pengu.vanillatech.client.particle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.profiler.Profiler;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.opengl.GL11;

import com.pengu.hammercore.client.particle.api.SimpleParticle;
import com.pengu.hammercore.client.particle.def.ParticleSlowZap;
import com.pengu.hammercore.proxy.ParticleProxy_Client;
import com.pengu.vanillatech.InfoVT;

@SideOnly(Side.CLIENT)
public class ParticleFirefly extends SimpleParticle
{
	public ParticleFirefly(World world, double x, double y, double z, double tx, double ty, double tz, int particleMaxAge, float size)
	{
		super(world, x, y, z);
		this.particleScale = size;
		this.particleMaxAge = particleMaxAge * 6;
		double dx = tx - x;
		double dy = ty - y;
		double dz = tz - z;
		this.motionX = dx / (double) particleMaxAge;
		this.motionY = dy / (double) particleMaxAge;
		this.motionZ = dz / (double) particleMaxAge;
	}
	
	@Override
	public void onUpdate()
	{
		Entity renderentity = Minecraft.getMinecraft().getRenderViewEntity();
		renderentity.world.profiler.startSection("Firefies");
		double visibleDistance = 50;
		
		if(Minecraft.getMinecraft().gameSettings.particleSetting > 0)
			visibleDistance = 32;
		if(Minecraft.getMinecraft().gameSettings.particleSetting > 1)
			visibleDistance = 5;
		
		if(renderentity.getDistance(posX, posY, posZ) > visibleDistance)
		{
			setExpired();
			return;
		}
		
		if(particleAge % (particleMaxAge / 12) == 0)
		{
			double particleMaxAge = this.particleMaxAge / 12D;
			double tx = posX + (rand.nextDouble() - rand.nextDouble()) * 8;
			double tz = posZ + (rand.nextDouble() - rand.nextDouble()) * 8;
			double ty = world.getHeight((int) tx, (int) tz) + 4 + rand.nextDouble() * 12 - rand.nextDouble() * 4;
			double dx = tx - posX;
			double dy = ty - posY;
			double dz = tz - posZ;
			this.motionX = dx / particleMaxAge;
			this.motionY = dy / particleMaxAge;
			this.motionZ = dz / particleMaxAge;
		}
		
		if(renderentity.getDistance(posX, posY, posZ) < 4)
		{
			int particleMaxAge = this.particleMaxAge / 4;
			double dx = (posX - renderentity.posX) * 2;
			double dy = (posY - renderentity.posY) * 2;
			double dz = (posZ - renderentity.posZ) * 2;
			this.motionX = dx / (double) particleMaxAge;
			this.motionY = dy / (double) particleMaxAge;
			this.motionZ = dz / (double) particleMaxAge;
		}
		
		if(world.isRaining())
		{
			ParticleProxy_Client.queueParticleSpawn(new ParticleSlowZap(world, 50, .01F, posX, posY, posZ, posX, posY, posZ, .75F, 1F, 0));
			setExpired();
			return;
		}
		
		prevPosX = posX;
		prevPosY = posY;
		prevPosZ = posZ;
		
		if(particleAge++ >= particleMaxAge)
			setExpired();
		
		motionY -= 0.04 * particleGravity;
		
		move(motionX, motionY, motionZ);
		
		if(particleAge % 20 == 0)
			ParticleProxy_Client.queueParticleSpawn(new ParticleSlowZap(world, 8, .01F, posX, posY, posZ, prevPosX, prevPosY, prevPosZ, (float) ((.6 + Math.abs(Math.sin(particleAge / 10D)) * .4) * this.particleRed), particleGreen, particleBlue));
		
		motionX *= .9800000190734863;
		motionY *= .9800000190734863;
		motionZ *= .9800000190734863;
		
		if(onGround)
		{
			motionX *= .699999988079071;
			motionZ *= .699999988079071;
		}
		renderentity.world.profiler.endSection();
	}
	
	@Override
	public int getBrightnessForRender(float p_189214_1_)
	{
		return 0;
	}
	
	public ParticleFirefly setColor(int color)
	{
		particleRed = ((color >> 16) & 255) / 255F;
		particleGreen = ((color >> 8) & 255) / 255F;
		particleBlue = ((color >> 0) & 255) / 255F;
		return this;
	}
	
	static final ResourceLocation tex = new ResourceLocation(InfoVT.MOD_ID, "textures/particles/firefly.png");
	
	@Override
	public void doRenderParticle(double x, double y, double z, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ)
	{
		EntityPlayer entityIn = Minecraft.getMinecraft().player;
		Profiler p = Minecraft.getMinecraft().world.profiler;
		
		p.startSection("Fireflies");
		ActiveRenderInfo.updateRenderInfo(entityIn, Minecraft.getMinecraft().gameSettings.thirdPersonView == 2);
		
		Particle.interpPosX = entityIn.lastTickPosX + (entityIn.posX - entityIn.lastTickPosX) * (double) partialTicks;
		Particle.interpPosY = entityIn.lastTickPosY + (entityIn.posY - entityIn.lastTickPosY) * (double) partialTicks;
		Particle.interpPosZ = entityIn.lastTickPosZ + (entityIn.posZ - entityIn.lastTickPosZ) * (double) partialTicks;
		Particle.cameraViewDir = entityIn.getLook(partialTicks);
		GlStateManager.enableBlend();
		
		float agescale = ((float) particleMaxAge - (float) particleAge) / (float) particleMaxAge;
		float particleScale = this.particleScale * agescale;
		
		GL11.glPushMatrix();
		GL11.glDepthMask(false);
		GL11.glBlendFunc(770, 1);
		GlStateManager.enableBlend();
		
		Minecraft.getMinecraft().getTextureManager().bindTexture(tex);
		
		BufferBuilder buffer = Tessellator.getInstance().getBuffer();
		
		buffer.begin(7, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
		
		float f = (float) this.particleTextureIndexX / 16.0F;
		float f1 = f + 0.0624375F;
		float f2 = (float) this.particleTextureIndexY / 16.0F;
		float f3 = f2 + 0.0624375F;
		float f4 = 0.1F * particleScale;
		
		f = 0;
		f1 = 1;
		f2 = 0;
		f3 = 1;
		
		float f5 = (float) (this.prevPosX + (this.posX - this.prevPosX) * (double) partialTicks - interpPosX);
		float f6 = (float) (this.prevPosY + (this.posY - this.prevPosY) * (double) partialTicks - interpPosY);
		float f7 = (float) (this.prevPosZ + (this.posZ - this.prevPosZ) * (double) partialTicks - interpPosZ);
		int i = getBrightnessForRender(partialTicks);
		int j = i >> 16 & 65535;
		int k = i & 65535;
		Vec3d[] avec3d = new Vec3d[] { new Vec3d((double) (-rotationX * f4 - rotationXY * f4), (double) (-rotationZ * f4), (double) (-rotationYZ * f4 - rotationXZ * f4)), new Vec3d((double) (-rotationX * f4 + rotationXY * f4), (double) (rotationZ * f4), (double) (-rotationYZ * f4 + rotationXZ * f4)), new Vec3d((double) (rotationX * f4 + rotationXY * f4), (double) (rotationZ * f4), (double) (rotationYZ * f4 + rotationXZ * f4)), new Vec3d((double) (rotationX * f4 - rotationXY * f4), (double) (-rotationZ * f4), (double) (rotationYZ * f4 - rotationXZ * f4)) };
		
		if(this.particleAngle != 0.0F)
		{
			float f8 = this.particleAngle + (this.particleAngle - this.prevParticleAngle) * partialTicks;
			float f9 = MathHelper.cos(f8 * .5F);
			float f10 = MathHelper.sin(f8 * .5F) * (float) cameraViewDir.x;
			float f11 = MathHelper.sin(f8 * .5F) * (float) cameraViewDir.y;
			float f12 = MathHelper.sin(f8 * .5F) * (float) cameraViewDir.z;
			Vec3d vec3d = new Vec3d((double) f10, (double) f11, (double) f12);
			
			for(int l = 0; l < 4; ++l)
				avec3d[l] = vec3d.scale(2.0D * avec3d[l].dotProduct(vec3d)).add(avec3d[l].scale((double) (f9 * f9) - vec3d.dotProduct(vec3d))).add(vec3d.crossProduct(avec3d[l]).scale((double) (2.0F * f9)));
		}
		
		particleAlpha = 1;
		
		buffer.pos((double) f5 + avec3d[0].x, (double) f6 + avec3d[0].y, (double) f7 + avec3d[0].z).tex((double) f1, (double) f3).color(particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j, k).endVertex();
		buffer.pos((double) f5 + avec3d[1].x, (double) f6 + avec3d[1].y, (double) f7 + avec3d[1].z).tex((double) f1, (double) f2).color(particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j, k).endVertex();
		buffer.pos((double) f5 + avec3d[2].x, (double) f6 + avec3d[2].y, (double) f7 + avec3d[2].z).tex((double) f, (double) f2).color(particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j, k).endVertex();
		buffer.pos((double) f5 + avec3d[3].x, (double) f6 + avec3d[3].y, (double) f7 + avec3d[3].z).tex((double) f, (double) f3).color(particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j, k).endVertex();
		
		Tessellator.getInstance().draw();
		
		particleAlpha = (float) Math.abs(Math.cos((hashCode() + world.getWorldTime()) / 10D)) * .5F;
		
		Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation(InfoVT.MOD_ID, "textures/particles/firefly_overlay.png"));
		
		buffer.begin(7, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
		
		float particleRed = (float) ((.6 + Math.abs(Math.sin(particleAge / 10D)) * .4) * this.particleRed);
		
		buffer.pos((double) f5 + avec3d[0].x, (double) f6 + avec3d[0].y, (double) f7 + avec3d[0].z).tex((double) f1, (double) f3).color(particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j, k).endVertex();
		buffer.pos((double) f5 + avec3d[1].x, (double) f6 + avec3d[1].y, (double) f7 + avec3d[1].z).tex((double) f1, (double) f2).color(particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j, k).endVertex();
		buffer.pos((double) f5 + avec3d[2].x, (double) f6 + avec3d[2].y, (double) f7 + avec3d[2].z).tex((double) f, (double) f2).color(particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j, k).endVertex();
		buffer.pos((double) f5 + avec3d[3].x, (double) f6 + avec3d[3].y, (double) f7 + avec3d[3].z).tex((double) f, (double) f3).color(particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j, k).endVertex();
		
		Tessellator.getInstance().draw();
		
		GlStateManager.disableBlend();
		GL11.glDepthMask(true);
		GL11.glPopMatrix();
		GL11.glBlendFunc(770, 771);
		p.endSection();
	}
}