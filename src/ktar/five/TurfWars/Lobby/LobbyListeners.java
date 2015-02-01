package ktar.five.TurfWars.Lobby;

import ktar.five.TurfWars.Lobby.LobbyUtils.MobType;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class LobbyListeners implements Listener{
	
	@EventHandler
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event){
		if(event.getEntity().hasMetadata("LobbyMob")){
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onEntityCombust(EntityCombustEvent event){
		if(event.getEntity().hasMetadata("LobbyMob")){
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onPlayerRightClickEntity(PlayerInteractEntityEvent event){
		if(event.getRightClicked().hasMetadata("LobbyMob")){
			LivingEntity entity = (LivingEntity) event.getRightClicked();
			String string = entity.getMetadata("LobbyMob").get(0).toString();
			MobType type = MobType.valueOf(string);
			if(!Lobby.players.switchTeam(Lobby.players.getTurfPlayer(event.getPlayer().getUniqueId()))){
				event.getPlayer().sendMessage("Team full");
			}
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onEntityDamageEvent(EntityDamageEvent event){
		if(event.getEntity().hasMetadata("LobbyMob")){
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onEntityTarget(EntityTargetEvent event){
		if(event.getEntity().hasMetadata("LobbyMob")){
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onEntityTargetEntity(EntityTargetLivingEntityEvent event){
		if(event.getEntity().hasMetadata("LobbyMob")){
			event.setCancelled(true);
		}
	}
	
	
}
