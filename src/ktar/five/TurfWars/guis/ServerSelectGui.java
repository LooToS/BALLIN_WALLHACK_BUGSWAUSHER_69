package ktar.five.TurfWars.guis;

import java.util.List;

import ktar.five.TurfWars.Main;
import ktar.five.TurfWars.Lobby.Lobby;
import ktar.five.TurfWars.Lobby.LobbyUtils;
import ktar.five.TurfWars.guiapi.menus.menus.ItemMenu;
import ktar.five.TurfWars.hub.Hub;

public class ServerSelectGui extends ItemMenu
{
	public ServerSelectGui()
	{
		super("Turf Wars", Size.SIX_LINE, Main.instance);
		initInventory();
	}

	public void initInventory()	
	{
		this.setItem(4, new GoBackMenuItem(this));
//		for(Lobby lobby : LobbyUtils.instance.getJoinableServers())
//		{
			this.addItem(new ServerMenuItem(null));
//		}
	}
}
