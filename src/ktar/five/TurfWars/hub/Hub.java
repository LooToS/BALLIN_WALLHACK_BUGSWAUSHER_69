package ktar.five.TurfWars.hub;

import java.util.ArrayList;
import java.util.List;

import ktar.five.TurfWars.GenericUtils;
import ktar.five.TurfWars.Main;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

public class Hub {

	private Main instance;
	
	public Hub(Main plugin, FileConfiguration config){
		this.instance = plugin;
		World world = Bukkit.getWorld(config.getString("hubOptions.world"));
		ConfigurationSection options = config.getConfigurationSection("hubOptions.hubNpcLocations");
		for(String key : options.getKeys(false)){
			HubUtils.spawnHubNpc(
					GenericUtils.configToLocation(options.getConfigurationSection(key), world));
		}
		
	}
	
}
