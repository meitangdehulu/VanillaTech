package com.pengu.vanillatech.evt;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.advancements.Advancement;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SPacketCustomSound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import com.pengu.hammercore.HammerCore;
import com.pengu.hammercore.annotations.MCFBus;
import com.pengu.hammercore.utils.AdvancementUtils;
import com.pengu.hammercore.utils.WorldLocation;
import com.pengu.vanillatech.Info;
import com.pengu.vanillatech.init.BlocksVT;
import com.pengu.vanillatech.init.DamageSourcesVT;
import com.pengu.vanillatech.init.ItemsVT;
import com.pengu.vanillatech.process.ProcessConvertIronToUnstable;

@MCFBus
public class ExplosionEvents
{
	@SubscribeEvent
	public void explode(ExplosionEvent.Detonate evt)
	{
		Vec3d estart = evt.getExplosion().getPosition();
		
		for(int i = 0; i < evt.getAffectedBlocks().size(); ++i)
		{
			BlockPos pos = evt.getAffectedBlocks().get(i);
			if(ProcessConvertIronToUnstable.getProcess(new WorldLocation(evt.getWorld(), pos)) != null || evt.getWorld().getBlockState(pos).getBlock() == BlocksVT.UNSTABLE_METAL_BLOCK)
				evt.getAffectedBlocks().remove(i);
		}
		
		List<Entity> ents = evt.getAffectedEntities();
		for(Entity e : new ArrayList<>(ents))
			if(e instanceof EntityItem)
			{
				ItemStack stack = ((EntityItem) e).getItem().copy();
				if(stack.getItem() == Items.NETHER_STAR && e.ticksExisted > 5 && e.getDistanceSq(estart.x, estart.y, estart.z) < 16)
				{
					((EntityItem) e).setItem(new ItemStack(ItemsVT.NETHERSTAR_SHARD, 8));
					for(int i = 0; i < stack.getCount() - 1; ++i)
						if(!evt.getWorld().isRemote)
							e.dropItem(ItemsVT.NETHERSTAR_SHARD, 8);
					if(!evt.getWorld().isRemote)
					{
						Random r = evt.getWorld().rand;
						HammerCore.audioProxy.playSoundAt(evt.getWorld(), "entity.elder_guardian.curse", e.getPosition(), 4F, 1F, SoundCategory.NEUTRAL);
						
						int zaps = 32 + r.nextInt(32);
						for(int i = 0; i < zaps; ++i)
						{
							Vec3d a = e.getPositionVector();
							Vec3d b = e.getPositionVector().addVector((r.nextDouble() - r.nextDouble()) * 12, r.nextDouble() * 12, (r.nextDouble() - r.nextDouble()) * 12);
							HammerCore.particleProxy.spawnSlowZap(evt.getWorld(), a, b, 0xFFDDCC, 16, .15F);
							
							List<Entity> entsW = evt.getWorld().getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(a.x, a.y, a.z, b.x, b.y, b.z), ent -> ent != null && !EntityItem.class.isAssignableFrom(ent.getClass()));
							for(Entity ew : entsW)
								ew.attackEntityFrom(DamageSourcesVT.NETHER_STAR_DISSOLVE, 1000F);
						}
					}
				}
				if(((EntityItem) e).getItem().getItem() == ItemsVT.NETHERSTAR_SHARD)
				{
					ents.remove(e);
					if(e.ticksExisted >= 5)
					{
						BlockPos pos = e.getPosition().down();
						World world = e.world;
						
						IBlockState state = world.getBlockState(pos);
						
						if(state.getBlock() == Blocks.IRON_BLOCK)
						{
							List<BlockPos> posss = evt.getAffectedBlocks();
							int index = posss.indexOf(pos);
							if(index != -1)
							{
								posss.remove(index);
								ProcessConvertIronToUnstable.startProcess(new WorldLocation(world, pos));
								((EntityItem) e).getItem().shrink(1);
							}
						}
					}
				}
			}
		
		List<BlockPos> pss = new ArrayList<BlockPos>(evt.getAffectedBlocks());
		
		EntityLivingBase ent = evt.getExplosion().getExplosivePlacedBy();
		if(ent instanceof EntityWither)
		{
			MinecraftServer mc = ent.getServer();
			if(mc == null)
				return;
			
			EntityWither wither = (EntityWither) ent;
			
			for(int x = -6; x < 6; ++x)
				for(int y = -6; y < 6; ++y)
					for(int z = -6; z < 6; ++z)
						pss.add(wither.getPosition().add(x, y, z));
			
			List<EntityPlayerMP> players = null;
			boolean containsWitherproof = false;
			
			StatBase useWP = StatList.getCraftStats(Item.getItemFromBlock(BlocksVT.WITHER_PROOF_RED_STONE));
			
			if(useWP != null)
				for(int i = 0; i < pss.size(); ++i)
				{
					BlockPos pos = pss.get(i);
					IBlockState state = evt.getWorld().getBlockState(pos);
					if(state.getBlock() == BlocksVT.WITHER_PROOF_RED_STONE)
					{
						players = evt.getWorld().getEntitiesWithinAABB(EntityPlayerMP.class, new AxisAlignedBB(pos).grow(64), e -> e.getStatFile().readStat(useWP) > 0);
						containsWitherproof = true;
						break;
					}
				}
			
			ResourceLocation advancement = new ResourceLocation(Info.MOD_ID, "main/witherproof_red_stone");
			Advancement adv = mc.getAdvancementManager().getAdvancement(advancement);
			if(players != null)
				for(EntityPlayerMP mp : players)
				{
					if(!mp.getAdvancements().getProgress(adv).isDone())
						mp.connection.sendPacket(new SPacketCustomSound("ui.toast.challenge_complete", SoundCategory.PLAYERS, mp.posX, mp.posY, mp.posZ, 362367F, 1));
					AdvancementUtils.completeAdvancement(advancement, mp);
				}
		}
	}
}