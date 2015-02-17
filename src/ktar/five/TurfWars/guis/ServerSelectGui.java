package ktar.five.TurfWars.guis;

import ktar.five.TurfWars.Main;
import ktar.five.TurfWars.guiapi.menus.menus.ItemMenu;

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
