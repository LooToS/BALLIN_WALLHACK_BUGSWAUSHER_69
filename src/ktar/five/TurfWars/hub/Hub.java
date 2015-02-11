package ktar.five.TurfWars.hub;

import java.util.HashMap;

import ktar.five.TurfWars.GenericUtils;
import ktar.five.TurfWars.Lobby.Lobby;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

public class Hub {
	
	private HashMap<Integer, Lobby> lobbys = new HashMap<Integer, Lobby>();
	
	public Hub(FileConfiguration config){
		World world = Bukkit.getWorld(config.getString("hubOptions.world"));
		ConfigurationSection options = config.getConfigurationSection("hubOptions.hubNpcLocations");
		for(String key : options.getKeys(false)){
			HubUtils.spawnHubNpc(
					GenericUtils.configToLocation(options.getConfigurationSection(key), world));
		}
	}
	
	public void updateLobby(Lobby lobby)
	{
		
	}
}
