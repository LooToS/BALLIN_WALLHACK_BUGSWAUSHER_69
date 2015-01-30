package ktar.five.TurfWars.Game.kits;

import org.bukkit.Material;

public class Infiltrator extends Kit{

	public Infiltrator(){
		super(true, 8, 1, 2000, newIs(Material.BOW, 1), newIs(Material.IRON_SWORD, 1));
	}
	
}
