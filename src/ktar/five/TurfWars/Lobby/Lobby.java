package ktar.five.TurfWars.Lobby;

import ktar.five.TurfWars.Main;
import ktar.five.TurfWars.Game.Game;
import ktar.five.TurfWars.Game.Info.GamePlayers;
import ktar.five.TurfWars.Game.Info.GameStatus;
import ktar.five.TurfWars.Game.Info.WorldInfo;
import ktar.five.TurfWars.Game.cooldowns.Cooldown;
import ktar.five.inventory.MobInventories;
import net.minecraft.server.v1_8_R1.EntityInsentient;
import net.minecraft.server.v1_8_R1.EntityLiving;
import net.minecraft.server.v1_8_R1.NBTTagCompound;

import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftLivingEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Sheep;
import org.bukkit.entity.Zombie;
import org.bukkit.event.Listener;
import org.bukkit.metadata.FixedMetadataValue;

public class Lobby implements Listener{

	private Game game;	
	public WorldInfo info;
	public GameStatus status;
	private int seconds;
	private int lobbyCountdown = 50;
	private GamePlayers players;
	private Main instance;
	
	public Lobby(Main plugin, FileConfiguration config){
		this.instance = plugin;
		this.createTimer();
		World world = Bukkit.getWorld(config.getString("lobbyOptions.world"));
		ConfigurationSection locations = config.getConfigurationSection("lobbyOptions.locations");
		spawnMobs(configToLocation(locations.getConfigurationSection("blueSheep"), world),
				configToLocation(locations.getConfigurationSection("redSheep"), world), 
				configToLocation(locations.getConfigurationSection("marksman"), world), 
				configToLocation(locations.getConfigurationSection("infiltrator"), world), 
				configToLocation(locations.getConfigurationSection("shredder"), world));
	}
	
	/*
	 * if (commandLabel.equalsIgnoreCase("game")) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Connect");
        out.writeUTF("game");
        p.sendPluginMessage(this, "BungeeCord", out.toByteArray());
    }
	 */
	private enum MobType{
		BLUESHEEP,
		REDSHEEP,
		MARKSMAN,
		INFILTRATOR,
		SHREDDER;
	}
	
	private void spawnMob(EntityType type, Location loc, MobType meta){
		LivingEntity entity = (LivingEntity) loc.getWorld().spawnEntity(loc, type);
		if(meta.equals(MobType.BLUESHEEP) || meta.equals(MobType.REDSHEEP)){
			Sheep sheep = (Sheep) entity;
			if(meta.equals(MobType.BLUESHEEP)){
				sheep.setColor(DyeColor.BLUE);
			}else if(meta.equals(MobType.REDSHEEP)){
				sheep.setColor(DyeColor.RED);
			}
			sheep.setAdult();
		}else{
			Zombie zombie = (Zombie) entity;
			zombie.setBaby(false);
			zombie.setCustomName(meta.toString());
			zombie.setCustomNameVisible(true);
		}
	
		entity.setCanPickupItems(false);
		entity.setMaxHealth(2000d);
		entity.setHealth(2000d);
		entity.setRemoveWhenFarAway(false);
		entity.setMetadata("Ktar", new FixedMetadataValue(Main.instance, meta.toString()));
		
		EntityInsentient nmsEntity = (EntityInsentient) ((CraftLivingEntity) entity).getHandle();
		NBTTagCompound tag = new NBTTagCompound();
		nmsEntity.c(tag);
		tag.setBoolean("NoAI", true);
		EntityLiving el = nmsEntity;
		el.a(tag);
		
	}
	
	private void spawnMobs(Location blueSheep,
			Location redSheep, Location marksman,
			Location infiltrator, Location shredder) {
		this.spawnMob(EntityType.SHEEP, loc, meta);
		
	}

	public static Location configToLocation(ConfigurationSection section){
		return new Location (
			Bukkit.getServer().getWorld(section.getString("world")),
			Double.valueOf(section.getString("x")),
			Double.valueOf(section.getString("y")),
			Double.valueOf(section.getString("z")));
	}
	
	public static Location configToLocation(ConfigurationSection section, World world){
		return new Location (
			world,
			Double.valueOf(section.getString("x")),
			Double.valueOf(section.getString("y")),
			Double.valueOf(section.getString("z")));
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
