package ktar.five.TurfWars.Game;

import java.util.UUID;

import ktar.five.TurfWars.Main;
import ktar.five.TurfWars.Game.Cooling.TurfEvent;
import ktar.five.TurfWars.Game.Info.Phase.PhaseType;
import ktar.five.TurfWars.Game.Player.Kit;
import ktar.five.TurfWars.Game.Player.TurfPlayer;
import ktar.five.TurfWars.Lobby.Lobby;

import org.bukkit.Bukkit;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.metadata.FixedMetadataValue;

public class GameListeners {


	@EventHandler
	public void blockPlaceEvent(BlockPlaceEvent event) {
		if(Lobby.players.playerInGame(event.getPlayer().getUniqueId())){
			if(Lobby.getGame().phase.getType() == PhaseType.BUILDING){
				if(!Lobby.getGame().worldManager.canBePlaced(event.getBlockPlaced(), Lobby.players.getPlayerTeam(event.getPlayer().getUniqueId()))){
					event.setCancelled(true);
				}else{
					Lobby.getGame().worldManager.addPlacedBlock(event.getBlockPlaced());
					Lobby.players.getTurfPlayer(event.getPlayer().getUniqueId()).placedBlock();
				}
			}else{
				event.setCancelled(true);
			}
		}
	}
	@EventHandler
	public void entityHitByOtherEntity(EntityDamageByEntityEvent event) {
		if (event.getEntity() instanceof Player) {
			Player damaged = (Player) event.getEntity();

			if(Lobby.players.playerInGame(damaged.getUniqueId())){
				if (event.getCause().equals(DamageCause.ENTITY_ATTACK) && event.getDamager() instanceof Player) {
					Player damager = (Player) event.getDamager();
					if (Lobby.players.areOnSameTeam(damaged.getUniqueId(), damager.getUniqueId())) {
						event.setDamage(0D);
						event.setCancelled(true);
					}else if (((Damageable) damaged).getHealth() <= 0) {
						TurfPlayer player = Lobby.players.getAll().get(damager.getUniqueId());
						player.addKill();
						damaged.setHealth(20D);
						Lobby.getGame().playerDied(damaged);
					}
				}else if (event.getCause().equals(DamageCause.PROJECTILE) && event.getDamager() instanceof Arrow){
					Arrow arrow = (Arrow) event.getDamager();
					Player damager = Bukkit.getPlayer((UUID) arrow.getMetadata("Arrow").get(0).value());
					if(damager == damaged){
						event.setCancelled(true);
					} else if (Lobby.players.areOnSameTeam(damaged.getUniqueId(), damager.getUniqueId())) {
						event.setDamage(0D);
						event.setCancelled(true);
					}else if (((Damageable) damaged).getHealth() <= 0) {
						TurfPlayer player = Lobby.players.getAll().get(damager.getUniqueId());
						player.addKill();
						damaged.setHealth(20D);
						Lobby.getGame().playerDied(damaged);
					}
				}
			}
		}
	}

	@EventHandler
	public void entityDamagedEvent(EntityDamageEvent event) {
		if (event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();
			DamageCause cause = event.getCause();
			if(Lobby.players.playerInGame(player.getUniqueId())){

				if (cause.equals(DamageCause.FALL)) {
					event.setDamage(0D);
					event.setCancelled(true);
				} else if (cause.equals(DamageCause.LAVA)) {
					event.setDamage(0D);
				} else if (cause.equals(DamageCause.VOID)) {
					event.setDamage(0D);
					Lobby.getGame().playerDied(player);
				} else if (cause.equals(DamageCause.FIRE_TICK)){
					event.setDamage(0D);
				}
			}
		}
	}

	@EventHandler
	public void projectileHitEvent(ProjectileHitEvent event) {
		Player shooter = Bukkit.getPlayer((UUID) event.getEntity().getMetadata("Arrow").get(0).value());
		if(Lobby.players.playerInGame(shooter.getUniqueId())){
			if(event.getEntity().isOnGround()){
				Lobby.getGame().worldManager.removeIfIsPlacedBlock(event.getEntity().getLocation().getBlock());
				Lobby.players.getTurfPlayer(shooter.getUniqueId()).brokeBlock();
			}

			event.getEntity().getLocation();
		}
	}

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		if(Lobby.players.playerInGame(event.getPlayer().getUniqueId())){
			TurfPlayer p = Lobby.players.getTurfPlayer(event.getPlayer().getUniqueId());

			switch (event.getTo().getBlock().getType()){
			case LAVA:
			case STATIONARY_LAVA:
			case WATER:
			case STATIONARY_WATER:
				Lobby.getGame().playerDied(p.getPlayer());
				break;
			default:
				break;
			}

			p.handleMoving(event.getTo());

			if (!p.canMove && event.getTo().getY() > event.getFrom().getY()) {
				event.setTo(event.getFrom());
			}
		}
	}

	@EventHandler
	public void onArrowGetCooldown(TurfEvent event) {
		if(Lobby.players.playerInGame(event.getUUID())){
			TurfPlayer player = Lobby.players.getTurfPlayer(event.getUUID());
			if(player.arrows < player.kit.maxArrows){
				player.giveArrow();
			}
		}
	}

	@EventHandler
	public void entityShootBow(EntityShootBowEvent event){
		if(event.getEntity() instanceof Player){
			Player p = (Player) event.getEntity();
			if(Lobby.players.playerInGame(p.getUniqueId())){
				TurfPlayer player = Lobby.players.getTurfPlayer(p.getUniqueId());
				if(player.kit == Kit.SHREDDER){
					for(int i = player.arrows ; i >= 0 ; i--){
						Projectile proj = p.getWorld().spawnArrow(p.getLocation(),p.getLocation().getDirection(),
								2/*put your arrow speed here*/, 2/*put your spread here*/);
						proj.setMetadata("Arrow", new FixedMetadataValue(Main.instance, p.getUniqueId()));
						player.shotArrow();
					}
				}else{
					event.getProjectile().setMetadata("Arrow", new FixedMetadataValue(Main.instance, p.getUniqueId()));
					player.shotArrow();
				}
			}
		}
	}


}
