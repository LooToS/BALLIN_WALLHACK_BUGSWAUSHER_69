package ktar.five.TurfWars.hub;

import ktar.five.TurfWars.GenericUtils;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

public class Hub {
	
	public Hub(FileConfiguration config){
		World world = Bukkit.getWorld(config.getString("hubOptions.world"));
		ConfigurationSection options = config.getConfigurationSection("hubOptions.hubNpcLocations");
		for(String key : options.getKeys(false)){
			HubUtils.spawnHubNpc(
					GenericUtils.configToLocation(options.getConfigurationSection(key), world));
		}
		
	}
	
}
