package ktar.five.TurfWars.Game;

import com.connorlinfoot.titleapi.TitleAPI;
import ktar.five.TurfWars.Game.Info.GameStatus;
import ktar.five.TurfWars.Game.Info.Phase;
import ktar.five.TurfWars.Game.Info.WorldManager;
import ktar.five.TurfWars.Game.Player.Team;
import ktar.five.TurfWars.Game.Player.TurfPlayer;
import ktar.five.TurfWars.Lobby.Lobby;
import ktar.five.TurfWars.Main;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

public class Game {

	public final String serverID;
	public final Main plugin;
	public int seconds, totalTime, blockgettercounter;
	public Phase phase;
	public WorldManager worldManager;

	public Game(String serverID, Main instance, Lobby lobby) {
		this.serverID = serverID;
		this.plugin = instance;
	}

	public void start(WorldManager manager) {
		this.seconds = 0;
		this.worldManager = manager;
		for (TurfPlayer p : Lobby.players.redTeam.values())
			p.getPlayer().teleport(worldManager.redSpawn);
		for (TurfPlayer p : Lobby.players.blueTeam.values())
			p.getPlayer().teleport(worldManager.blueSpawn);
		phase = Phase.startCount;
	}

	public void perSecond() {
		totalTime++;
		if (Lobby.status == GameStatus.STARTING) {
			if (seconds == 0) {
				for (TurfPlayer player : Lobby.players.getAll().values()) {
					player.canMove = false;
				}
			} else if (seconds != Phase.startCount.getSeconds()) {
				displayStartGametitlecountdown();
			} else {
				for (TurfPlayer player : Lobby.players.getAll().values()) {
					player.canMove = true;
				}
				displayStartGametitle();
				seconds = 0;
				Lobby.updateStatus(GameStatus.IN_PROGRESS);
				phase = Phase.n1;
				handlePhases();
			}
		} else if (Lobby.status == GameStatus.IN_PROGRESS) {
			if (seconds == phase.getSeconds()) {
				phase = Phase.valueOf("n" + (phase.getPhaseNumber() + 1));
				seconds = 0;
				handlePhases();//give the items n such
			} else {
				handlePhases();//items n such
			}
		}
	}

	private void displayStartGametitlecountdown() {
		for(TurfPlayer player : Lobby.players.getAll().values()){
			TitleAPI.sendTitle(player.getPlayer(), 0, 20, 0, "GAME STARTS IN", (Phase.startCount.getSeconds() - seconds) + " SECONDS");
		}

	}

	private void displayStartGametitle() {
		for(TurfPlayer player : Lobby.players.getAll().values()){
			TitleAPI.sendTitle(player.getPlayer(), 0, 20, 0, "GAME STARTS IN", (Phase.startCount.getSeconds() - seconds) + " SECONDS");
		}

	}

	private void handlePhases() {
		if (phase.getType() == Phase.PhaseType.BUILDING && seconds == 0) {//if it is a build phase starting.
			for (TurfPlayer player : Lobby.players.getAll().values())
				player.canVenture = false;

			for (TurfPlayer player : Lobby.players.blueTeam.values())
				player.getPlayer().getInventory().addItem(new ItemStack(Material.STAINED_CLAY, phase.getAmount(), Team.BLUE.color));
			for (TurfPlayer player : Lobby.players.redTeam.values())
				player.getPlayer().getInventory().addItem(new ItemStack(Material.STAINED_CLAY, phase.getAmount(), Team.RED.color));

		} else if (phase.getType() == Phase.PhaseType.KILLING && seconds == 0) {//else if is killing phase starting
			for (TurfPlayer player : Lobby.players.getAll().values()) {
				player.setKitVenturing();
				player.resetInventory();
			}
		} else if (phase.getType() == Phase.PhaseType.KILLING) {
			blockgettercounter++;
			if (blockgettercounter == 6) {
				blockgettercounter = 0;
				for (TurfPlayer player : Lobby.players.blueTeam.values())
					player.getPlayer().getInventory().addItem(new ItemStack(Material.STAINED_CLAY, 1, Team.BLUE.color));
				for (TurfPlayer player : Lobby.players.redTeam.values())
					player.getPlayer().getInventory().addItem(new ItemStack(Material.STAINED_CLAY, 1, Team.RED.color));
			}
		}
	}

	public void endGame(Team winning) {
		Lobby.updateStatus(GameStatus.ENDING);
		for (TurfPlayer player : Lobby.players.getTurfPlayers(winning, false).values()) {
			player.addWin(this.totalTime);
		}
		for (TurfPlayer player : Lobby.players.getTurfPlayers(winning, true).values()) {
			player.addDefeat(this.totalTime);
		}
		this.worldManager.resetMap();
		
		Lobby.players.updateDatabase();
		//end the game completely


		this.worldManager = null;
		this.phase = null;
	}

	public void playerDied(Player p) {
		p.setHealth(20D);
		p.setFireTicks(0);
		TurfPlayer player = Lobby.players.getAll().get(p.getUniqueId());
		Team team = Lobby.players.getPlayerTeam(player);
		if (team == Team.BLUE) {
			p.teleport(worldManager.blueSpawn);
			worldManager.addClays(Team.RED, phase.getAmount());
		} else if (team == Team.RED) {
			p.teleport(worldManager.redSpawn);
			worldManager.addClays(Team.BLUE, phase.getAmount());
		}
		player.addDeath();
		player.resetInventory();
	}
	
}