package com.pengu.vanillatech.client.render.generic;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.particle.Particle;
import net.minecraft.util.ITickable;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.MathHelper;

import com.pengu.hammercore.client.particle.def.ParticleSlowZap;
import com.pengu.hammercore.utils.ColorHelper;
import com.pengu.vanillatech.utils.AtomicTuple;

public class ZapLayer implements ITickable
{
	public final Random rand = new Random();
	public int spawnRateIntern;
	
	public int rgb = 0xFFFFFF;
	public int zapTimeLenght = 10;
	public int maxZapCount = 40;
	
	public List<ParticleSlowZap> zaps = new ArrayList<>();
	
	public ZapLayer(int rgb, int zapLifespan)
    {
		this.rgb = rgb;
		this.zapTimeLenght = zapLifespan;
    }
	
	public void render()
	{
		for(int i = 0; i < zaps.size(); ++i)
		{
			ParticleSlowZap zap = zaps.get(i);
			GL11.glPushMatrix();
			GL11.glTranslated(Particle.interpPosX, Particle.interpPosY, Particle.interpPosZ);
			zap.doRenderParticle(0, 0, 0, 1, 0, 0, 0, 0, 0);
			GL11.glPopMatrix();
		}
	}
	
	@Override
	public void update()
	{
		for(int i = 0; i < zaps.size(); ++i)
		{
			ParticleSlowZap zap = zaps.get(i);
			zap.onUpdate();
			if(!zap.isAlive())
				zaps.remove(i);
		}
		
		spawnRateIntern = MathHelper.clamp(spawnRateIntern, 0, 1000);
		if(zaps.size() < maxZapCount && rand.nextInt(spawnRateIntern + 1) == 0)
		{
			spawnRateIntern += 2;
			spawnRateIntern *= 2;
			
			double rad = .5;
			
			zaps.add(new ParticleSlowZap(null, 10, 1F, .5, .5, .5, .5 + (rand.nextDouble() - rand.nextDouble()) * rad, .5 + (rand.nextDouble() - rand.nextDouble()) * rad, .5 + (rand.nextDouble() - rand.nextDouble()) * rad, ColorHelper.getRed(rgb), ColorHelper.getGreen(rgb), ColorHelper.getBlue(rgb)));
		}
		
		if(spawnRateIntern > 0)
			spawnRateIntern -= Math.ceil(Math.cbrt(spawnRateIntern) / 3);
	}
}