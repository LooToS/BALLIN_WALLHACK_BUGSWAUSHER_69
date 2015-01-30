package ktar.five.TurfWars.Game.Info;

import ktar.five.TurfWars.Game.Game;
import ktar.five.TurfWars.Game.cooldowns.Cooldown;
import ktar.five.TurfWars.Game.kits.Kit;
import ktar.five.TurfWars.Main;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class TurfPlayer {

	public UUID playerUUID;
	public int id;
	public Game game;
	public Kit kit;//Defines the kit that they respawn with
	public int wins, defeats, totalKills, totalDeaths,
	topKillsPerMatch, currentKillsThisMatch, /*kit kills*/
	shortestGame, longestGame,
	blocksDestroyed, blocksPlaced, arrowsShot,
	topKillStreak, currentKillStreak;
	public int arrows;
	public double multiplier;
	public boolean canVenture, canMove, isSuperSlowed;

	public TurfPlayer(int id, Main instance) {
		try {
			this.id = id;
			ResultSet rs = instance.sql.querySQL("SELECT * FROM UserStats WHERE id = " + id);
			this.wins = rs.getInt("wins");
			this.defeats = rs.getInt("defeats");
			this.totalKills = rs.getInt("totalKills");
			this.totalDeaths = rs.getInt("totalDeaths");
			this.topKillsPerMatch = rs.getInt("topKillsPerMatch");
			this.currentKillsThisMatch = 0;
			this.shortestGame = rs.getInt("shortestGame");
			this.longestGame = rs.getInt("longestGame");
			this.currentKillStreak = 0;
			this.topKillStreak = rs.getInt("topKillStreak");
			this.arrowsShot = rs.getInt("arrowsShot");
			this.blocksDestroyed = rs.getInt("blocksDestroyed");
			this.blocksPlaced = rs.getInt("blocksPlaced");
			this.multiplier = rs.getDouble("multiplier");
			rs.close();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}

	public Player getPlayer() {
		return Bukkit.getPlayer(playerUUID);
	}

	public boolean isOnOwnTurf() {
		Location loc = Bukkit.getPlayer(playerUUID).getLocation().subtract(0, 1, 0);
		for (int i = 0; i < 3; i++) {
			loc = loc.subtract(0, 1, 0);
			if (loc.getBlock().getData() == game.players.getTeamByte(this)) {
				return true;
			}
		}
		return false;
	}

	public void setKitVenturing() {
		canVenture = kit.canVenture;
	}

	public void handleMoving(Location to) {
		if (!isSuperSlowed && !canMove) {
			getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 9999, 256));
			isSuperSlowed = true;
		} else if (isSuperSlowed && canMove) {
			getPlayer().removePotionEffect(PotionEffectType.SLOW);
			isSuperSlowed = false;
		}

		if (!isOnOwnTurf() && !canVenture) {
			getPlayer().setVelocity(to.getDirection().multiply(-2.0D));
		}
	}

	public void addWin(int gameTime) {
		wins++;
		if (currentKillsThisMatch > topKillsPerMatch)
			topKillsPerMatch = currentKillsThisMatch;
		if (gameTime < shortestGame)
			shortestGame = gameTime;
		if (gameTime > longestGame)
			longestGame = gameTime;

	}

	public void addDefeat(int gameTime) {
		defeats++;
		if (currentKillsThisMatch > topKillsPerMatch)
			topKillsPerMatch = currentKillsThisMatch;
		if (gameTime < shortestGame)
			shortestGame = gameTime;
		if (gameTime > longestGame)
			longestGame = gameTime;
	}

	public void addDeath() {
		totalDeaths++;
		if (currentKillStreak > topKillStreak) {
			topKillStreak = currentKillStreak;
		}
		currentKillStreak = 0;
	}

	public void addKill() {
		currentKillStreak++;
		totalKills++;
		currentKillsThisMatch++;
	}

	public void brokeBlock() {
		this.blocksDestroyed++;
	}

	public void placedBlock() {
		this.blocksPlaced++;
	}

	public void shotArrow() {
		this.arrowsShot++;
		this.arrows--;
		if (!Cooldown.isCooling(this.playerUUID, "arrow")) {
			this.addArrowCooldown();
		}
	}

	public void addArrowCooldown() {
		Cooldown.add(this.playerUUID, "arrow", this.kit.oneArrowPerX);
	}

	public String getQuery(){
		return "query";
		//do this
	}

	public void resetInventory() {
		Player p = this.getPlayer();
		Cooldown.removeCooldown(this.playerUUID, "arrow");
		this.arrows = 0;
		this.addArrowCooldown();
		p.getInventory().clear();
		p.getInventory().setContents(kit.getItems().toArray(new ItemStack[kit.getItems().size()]));
	}

	public void giveArrow() {
		this.getPlayer().getInventory().addItem(new ItemStack(Material.ARROW, 1));
		this.arrows++;
		if(this.arrows < this.kit.maxArrows){
			Cooldown.removeCooldown(this.playerUUID, "arrow");
			this.addArrowCooldown();
		}
	}
}



