package ktar.five.TurfWars;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import ktar.five.TurfWars.SQL.MySQL;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;

public class Main extends JavaPlugin implements PluginMessageListener{

	public final MySQL sql = new MySQL(this, null, null, null, null, null);
	Connection c = null;
	public static Economy economy = null;

	
	public static Main instance = null;

	@Override
	public void onLoad(){
		instance = this;
	}

	@Override
	public void onEnable() {
		try {
			c = sql.openConnection();
			sql.updateSQL("CREATE TABLE IF NOT EXISTS UserStats (id int NOT NULL AUTO_INCREMENT, uuid char(36) NOT NULL UNIQUE, " +
					"wins int NOT NULL, defeats int NOT NULL, totalKills int NOT NULL, totalDeaths int NOT NULL, topKillsPerMatch int NOT NULL, " +
					"shortestGame int NOT NULL, longestGame int NOT NULL, topKillStreak int NOT NULL, arrowsShot int NOT NULL, " +
					"blocksDestroyed int NOT NULL, blocksPlaced int NOT NULL, kitsUnlocked int NOT NULL, PRIMARY KEY (id))");
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}

		this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
		this.getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", this);
	}

	@Override
	public void onDisable() {
		instance = null;
		try {
			sql.closeConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}


	private boolean setupEconomy()
	{
		RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
		if (economyProvider != null) {
			economy = economyProvider.getProvider();
		}

		return (economy != null);
	}


	@Override
	public void onPluginMessageReceived(String channel, Player player, byte[] message) {
		if (!channel.equals("BungeeCord")) {
			return;
		}
		ByteArrayDataInput in = ByteStreams.newDataInput(message);
		String subchannel = in.readUTF();
		if (subchannel.equals("GameStatus")) {
			short len = in.readShort();
			byte[] msgbytes = new byte[len];
			in.readFully(msgbytes);

			//DataInputStream msgin = new DataInputStream(new ByteArrayInputStream(msgbytes));

			//This is where we will send the status back.
			String status = "Something";
			sendBungeeGameStatus("LobbyBungeeName", status);


		}else if (subchannel.equals("GameStatusReturn")) {
			short len = in.readShort();
			byte[] msgbytes = new byte[len];
			in.readFully(msgbytes);

			DataInputStream msgin = new DataInputStream(new ByteArrayInputStream(msgbytes));
			String status;
			try {
				status = msgin.readUTF();
			} catch (IOException e) {
				status = "Error";
			}

			//Set status somewhere..... For gui.... Hashmap???/
		}
	}


	public void getBungeeGameStatus(String BungeeName){
		ByteArrayOutputStream b = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(b);
		try
		{
			out.writeUTF("Forward");
			out.writeUTF(BungeeName);
			out.writeUTF("GameStatus");
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		if (Bukkit.getOnlinePlayers().size() > 0)
		{
			Player p = Bukkit.getOnlinePlayers().iterator().next();
			p.sendPluginMessage(this, "BungeeCord", b.toByteArray());
		}
	}

	public void sendBungeeGameStatus(String BungeeName, String Status){
		ByteArrayOutputStream b = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(b);
		try
		{
			out.writeUTF("Forward");
			out.writeUTF(BungeeName);
			out.writeUTF("GameStatusReturn");
			ByteArrayOutputStream msgbytes = new ByteArrayOutputStream();
			DataOutputStream msgout = new DataOutputStream(msgbytes);
			msgout.writeUTF(Status);

			out.write(msgbytes.toByteArray());
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		if (Bukkit.getOnlinePlayers().size() > 0)
		{
			Player p = Bukkit.getOnlinePlayers().iterator().next();
			p.sendPluginMessage(this, "BungeeCord", b.toByteArray());
		}
	}

	public void getBungeePlayerCount(String BungeeName){
		ByteArrayOutputStream b = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(b);
		try
		{      
			out.writeUTF("PlayerCount");
			out.writeUTF(BungeeName);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		if (Bukkit.getOnlinePlayers().size() > 0)
		{
			Player p = Bukkit.getOnlinePlayers().iterator().next();
			p.sendPluginMessage(this, "BungeeCord", b.toByteArray());
		}
	}





}
