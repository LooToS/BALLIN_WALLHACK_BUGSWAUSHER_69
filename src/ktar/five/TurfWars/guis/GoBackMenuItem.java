package ktar.five.TurfWars.guis;

import ktar.five.TurfWars.Main;
import ktar.five.TurfWars.guiapi.menus.items.SubMenuItem;
import ktar.five.TurfWars.guiapi.menus.menus.ItemMenu;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class GoBackMenuItem extends SubMenuItem
{
	public GoBackMenuItem(ItemMenu gui)
	{
		super(Main.instance, ChatColor.GRAY + "<- Go Back", new ItemStack(Material.BED), gui, new String[0]);
	}
}
