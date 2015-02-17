package ktar.five.TurfWars.Lobby;

import ktar.five.TurfWars.Game.Cooling.Cooldown;
import ktar.five.TurfWars.Game.Game;
import ktar.five.TurfWars.Game.Info.GamePlayers;
import ktar.five.TurfWars.Game.Info.GameStatus;
import ktar.five.TurfWars.Game.Info.WorldManager;
import ktar.five.TurfWars.Game.Player.Team;
import ktar.five.TurfWars.Game.Player.TurfPlayer;
import ktar.five.TurfWars.Game.Scoreboard.Scoreboards;
import ktar.five.TurfWars.GenericUtils;
import ktar.five.TurfWars.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.List;

public class Lobby implements Listener{

	private static Game game;	
	public static WorldManager info;
	public static GameStatus status;
	public static int seconds;
	public static int lobbyCountdown = 50;
	public static GamePlayers players;
	public static String serverid;

	public Lobby(FileConfiguration config){
		this.createTimer();
		this.players = new GamePlayers();
		seconds = 0;
		serverid = config.getString("serverid");
		World world = Bukkit.getWorld(config.getString("lobbyOptions.world"));
		ConfigurationSection locations = config.getConfigurationSection("lobbyOptions.locations");
		LobbyUtils.spawnMobs(GenericUtils.configToLocation(locations.getConfigurationSection("blueSheep"), world),
				GenericUtils.configToLocation(locations.getConfigurationSection("redSheep"), world), 
				GenericUtils.configToLocation(locations.getConfigurationSection("marksman"), world), 
				GenericUtils.configToLocation(locations.getConfigurationSection("infiltrator"), world), 
				GenericUtils.configToLocation(locations.getConfigurationSection("shredder"), world));
		List<Location> fireworks = new ArrayList<Location>();
		for(String section : locations.getConfigurationSection("fireworks").getKeys(false)){
			fireworks.add(GenericUtils.configToLocation(locations.getConfigurationSection("fireworks." + section), world));
		}
		info = new WorldManager(GenericUtils.configToLocation(locations.getConfigurationSection("lobbySpawn"), world), 
				GenericUtils.configToLocation(locations.getConfigurationSection("redSpawn"), world), 
				GenericUtils.configToLocation(locations.getConfigurationSection("blueSpawn"), world), 
				GenericUtils.configToLocation(locations.getConfigurationSection("boundaryOne"), world), 
				GenericUtils.configToLocation(locations.getConfigurationSection("boundaryTwo"), world),
				fireworks,
				GenericUtils.configToLocation(locations.getConfigurationSection("winning"), world),
				GenericUtils.configToLocation(locations.getConfigurationSection("loosing"), world));
		status = GameStatus.WAITING_FOR_PLAYERS;
		game = new Game(serverid);
	}
	
    private void createTimer() {
        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Main.instance, new Runnable() {
            @Override
            public void run() {
            	perSecond();
            	Cooldown.handleCooldowns();
            }
        }, 0L, 20L);
    }
    
    public void perSecond() {
        seconds++;
		if(status == GameStatus.LOBBY_COUNTDOWN || status == GameStatus.WAITING_FOR_PLAYERS){
			for(TurfPlayer p : players.getAll().values()){
				Scoreboards.getLobbyScoreboard(p);
			}
		}else if(status == GameStatus.IN_PROGRESS || status == GameStatus.STARTING){
			for(TurfPlayer p : players.getAll().values()){
				Scoreboards.getGameScoreboard(p);
			}
		}
        if (status == GameStatus.WAITING_FOR_PLAYERS && players.gameFull()) {
            seconds = 0;
            updateStatus(GameStatus.LOBBY_COUNTDOWN);
        } else if (status == GameStatus.LOBBY_COUNTDOWN) {
            if (!players.gameFull()) {
                seconds = 0;
                updateStatus(GameStatus.WAITING_FOR_PLAYERS);
            } else if (players.gameFull() && seconds == lobbyCountdown) {
                this.startGame();
            }
        } else if (status == GameStatus.STARTING || status == GameStatus.IN_PROGRESS){
        	game.perSecond();
        } else if (status == GameStatus.ENDING) {
			if (seconds == 30) {
				updateStatus(GameStatus.RESTARTING);
				for (TurfPlayer player : players.getAll().values()) {
					player.returnToLobby();
				}
				this.endGame(info.getWinning());
				players.clear();
			}else{
				LobbyUtils.playFireworks();
			}

		}
    }

	private void startGame(){
    	game.start(info);
    }
    
    private static void endGame(Team team){
    	game.endGame(team);
		game = new Game(serverid);
		status = GameStatus.WAITING_FOR_PLAYERS;
		seconds = 0;
    }

	public static void teamWon(Team team){
		updateStatus(GameStatus.ENDING);
		for(TurfPlayer player : players.getTurfPlayers(team, false).values()){
			Player p = player.getPlayer();
			p.getInventory().clear();
			p.setHealth(20d);

			player.canMove = false;
			player.canVenture = true;
			p.teleport(info.winning);
		}
		for(TurfPlayer player : players.getTurfPlayers(team, true).values()){
			Player p = player.getPlayer();
			p.getInventory().clear();
			p.setHealth(20d);

			player.canMove = false;
			player.canVenture = true;
			p.teleport(info.loosing);
		}
		seconds = 0;
	}

	public static void updateStatus(GameStatus newstatus) {
		status = newstatus;
	}
	
	public static Game getGame(){
		return game;
	}

	public String getName() {
		return getGame().serverID;
	}
	
}
