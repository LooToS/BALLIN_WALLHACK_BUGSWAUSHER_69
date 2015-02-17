package ktar.five.TurfWars.Lobby;

import ktar.five.TurfWars.Game.Info.GamePlayers;
import ktar.five.TurfWars.Game.Info.GameStatus;
import ktar.five.TurfWars.Game.Player.Team;
import ktar.five.TurfWars.Game.Player.TurfPlayer;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class LobbyListeners implements Listener{
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event){
		GamePlayers players = Lobby.players;
		TurfPlayer player = players.getTurfPlayer(event.getPlayer().getUniqueId());
		if(!players.gameFull() && Lobby.status == GameStatus.WAITING_FOR_PLAYERS){
			players.putInLowerTeam(player);
		}else if (Lobby.status == GameStatus.IN_PROGRESS && !players.playerInGame(player.playerUUID) && Lobby.getGame() != null){
			players.putInTeam(Team.SPECTATOR, player);
		}
	}
	
	@EventHandler
	public void onPlayerDisconnect(PlayerQuitEvent event){
		GamePlayers players = Lobby.players;
		TurfPlayer player =players.getTurfPlayer(event.getPlayer().getUniqueId());
		event.getPlayer().setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
		if(players.playerInGame(player.playerUUID)){
			players.remove(player);
		}
	}
	
	
	
}
