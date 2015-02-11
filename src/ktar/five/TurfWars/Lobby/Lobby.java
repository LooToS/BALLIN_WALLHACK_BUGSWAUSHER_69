package ktar.five.TurfWars.Lobby;

import java.util.ArrayList;
import java.util.List;

import ktar.five.TurfWars.GenericUtils;
import ktar.five.TurfWars.Main;
import ktar.five.TurfWars.Game.Game;
import ktar.five.TurfWars.Game.Cooling.Cooldown;
import ktar.five.TurfWars.Game.Info.GamePlayers;
import ktar.five.TurfWars.Game.Info.GameStatus;
import ktar.five.TurfWars.Game.Info.WorldManager;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;

public class Lobby implements Listener{

	private static Game game;	
	public WorldManager info; 
	public static GameStatus status;
	private int seconds;
	private int lobbyCountdown = 50;
	public static GamePlayers players;
	private Main instance;
	
	public Lobby(Main plugin, FileConfiguration config){
		this.instance = plugin;
		this.createTimer();
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
            updateStatus(GameStatus.LOBBY_COUNTDOWN);
        } else if (status == GameStatus.LOBBY_COUNTDOWN) {
            if (!players.gameFull()) {
                seconds = 0;
                updateStatus(GameStatus.WAITING_FOR_PLAYERS);
            } else if (players.gameFull() && seconds == lobbyCountdown) {
                this.startGame();
            }
        } else if (status == GameStatus.STARTING || status == GameStatus.IN_PROGRESS || status == GameStatus.ENDING){
        	game.perSecond();
        }
    }
    
    private void startGame(){
    	game.start(info);
    }
    
    private void endGame(){
    	
    }

	public static void updateStatus(GameStatus newstatus) {
		status = newstatus;
	}
	
	public static Game getGame(){
		return game;
	}

	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
