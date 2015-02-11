package ktar.five.TurfWars;

import ktar.five.TurfWars.Game.Player.Kit;
import ktar.five.TurfWars.Game.Player.TurfPlayer;
import ktar.five.TurfWars.Lobby.Lobby;
import ktar.five.TurfWars.Lobby.LobbyUtils.MobType;
import ktar.five.TurfWars.guis.ServerSelectGui;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class EntityListener implements Listener {

	@EventHandler
	public void onEntityDamageEvent(EntityDamageEvent event){
		if(event.getEntity().hasMetadata("LobbyMob") || event.getEntity().hasMetadata("HubMob")){
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onEntityTarget(EntityTargetEvent event){
		if(event.getEntity().hasMetadata("LobbyMob") || event.getEntity().hasMetadata("HubMob")){
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onEntityTargetEntity(EntityTargetLivingEntityEvent event){
		if(event.getEntity().hasMetadata("LobbyMob") || event.getEntity().hasMetadata("HubMob")){
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event){
		if(event.getEntity().hasMetadata("LobbyMob") || event.getEntity().hasMetadata("HubMob")){
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onEntityCombust(EntityCombustEvent event){
		if(event.getEntity().hasMetadata("LobbyMob") || event.getEntity().hasMetadata("HubMob")){
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onPlayerRightClickEntity(PlayerInteractEntityEvent event){
		if(event.getRightClicked().hasMetadata("LobbyMob")){
			LivingEntity entity = (LivingEntity) event.getRightClicked();
			String string = entity.getMetadata("LobbyMob").get(0).toString();
			MobType type = MobType.valueOf(string);
			TurfPlayer player = Lobby.players.getTurfPlayer(event.getPlayer().getUniqueId());
			if (type.equals(MobType.BLUESHEEP) || type.equals(MobType.REDSHEEP)) {
				if(!Lobby.players.switchTeam(Lobby.players.getTurfPlayer(event.getPlayer().getUniqueId()))){
                    event.getPlayer().sendMessage("Team full");
                }
			}else if(type.equals(MobType.MARKSMAN)){
				player.kit = Kit.MARKSMAN;
			}else if (type.equals(MobType.INFILTRATOR)){
				if(player.hasKitUnlocked(Kit.INFILTRATOR)){
					player.kit = Kit.INFILTRATOR;
				}else if (player.canBuy(Kit.INFILTRATOR.cost)){
					player.unlockKit(Kit.INFILTRATOR);
					player.removeMoney(Kit.INFILTRATOR.cost);
					player.kit = Kit.INFILTRATOR;
				}
			}else if (type.equals(MobType.SHREDDER)){
				if(player.hasKitUnlocked(Kit.SHREDDER)){
					player.kit = Kit.SHREDDER;
				}else if (player.canBuy(Kit.SHREDDER.cost)){
					player.unlockKit(Kit.SHREDDER);
					player.removeMoney(Kit.SHREDDER.cost);
					player.kit = Kit.SHREDDER;
				}
			}
			event.setCancelled(true);
		}else if(event.getRightClicked().hasMetadata("HubMob")){
			ServerSelectGui gui = new ServerSelectGui();
			gui.open(event.getPlayer());
			event.setCancelled(true);
		}
	}
	
	
}
