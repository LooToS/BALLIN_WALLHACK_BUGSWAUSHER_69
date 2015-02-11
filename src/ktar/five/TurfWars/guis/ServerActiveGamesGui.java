package ktar.five.TurfWars.guis;

import ktar.five.TurfWars.Main;
import ktar.five.TurfWars.Lobby.Lobby;
import ktar.five.TurfWars.guiapi.menus.events.ItemClickEvent;
import ktar.five.TurfWars.guiapi.menus.items.MenuItem;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class ServerActiveGamesGui extends MenuItem
{
	private Lobby lobbyToConnectTo;
	private Plugin p;
	
	public ServerActiveGamesGui(Lobby lobby)
	{
		super(lobby.getName(), new ItemStack(Material.EMERALD_BLOCK), new String[] 
				{"",
			     ChatColor.YELLOW + "Game: " + ChatColor.WHITE + "Turf Wars",
				 ChatColor.YELLOW + "Map: "	+ ChatColor.WHITE +  lobby.getGame().toString(),
				 ChatColor.YELLOW + "Players: " + ChatColor.WHITE + lobby.players.getAll().values().size() + "/" + (lobby.players.maxPerTeam * 2),
				 "",
				 ChatColor.YELLOW + "In Progress",
				 ChatColor.UNDERLINE +  "Get Ultra to join full servers!"
				 });
		this.lobbyToConnectTo = lobby;
		p = Main.instance;
	}

	public void onItemClick(ItemClickEvent event)
	{
        event.setWillClose(true);
        final String playerName = event.getPlayer().getName();
        Bukkit.getScheduler().scheduleSyncDelayedTask(p, new Runnable() {
            public void run() {
                Player p = Bukkit.getPlayerExact(playerName);
                if (p != null) {
                	
                }
            }
        }, 3);

	}
}