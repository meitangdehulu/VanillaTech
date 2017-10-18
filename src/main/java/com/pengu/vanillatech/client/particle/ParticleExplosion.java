package com.pengu.vanillatech.client.particle;

import org.lwjgl.opengl.GL11;

import com.pengu.hammercore.client.particle.api.SimpleParticle;
import com.pengu.hammercore.client.utils.RenderUtil;
import com.pengu.hammercore.utils.ColorHelper;

import net.minecraft.world.World;

public class ParticleExplosion extends SimpleParticle
{
	public ParticleExplosion(World worldIn, double posXIn, double posYIn, double posZIn)
	{
		super(worldIn, posXIn, posYIn, posZIn);
		particleMaxAge = 100;
	}
	
	public ParticleExplosion(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn)
	{
		super(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
		particleMaxAge = 70;
	}
	
	@Override
	public void doRenderParticle(double x, double y, double z, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ)
	{
		float progression = Math.min((particleAge + partialTicks) / particleMaxAge, 1);
		
		if(particleAge < 3)
			return;
		
		int alpha = 10;
		
		if(progression > .5F)
			alpha += 245 * (progression - .5F) * 2F;
		
		GL11.glPushMatrix();
		GL11.glTranslated(x, y, z);
		GL11.glScaled(8, 8, 8);
		GL11.glScaled(progression * 90, progression * 90, progression * 90);
		RenderUtil.renderLightRayEffects(0, 0, 0, alpha << 24 | ColorHelper.packRGB(particleRed, particleGreen, particleBlue), hashCode(), progression, 1, 2F, 50, 25);
		GL11.glPopMatrix();
	}
}