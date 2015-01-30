package ktar.five.TurfWars.Game.cooldowns;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;

public class Cooldown {

	public static HashMap<UUID, AbilityCooldown> cooldownPlayers = new HashMap<>();
	
	public static void add(UUID player, String ability, long seconds) {
        if(!cooldownPlayers.containsKey(player)) cooldownPlayers.put(player, new AbilityCooldown(player));
        if(isCooling(player, ability)) return;
        cooldownPlayers.get(player).cooldownMap.put(ability, new AbilityCooldown(player, seconds * 1000, System.currentTimeMillis()));
    }
	
	public static boolean isCooling(UUID player, String ability) {
        return cooldownPlayers.containsKey(player) && cooldownPlayers.get(player).cooldownMap.containsKey(ability);
    }
	
	public static double getRemaining(UUID player, String ability) {
        if(!cooldownPlayers.containsKey(player)) return 0.0;
        if(!cooldownPlayers.get(player).cooldownMap.containsKey(ability)) return 0.0;
        return utilTime.convert((cooldownPlayers.get(player).cooldownMap.get(ability).seconds + cooldownPlayers.get(player).cooldownMap.get(ability).systime) - System.currentTimeMillis(), utilTime.SECONDS, 1);
    }
	
	public static void removeCooldown(UUID player, String ability) {
        if(!cooldownPlayers.containsKey(player)) {
            return;
        }
        if(!cooldownPlayers.get(player).cooldownMap.containsKey(ability)) {
            return;
        }
        cooldownPlayers.get(player).cooldownMap.remove(ability);

    }
	
	
	
	public static void handleCooldowns() {
        if(cooldownPlayers.isEmpty()) {
            return;
        }
        for (UUID key : cooldownPlayers.keySet()) {
            for (String name : cooldownPlayers.get(key).cooldownMap.keySet()) {
                if (getRemaining(key, name) <= 0.0) {
                    if(name == "arrow") {
                        Bukkit.getServer().getPluginManager().callEvent(new TurfEvent(key));
                    }
                    removeCooldown(key, name);
                }
            }
        }
    }
	
	
	
}
