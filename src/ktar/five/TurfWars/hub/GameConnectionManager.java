package ktar.five.TurfWars.hub;

import ktar.five.TurfWars.Main;

import org.bukkit.entity.Player;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

public class GameConnectionManager {
	
	
	public void join(String serverid, Player player){
		  ByteArrayDataOutput out = ByteStreams.newDataOutput();
		  out.writeUTF("Connect");
		  out.writeUTF(serverid);
		  
		  player.sendPluginMessage(Main.instance, "BungeeCord", out.toByteArray());
	}
	
	public void sendToLobby(Player player){
		  ByteArrayDataOutput out = ByteStreams.newDataOutput();
		  out.writeUTF("Connect");
		  out.writeUTF("lobby");
		  
		  player.sendPluginMessage(Main.instance, "BungeeCord", out.toByteArray());
	}
	
	
	
}
