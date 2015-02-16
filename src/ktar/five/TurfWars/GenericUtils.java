package ktar.five.TurfWars;

import org.bukkit.Bukkit;
import org.bukkit.Color;
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

	public static Color getColor(int i) {
		Color c = null;
		if(i==1){
			c=Color.AQUA;
		}
		if(i==2){
			c=Color.BLACK;
		}
		if(i==3){
			c=Color.BLUE;
		}
		if(i==4){
			c=Color.FUCHSIA;
		}
		if(i==5){
			c=Color.GRAY;
		}
		if(i==6){
			c=Color.GREEN;
		}
		if(i==7){
			c=Color.LIME;
		}
		if(i==8){
			c=Color.MAROON;
		}
		if(i==9){
			c=Color.NAVY;
		}
		if(i==10){
			c=Color.OLIVE;
		}
		if(i==11){
			c=Color.ORANGE;
		}
		if(i==12){
			c=Color.PURPLE;
		}
		if(i==13){
			c=Color.RED;
		}
		if(i==14){
			c=Color.SILVER;
		}
		if(i==15){
			c=Color.TEAL;
		}
		if(i==16){
			c=Color.WHITE;
		}
		if(i==17){
			c=Color.YELLOW;
		}

		return c;
	}
	
	
}
