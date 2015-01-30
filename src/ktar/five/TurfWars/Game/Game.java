package ktar.five.TurfWars.Game;

import java.util.UUID;

import ktar.five.TurfWars.Main;
import ktar.five.TurfWars.Game.Info.ClayManager;
import ktar.five.TurfWars.Game.Info.GamePlayers;
import ktar.five.TurfWars.Game.Info.GameStatus;
import ktar.five.TurfWars.Game.Info.Phase;
import ktar.five.TurfWars.Game.Info.Phase.PhaseType;
import ktar.five.TurfWars.Game.Info.Team;
import ktar.five.TurfWars.Game.Info.TurfPlayer;
import ktar.five.TurfWars.Game.cooldowns.TurfEvent;
import ktar.five.TurfWars.Lobby.Lobby;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

public class Game implements Listener {

	public final String serverID;
	public final Main plugin;
	public int seconds, totalTime, blockgettercounter;
	public Phase phase;
	public GamePlayers players;
	public ClayManager clayManager;
	public Lobby lobby;

	public Game(String serverID, Main instance, Lobby lobby) {
		this.serverID = serverID;
		this.plugin = instance;
		this.lobby = lobby;
	}

	public void start(GamePlayers players) {
		this.seconds = 0;
		this.players = players;
		this.clayManager = new ClayManager(lobby.info);
		for (TurfPlayer p : players.redTeam.values())
			p.getPlayer().teleport(lobby.info.redSpawn);
		for (TurfPlayer p : players.blueTeam.values())
			p.getPlayer().teleport(lobby.info.blueSpawn);
		phase = Phase.startCount;
	}

	public void perSecond() {
		totalTime++;
		if (lobby.status == GameStatus.STARTING) {
			if (seconds == 0) {
				for (TurfPlayer player : players.getAll().values()) {
					player.canMove = false;
				}
			} else if (seconds != Phase.startCount.getSeconds()) {
				displayStartGametitlecountdown();
			} else {
				for (TurfPlayer player : players.getAll().values()) {
					player.canMove = true;
				}
				displayStartGametitle();
				seconds = 0;
				lobby.updateStatus(GameStatus.IN_PROGRESS);
				phase = Phase.n1;
				handlePhases();
			}
		} else if (lobby.status == GameStatus.IN_PROGRESS) {
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
		// TODO Auto-generated method stub
		
	}

	private void displayStartGametitle() {
		// TODO Auto-generated method stub
		
	}

	private void handlePhases() {
		if (phase.getType() == Phase.PhaseType.BUILDING && seconds == 0) {//if it is a build phase starting.
			for (TurfPlayer player : players.getAll().values())
				player.canVenture = false;

			for (TurfPlayer player : players.blueTeam.values())
				player.getPlayer().getInventory().addItem(new ItemStack(Material.STAINED_CLAY, phase.getAmount(), Team.BLUE.color));
			for (TurfPlayer player : players.redTeam.values())
				player.getPlayer().getInventory().addItem(new ItemStack(Material.STAINED_CLAY, phase.getAmount(), Team.RED.color));

		} else if (phase.getType() == Phase.PhaseType.KILLING && seconds == 0) {//else if is killing phase starting
			for (TurfPlayer player : players.getAll().values()) {
				player.setKitVenturing();
			}
		} else if (phase.getType() == Phase.PhaseType.KILLING) {
			blockgettercounter++;
			if (blockgettercounter == 6) {
				blockgettercounter = 0;
				for (TurfPlayer player : players.blueTeam.values())
					player.getPlayer().getInventory().addItem(new ItemStack(Material.STAINED_CLAY, 1, Team.BLUE.color));
				for (TurfPlayer player : players.redTeam.values())
					player.getPlayer().getInventory().addItem(new ItemStack(Material.STAINED_CLAY, 1, Team.RED.color));
			}
		}
	}

	//Finished
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		TurfPlayer p = players.getTurfPlayer(event.getPlayer().getUniqueId());

		switch (event.getTo().getBlock().getType()){
		case LAVA:
		case STATIONARY_LAVA:
		case WATER:
		case STATIONARY_WATER:
			this.playerDied(p.getPlayer());
			break;
		default:
			break;
		}

		p.handleMoving(event.getTo());

		if (!p.canMove && event.getTo().getY() > event.getFrom().getY()) {
			event.setTo(event.getFrom());
		}
	}

	@EventHandler
	public void onArrowGetCooldown(TurfEvent event) {
		TurfPlayer player = players.getTurfPlayer(event.getUUID());
		if(player.arrows >= player.kit.maxArrows){
			return;
		}else{
			player.giveArrow();
		}
	}

