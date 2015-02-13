package ktar.five.TurfWars.Game.Player;

import ktar.five.TurfWars.Game.Cooling.Cooldown;
import ktar.five.TurfWars.Game.Game;
import ktar.five.TurfWars.Lobby.Lobby;
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
	topKillStreak, currentKillStreak, kitsUnlocked;
	public int arrows;
	public double multiplier, moneyGotThisRound;
	public boolean canVenture, canMove, isSuperSlowed;

	public TurfPlayer(UUID uu) {
		wins = defeats = totalKills = totalDeaths = topKillsPerMatch = currentKillsThisMatch = /*kit kills*/
		shortestGame = longestGame = blocksDestroyed = blocksPlaced = arrowsShot =
		topKillStreak = currentKillStreak = kitsUnlocked = 0;

		try {
			this.playerUUID = uu;
			ResultSet rs = Main.instance.sql.querySQL("SELECT * FROM UserStats WHERE uuid = " + uu.toString());
			this.id = rs.getInt("id");
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
			this.kitsUnlocked = rs.getInt("kitsUnlocked");
			rs.close();
			this.kit = Kit.MARKSMAN;
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}

	public Player getPlayer() {
		return Bukkit.getPlayer(playerUUID);
	}

	public int getUnlockedKits(){
		return this.kitsUnlocked;
	}

	public void addMoney(double amnt){
		this.moneyGotThisRound += amnt;
	}

	public boolean hasKitUnlocked(Kit kit){
		if(kitsUnlocked == 3){
			return true;
		}else if (kit == Kit.SHREDDER) {
			return kitsUnlocked == 2;
		} else
			return kit != Kit.INFILTRATOR || kitsUnlocked == 1;
	}

	public double getBalance(){
		return Main.economy.getBalance(this.getPlayer().getPlayer());
	}

	public void removeMoney(double amount){
		Main.economy.withdrawPlayer(this.getPlayer().getPlayer(), amount);
	}

	public boolean canBuy(double amount){
		return getBalance()>=amount;
	}

	public boolean isOnOwnTurf() {
		Location loc = Bukkit.getPlayer(playerUUID).getLocation().subtract(0, 1, 0);
		for (int i = 0; i < 3; i++) {
			loc = loc.subtract(0, 1, 0);
			if (loc.getBlock().getData() == Lobby.players.getTeamByte(this)) {
				return true;
			}
		}
		return false;
	}

	public void setKitVenturing() {
		canVenture = kit.canVenture;
	}

	public void unlockKit(Kit kit){
		if(this.hasKitUnlocked(Kit.INFILTRATOR) && kit.equals(Kit.SHREDDER)){
			kitsUnlocked = 3;
		}else if (this.hasKitUnlocked(Kit.SHREDDER) && kit.equals(Kit.INFILTRATOR)){
			kitsUnlocked = 3;
		}else if (!this.hasKitUnlocked(Kit.INFILTRATOR) && kit.equals(Kit.SHREDDER)){
			kitsUnlocked = 2;
		}else if (kit.equals(Kit.INFILTRATOR)){
			kitsUnlocked = 1;
		}
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
		addMoney(10);
		if (currentKillsThisMatch > topKillsPerMatch)
			topKillsPerMatch = currentKillsThisMatch;
		if (gameTime < shortestGame)
			shortestGame = gameTime;
		if (gameTime > longestGame)
			longestGame = gameTime;
		Main.economy.depositPlayer(this.getPlayer().getPlayer(), this.moneyGotThisRound*multiplier);
	}

	public void addDefeat(int gameTime) {
		defeats++;
		if (currentKillsThisMatch > topKillsPerMatch)
			topKillsPerMatch = currentKillsThisMatch;
		if (gameTime < shortestGame)
			shortestGame = gameTime;
		if (gameTime > longestGame)
			longestGame = gameTime;
		
		Main.economy.depositPlayer(this.getPlayer().getPlayer(), this.moneyGotThisRound*multiplier);
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
		addMoney(0.5);
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
		return "INSERT INTO UserStats (uuid, wins, defeats, totalKills, totalDeaths, topKillsPerMatch, shortestGame, longestGame" +
				"topKillStreak, arrowsShot, blocksDestroyed, blocksPlaced, kitsUnlocked) " +
				"VALUES (" + this.playerUUID.toString() + ", " + this.wins  + ", " + this.defeats  + ", " + this.totalKills  + ", " + this.totalDeaths  + ", " +
				this.topKillsPerMatch  + ", " + this.shortestGame  + ", " + this.longestGame  + ", " + this.topKillStreak  + ", " + this.arrowsShot
				+ ", " + this.blocksDestroyed  + ", " + this.blocksPlaced  + ", " + this.kitsUnlocked + ") " +
				"ON DUPLICATE KEY UPDATE UserStats SET ( wins = "+ this.wins  + ", defeats = " + this.defeats  + ", totalKills = " + this.totalKills  + ", totalDeaths =" + this.totalDeaths  + ", topKillsPerMatch = " +
		this.topKillsPerMatch  + ", shortestGame = " + this.shortestGame  + ", longestGame = " + this.longestGame  + ", topKillStreak = " + this.topKillStreak  + ", arrowsShot = " + this.arrowsShot
				+ ", blocksDestroyed = " + this.blocksDestroyed  + ", blocksPlaced = " + this.blocksPlaced  + ", kitsUnlocked = " + this.kitsUnlocked + ")" ;
	}

	public void resetInventory() {
		Player p = this.getPlayer();
		Cooldown.removeCooldown(this.playerUUID, "arrow");
		this.arrows = 0;
		this.addArrowCooldown();
		p.getInventory().clear();
		p.getInventory().setContents(kit.getItems().toArray(new ItemStack[kit.getItems().size()]));
	}

	public void returnToLobby(){

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



