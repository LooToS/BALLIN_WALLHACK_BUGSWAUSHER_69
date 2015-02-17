package ktar.five.TurfWars.Lobby;

import ktar.five.TurfWars.GenericUtils;
import ktar.five.TurfWars.Main;
import net.minecraft.server.v1_8_R1.EntityInsentient;
import net.minecraft.server.v1_8_R1.NBTTagCompound;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftLivingEntity;
import org.bukkit.entity.*;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.Random;

public class LobbyUtils {

	public static enum MobType{
		BLUESHEEP,
		REDSHEEP,
		MARKSMAN,
		INFILTRATOR,
		SHREDDER
	}

	public static Random r = new Random();

	private static void spawnMob(Location loc, MobType meta){
		LivingEntity entity;
		if(meta.equals(MobType.BLUESHEEP) || meta.equals(MobType.REDSHEEP)){
			entity = (LivingEntity) loc.getWorld().spawnEntity(loc, EntityType.SHEEP);
			Sheep sheep = (Sheep) entity;
			if(meta.equals(MobType.BLUESHEEP)){
				sheep.setColor(DyeColor.BLUE);
			}else if(meta.equals(MobType.REDSHEEP)){
				sheep.setColor(DyeColor.RED);
			}
			sheep.setAdult();
		}else{
			entity = (LivingEntity) loc.getWorld().spawnEntity(loc, EntityType.ZOMBIE);
			Zombie zombie = (Zombie) entity;
			zombie.setBaby(false);
			zombie.setCustomName(meta.toString());
			zombie.setCustomNameVisible(true);
		}

		entity.setCanPickupItems(false);
		entity.setMaxHealth(2000d);
		entity.setHealth(2000d);
		entity.setRemoveWhenFarAway(false);
		entity.setMetadata("LobbyMob", new FixedMetadataValue(Main.instance, meta.toString()));

		EntityInsentient nmsEntity = (EntityInsentient) ((CraftLivingEntity) entity).getHandle();
		NBTTagCompound tag = new NBTTagCompound();
		nmsEntity.c(tag);
		tag.setBoolean("NoAI", true);
		nmsEntity.a(tag);

	}

	public static void playFireworks() {
		r.setSeed(System.nanoTime());
		for(Location loc : Lobby.info.fireworks){

			//Spawn the Firework, get the FireworkMeta.
			Firework fw = (Firework) loc.getWorld().spawnEntity(loc, EntityType.FIREWORK);
			FireworkMeta fwm = fw.getFireworkMeta();
			//Get the type
			int rt = r.nextInt(5) + 1;
			FireworkEffect.Type type = FireworkEffect.Type.BALL;
			if (rt == 1) type = FireworkEffect.Type.BALL;
			if (rt == 2) type = FireworkEffect.Type.BALL_LARGE;
			if (rt == 3) type = FireworkEffect.Type.BURST;
			if (rt == 4) type = FireworkEffect.Type.CREEPER;
			if (rt == 5) type = FireworkEffect.Type.STAR;

			int r1i = r.nextInt(17) + 1;
			int r2i = r.nextInt(17) + 1;
			Color c1 = GenericUtils.getColor(r1i);
			Color c2 = GenericUtils.getColor(r2i);

			//Create our effect with this
			FireworkEffect effect = FireworkEffect.builder().flicker(r.nextBoolean()).withColor(c1).withFade(c2).with(type).trail(r.nextBoolean()).build();

			//Then apply the effect to the meta
			fwm.addEffect(effect);

			//Generate some random power and set it
			int rp = r.nextInt(2) + 1;
			fwm.setPower(rp);

			//Then apply this to our rocket
			fw.setFireworkMeta(fwm);
		}
	}

	static void spawnMobs(Location blueSheep,
			Location redSheep, Location marksman,
			Location infiltrator, Location shredder) {
		spawnMob(blueSheep, MobType.BLUESHEEP);
		spawnMob(redSheep, MobType.REDSHEEP);
		spawnMob(marksman, MobType.MARKSMAN);
		spawnMob(infiltrator, MobType.INFILTRATOR);
		spawnMob(shredder, MobType.SHREDDER);
	}
}
