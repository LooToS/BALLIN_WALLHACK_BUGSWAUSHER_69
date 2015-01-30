package ktar.five.TurfWars.Lobby;

import java.util.Map.Entry;

import ktar.five.TurfWars.Main;
import ktar.five.TurfWars.Game.Game;
import ktar.five.TurfWars.Game.Info.GamePlayers;
import ktar.five.TurfWars.Game.Info.GameStatus;
import ktar.five.TurfWars.Game.Info.WorldInfo;
import ktar.five.TurfWars.Game.cooldowns.Cooldown;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class Lobby implements Listener{

	private Game game;	
	public WorldInfo info;
	public GameStatus status;
	private int seconds;
	private int lobbyCountdown = 50;
	private GamePlayers players;
	private Main instance;
	
	public Lobby(Main plugin){
		this.instance = plugin;
		this.createTimer();
	}
	
	/*
	 * if (commandLabel.equalsIgnoreCase("game")) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Connect");
        out.writeUTF("game");
        p.sendPluginMessage(this, "BungeeCord", out.toByteArray());
    }
	 */
	@EventHandler
	public void playerJoinEvent(PlayerJoinEvent event){
		if(){
			
		}
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
    	WorldInfo info;
    	game.start(players);
    }
    
    private void endGame(){
    	
    }

	public void updateStatus(GameStatus status) {
		this.status = status;
		//update database
		
	}
	
}
