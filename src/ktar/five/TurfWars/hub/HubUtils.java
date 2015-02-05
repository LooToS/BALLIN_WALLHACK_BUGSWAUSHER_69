package ktar.five.TurfWars.hub;

import ktar.five.TurfWars.Main;
import net.minecraft.server.v1_8_R1.EntityInsentient;
import net.minecraft.server.v1_8_R1.NBTTagCompound;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftLivingEntity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.metadata.FixedMetadataValue;

public class HubUtils {

	public static void spawnHubNpc(Location loc) {
		LivingEntity entity = (LivingEntity) loc.getWorld().spawnEntity(loc, EntityType.VILLAGER);
		
		entity.setCanPickupItems(false);
		entity.setMaxHealth(2000d);
		entity.setHealth(2000d);
		entity.setRemoveWhenFarAway(false);
		entity.setMetadata("HubMob", new FixedMetadataValue(Main.instance, true));

		EntityInsentient nmsEntity = (EntityInsentient) ((CraftLivingEntity) entity).getHandle();
		NBTTagCompound tag = new NBTTagCompound();
		nmsEntity.c(tag);
		tag.setBoolean("NoAI", true);
		nmsEntity.a(tag);
		
	}

}
