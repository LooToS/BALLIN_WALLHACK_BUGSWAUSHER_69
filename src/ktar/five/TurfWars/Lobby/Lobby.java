package ktar.five.TurfWars.Lobby;

import ktar.five.TurfWars.Main;
import ktar.five.TurfWars.Game.Game;
import ktar.five.TurfWars.Game.Info.GamePlayers;
import ktar.five.TurfWars.Game.Info.GameStatus;
import ktar.five.TurfWars.Game.Info.WorldManager;
import ktar.five.TurfWars.Game.Cooling.Cooldown;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;

public class Lobby implements Listener{

	private Game game;	
	public WorldManager info;
	public GameStatus status;
	private int seconds;
	private int lobbyCountdown = 50;
	public static GamePlayers players;
	private Main instance;
	
	public Lobby(Main plugin, FileConfiguration config){
		this.instance = plugin;
		this.createTimer();
		World world = Bukkit.getWorld(config.getString("lobbyOptions.world"));
		ConfigurationSection locations = config.getConfigurationSection("lobbyOptions.locations");
		LobbyUtils.spawnMobs(LobbyUtils.configToLocation(locations.getConfigurationSection("blueSheep"), world),
				LobbyUtils.configToLocation(locations.getConfigurationSection("redSheep"), world), 
				LobbyUtils.configToLocation(locations.getConfigurationSection("marksman"), world), 
				LobbyUtils.configToLocation(locations.getConfigurationSection("infiltrator"), world), 
				LobbyUtils.configToLocation(locations.getConfigurationSection("shredder"), world));
		
		info = new WorldManager(LobbyUtils.configToLocation(locations.getConfigurationSection("lobbySpawn"), world), 
				LobbyUtils.configToLocation(locations.getConfigurationSection("redSpawn"), world), 
				LobbyUtils.configToLocation(locations.getConfigurationSection("blueSpawn"), world), 
				LobbyUtils.configToLocation(locations.getConfigurationSection("boundaryOne"), world), 
				LobbyUtils.configToLocation(locations.getConfigurationSection("boundaryTwo"), world));
	}
	
    private void createTimer() {
        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(instance, new Runnable() {
            @Override
            public void run() {
            	perSecond();
            	Cooldown.handleCooldowns();
            }
        }, 0L, 20L);
    }
    
    public void perSecond() {
        seconds++;
        if (status == GameStatus.WAITING_FOR_PLAYERS && players.gameFull()) {
            seconds = 0;
            this.updateStatus(GameStatus.LOBBY_COUNTDOWN);
        } else if (status == GameStatus.LOBBY_COUNTDOWN) {
            if (!players.gameFull()) {
                seconds = 0;
                this.updateStatus(GameStatus.WAITING_FOR_PLAYERS);
            } else if (players.gameFull() && game.seconds == lobbyCountdown) {
                this.startGame();
            }
        } else if (status == GameStatus.STARTING || status == GameStatus.IN_PROGRESS || status == GameStatus.ENDING){
        	game.perSecond();
        }
    }
    
    private void startGame(){
    	game.start(players, info);
    }
    
    private void endGame(){
    	
    }

	public void updateStatus(GameStatus status) {
		this.status = status;
		//update database
		
	}
	
}
