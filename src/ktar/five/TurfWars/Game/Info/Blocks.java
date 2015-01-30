package ktar.five.TurfWars.Game.Info;

import java.util.List;

import org.bukkit.Location;

public class Blocks {
	public Team team;
	public List<Location> blocks;
	
	public Blocks (Team team, List <Location> blocks){
		this.team = team;
		this.blocks = blocks;
	}
	
}
