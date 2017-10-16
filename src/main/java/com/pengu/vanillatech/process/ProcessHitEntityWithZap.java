package com.pengu.vanillatech.process;

import com.pengu.hammercore.HammerCore;
import com.pengu.hammercore.api.iProcess;

import net.minecraft.entity.Entity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.Vec3d;

public class ProcessHitEntityWithZap implements iProcess
{
	int delay, rgb;
	Vec3d start;
	Entity target;
	DamageSource source;
	private boolean first;
	public float opt_ampl = .15F, opt_damage = 0;
	
	public ProcessHitEntityWithZap(int delay, int rgb, Vec3d start, Entity target, DamageSource source, float dmg)
	{
		this.delay = delay + 7;
		this.rgb = rgb;
		this.start = start;
		this.target = target;
		this.source = source;
		this.opt_damage = dmg;
	}
	
	public void spawn()
	{
		HammerCore.updatables.add(this);
	}
	
	@Override
	public void update()
	{
		if(!first)
		{
			first = true;
			HammerCore.particleProxy.spawnSlowZap(target.world, start, target.getPositionVector().addVector(target.width / 2, target.height / 2, target.width / 2), rgb, delay - 7, opt_ampl);
		}
		
		if(delay > 0)
		{
			--delay;
			if(delay == 0)
				target.attackEntityFrom(source, opt_damage);
		}
	}
	
	@Override
	public boolean isAlive()
	{
		return delay > 0;
	}
}