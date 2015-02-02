package ktar.five.TurfWars;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;

public class GenericUtils {

	public static Location configToLocation(ConfigurationSection section){
		return new Location (
				Bukkit.getServer().getWorld(section.getString("world")),
				Double.valueOf(section.getString("x")),
				Double.valueOf(section.getString("y")),
				Double.valueOf(section.getString("z")));
	}

	public static Location configToLocation(ConfigurationSection section, World world){
		return new Location (
				world,
				Double.valueOf(section.getString("x")),
				Double.valueOf(section.getString("y")),
				Double.valueOf(section.getString("z")));
	}
	
	
}
