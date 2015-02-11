package ktar.five.TurfWars.guis;

import java.util.Arrays;

import ktar.five.TurfWars.guiapi.menus.items.MenuItem;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class TurfWarsStatsItem extends MenuItem
{
	public TurfWarsStatsItem(Player player)
	{
		super(ChatColor.BOLD + "Turf Wars Stats", new ItemStack(Material.BOOK), "");
		this.setLore(Arrays.asList(getStats(player)));	
	}

	private String[] getStats(Player player)
	{
		//?????
		return new String[0];
	}
}
