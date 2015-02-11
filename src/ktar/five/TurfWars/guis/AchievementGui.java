package ktar.five.TurfWars.guis;

import ktar.five.TurfWars.Main;
import ktar.five.TurfWars.guiapi.menus.menus.ItemMenu;

import org.bukkit.entity.Player;

public class AchievementGui extends ItemMenu
{
	private Player player;
	
	public AchievementGui(Player player)
	{
		super("Turf Wars", Size.SIX_LINE, Main.instance);
		this.player = player;
		initInventory();
	}

	public void initInventory()	
	{
		this.setItem(4, new GoBackMenuItem(this));
		this.setItem(22, new TurfWarsStatsItem(player));
		this.setItem(29, new TurfWarsAchievement("", null, null));
	}
}