	//Finished
	@EventHandler
	public void entityShootBow(EntityShootBowEvent event){
		if(event.getEntity() instanceof Player){
			Player p = (Player) event.getEntity();
			event.getProjectile().setMetadata("Arrow", new FixedMetadataValue(this.plugin, p.getUniqueId()));;
			this.players.getTurfPlayer(p.getUniqueId()).shotArrow();
		}
	}

	//Done
	@EventHandler
	public void entityDamagedEvent(EntityDamageEvent event) {
		if (event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();
			DamageCause cause = event.getCause();
			if (cause.equals(DamageCause.FALL)) {
				event.setDamage(0D);
				event.setCancelled(true);
			} else if (cause.equals(DamageCause.LAVA)) {
				event.setDamage(0D);
			} else if (cause.equals(DamageCause.VOID)) {
				event.setDamage(0D);
				playerDied(player);
			} else if (cause.equals(DamageCause.FIRE_TICK)){
				event.setDamage(0D);
			}
		}
	}

	@EventHandler
	public void projectileHitEvent(ProjectileHitEvent event) {
		Player shooter = Bukkit.getPlayer((UUID) event.getEntity().getMetadata("Arrow").get(0).value());
		if(event.getEntity().isOnGround()){
			clayManager.removeIfIsPlacedBlock(event.getEntity().getLocation().getBlock());
			players.getTurfPlayer(shooter.getUniqueId()).brokeBlock();
		}
		
		event.getEntity().getLocation();
	}

	@EventHandler
	public void entityHitByOtherEntity(EntityDamageByEntityEvent event) {
		if (event.getEntity() instanceof Player) {
			Player damaged = (Player) event.getEntity();
			if (event.getCause().equals(DamageCause.ENTITY_ATTACK) && event.getDamager() instanceof Player) {
				Player damager = (Player) event.getDamager();
				if (players.areOnSameTeam(damaged.getUniqueId(), damager.getUniqueId())) {
					event.setDamage(0D);
					event.setCancelled(true);
					return;
				}else if (((Damageable) damaged).getHealth() <= 0) {
					TurfPlayer player = players.getAll().get(damager.getUniqueId());
					player.addKill();
					damaged.setHealth(20D);
					playerDied(damaged);
				}
			}else if (event.getCause().equals(DamageCause.PROJECTILE) && event.getDamager() instanceof Arrow){
				Arrow arrow = (Arrow) event.getDamager();
				Player damager = Bukkit.getPlayer((UUID) arrow.getMetadata("Arrow").get(0).value());
				if(damager == damaged){
					event.setCancelled(true);
					return;
				} else if (players.areOnSameTeam(damaged.getUniqueId(), damager.getUniqueId())) {
					event.setDamage(0D);
					event.setCancelled(true);
					return;
				}else if (((Damageable) damaged).getHealth() <= 0) {
					TurfPlayer player = players.getAll().get(damager.getUniqueId());
					player.addKill();
					damaged.setHealth(20D);
					playerDied(damaged);
				}
			}
		}
	}

	public void endGame(Team winning) {
		lobby.updateStatus(GameStatus.ENDING);
		for (TurfPlayer player : players.getTurfPlayers(winning, false).values()) {
			player.addWin(this.totalTime);
		}
		for (TurfPlayer player : players.getTurfPlayers(winning, true).values()) {
			player.addDefeat(this.totalTime);
		}
		players.updateDatabase(this.plugin);
		//end the game completely


		this.clayManager = null;
		this.phase = null;
		this.players = null;
	}


	@EventHandler
	public void blockPlaceEvent(BlockPlaceEvent event) {
		if(this.phase.getType() == PhaseType.BUILDING){
			if(!this.clayManager.canBePlaced(event.getBlockPlaced(), players.getPlayerTeam(event.getPlayer().getUniqueId()))){
				event.setCancelled(true);
			}else{
				clayManager.addPlacedBlock(event.getBlockPlaced());
				players.getTurfPlayer(event.getPlayer().getUniqueId()).placedBlock();
			}
		}else{
			return;
		}
	}

	public void playerDied(Player p) {
		p.setHealth(20D);
		p.setFireTicks(0);
		TurfPlayer player = players.getAll().get(p.getUniqueId());
		Team team = players.getPlayerTeam(player);
		if (team == Team.BLUE) {
			p.teleport(lobby.info.blueSpawn);
			clayManager.addClays(Team.RED, phase.getAmount());
		} else if (team == Team.RED) {
			p.teleport(lobby.info.redSpawn);
			clayManager.addClays(Team.BLUE, phase.getAmount());
		}
		player.addDeath();
		player.resetInventory();
	}

	/* @EventHandler
    public void onPlayerDIE(PlayerDeathEvent event){
    	event.getEntity().setHealth(20D);
    	playerDied(event.getEntity());
    }*/

}