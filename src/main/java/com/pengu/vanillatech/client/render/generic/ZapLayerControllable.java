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

public class ZapLayerControllable implements ITickable
{
	public static final ZapLayerControllable ENHANCED_PICKACE__REPAIR = new ZapLayerControllable(0x00FF00, 20);
	
	public final Random rand = new Random();
	public int spawnRateIntern;
	
	public int rgb = 0xFFFFFF;
	public int zapTimeLenght = 10;
	public int maxZapCount = 40;
	
	public List<ParticleSlowZap> zaps = new ArrayList<>();
	
	public ZapLayerControllable(int rgb, int zapLifespan)
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
	}
	
	public void summon(float rad)
	{
		zaps.add(new ParticleSlowZap(null, zapTimeLenght, .2F, .5, .5, .5, .5 + (rand.nextDouble() - rand.nextDouble()) * rad, .5 + (rand.nextDouble() - rand.nextDouble()) * rad, .5 + (rand.nextDouble() - rand.nextDouble()) * rad, ColorHelper.getRed(rgb), ColorHelper.getGreen(rgb), ColorHelper.getBlue(rgb)));
	}
}