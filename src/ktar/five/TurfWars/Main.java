package ktar.five.TurfWars;

import java.sql.Connection;
import java.sql.SQLException;

import ktar.five.TurfWars.SQL.MySQL;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

public class Main extends JavaPlugin implements PluginMessageListener{
	
	public final MySQL sql = new MySQL(this, null, null, null, null, null);
	Connection c = null;
	
	public static Main instance = null;

	@Override
	public void onLoad(){
		instance = this;
	}
	
	@Override
	public void onEnable() {
		try {
			c = sql.openConnection();
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

	@Override
	public void onPluginMessageReceived(String channel, Player player, byte[] message) {
		if (!channel.equals("BungeeCord")) {
			return;
		}
		ByteArrayDataInput in = ByteStreams.newDataInput(message);
		String subchannel = in.readUTF();
		if (subchannel.equals("SomeSubChannel")) {
			// Use the code sample in the 'Response' sections below to read
			// the data.
		}
	}





}
