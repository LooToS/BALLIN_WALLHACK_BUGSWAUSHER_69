package ktar.five.TurfWars.Lobby;

import ktar.five.TurfWars.Main;

import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Sheep;
import org.bukkit.entity.Zombie;
import org.bukkit.metadata.FixedMetadataValue;

public class LobbyUtils {

	public static enum MobType{
		BLUESHEEP,
		REDSHEEP,
		MARKSMAN,
		INFILTRATOR,
		SHREDDER
	}

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

	static void spawnMobs(Location blueSheep,
			Location redSheep, Location marksman,
			Location infiltrator, Location shredder) {
		spawnMob(blueSheep, MobType.BLUESHEEP);
		spawnMob(redSheep, MobType.REDSHEEP);
		spawnMob(marksman, MobType.MARKSMAN);
		spawnMob(infiltrator, MobType.INFILTRATOR);
		spawnMob(shredder, MobType.SHREDDER);
	}

	static Location configToLocation(ConfigurationSection section){
		return new Location (
				Bukkit.getServer().getWorld(section.getString("world")),
				Double.valueOf(section.getString("x")),
				Double.valueOf(section.getString("y")),
				Double.valueOf(section.getString("z")));
	}

	static Location configToLocation(ConfigurationSection section, World world){
		return new Location (
				world,
				Double.valueOf(section.getString("x")),
				Double.valueOf(section.getString("y")),
				Double.valueOf(section.getString("z")));
	}


}
