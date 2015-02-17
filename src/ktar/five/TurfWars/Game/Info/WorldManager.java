package ktar.five.TurfWars.Game.Info;

import ktar.five.TurfWars.Game.Player.Team;
import ktar.five.TurfWars.Lobby.Lobby;
import ktar.five.TurfWars.Main;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.ArrayList;
import java.util.List;

public class WorldManager {

	public List<Blocks> turfBlocks;
	public List<Location> placedBlocks;
	public Location min,max;

	public final Location lobbySpawn;
	public final Location redSpawn;
	public final Location blueSpawn;
	public final Location boxCorner1;
	public final Location boxCorner2;

	public final Location winning;
	public final Location loosing;

	public final List<Location> fireworks;

	public int red;
	public int blue;

	public WorldManager(Location lobbySpawn, Location redSpawn,
			Location blueSpawn, Location boxCorner1,
			Location boxCorner2, List<Location> fireworks, Location winning, Location loosing) {

		this.lobbySpawn = lobbySpawn;
		this.redSpawn = redSpawn;
		this.blueSpawn = blueSpawn;
		this.boxCorner1 = boxCorner1;
		this.boxCorner2 = boxCorner2;

		this.fireworks = fireworks;

		this.winning = winning;
		this.loosing = loosing;

		turfBlocks = new ArrayList<>();
		placedBlocks = new ArrayList<>();
		int xmin = Math.min(boxCorner2.getBlockX(), boxCorner1.getBlockZ());
		int ymin = Math.min(boxCorner2.getBlockY(), boxCorner1.getBlockY());
		int zmin = Math.min(boxCorner2.getBlockZ(), boxCorner1.getBlockZ());

		int xmax = Math.min(boxCorner2.getBlockX(), boxCorner1.getBlockZ());
		int ymax = Math.min(boxCorner2.getBlockY(), boxCorner1.getBlockY());
		int zmax = Math.min(boxCorner2.getBlockZ(), boxCorner1.getBlockZ());

		for(int x = xmin ; x <= xmax ; x++){
			List<Location> blocks = new ArrayList<>();
			for(int y = ymin ; y <= ymax ; y++){
				for(int z = zmin ; z <= zmax ; z++){
					blocks.add(boxCorner2.getWorld().getBlockAt(x, y, z).getLocation());
				}
			}
			turfBlocks.add(new Blocks(Team.SPECTATOR, blocks));
		}

		for(int i = 0 ; i < turfBlocks.size()/2 ; i++ ){
			for(Location b : turfBlocks.get(i).blocks){
				if(b.getBlock().getType().equals(Material.SPONGE)){
					b.getBlock().setType(Material.STAINED_CLAY);
					b.getBlock().setData(Team.BLUE.color);
					b.getBlock().setMetadata("floor", new FixedMetadataValue(Main.instance, Team.BLUE));
				}	
			}
			turfBlocks.get(i).team = Team.BLUE;
		}

		for(int i = turfBlocks.size()-1 ; i > turfBlocks.size()/2 ; i-- ){
			for(Location b : turfBlocks.get(i).blocks){
				if(b.getBlock().getType().equals(Material.SPONGE)){
					b.getBlock().setType(Material.STAINED_CLAY);
					b.getBlock().setData(Team.RED.color);
					b.getBlock().setMetadata("floor", new FixedMetadataValue(Main.instance, Team.RED));
				}	
			}
			turfBlocks.get(i).team = Team.RED;
		}

		blue = red = turfBlocks.size()/2;
	}

	public boolean removeIfIsPlacedBlock(Block block) {
		if (placedBlocks.contains(block.getLocation())) {
			placedBlocks.remove(block.getLocation());
			block.breakNaturally();
			return true;
		}else{
			return false;
		}
	}

	public boolean canBePlaced(Block block, Team team){
		for(Blocks blocks : turfBlocks){
			if(blocks.team == team){
				if(blocks.blocks.contains(block.getLocation())){
					return true;
				}
			}
		}
		return false;

	}

	public void addPlacedBlock(Block block) {
		placedBlocks.add(block.getLocation());
	}

	public int getCurrent(Team team){
		int num = 0;
		if(team == Team.BLUE){
			for (Blocks turfBlock : turfBlocks) {
				if (turfBlock.team == team) {
					num++;
				} else {
					return num;
				}
			}
		}else if(team == Team.RED){
			for (Blocks turfBlock : turfBlocks) {
				if (turfBlock.team == team) {
					num++;
				} else {
					return num;
				}
			}
		}
		return num;
	}

	public void addClays(Team team, int num) {
		if(team == Team.BLUE){
			int n = getCurrent(team);
			blue += num;
			red -= num;
			for(int i = 1 ; i <= num ; i++ ){
				setTeam(team, n+i);
				if(this.getCurrent(Team.RED) == 0){
					Lobby.teamWon(team);
				}
			}
		}else if(team == Team.RED){
			int n = turfBlocks.size() - getCurrent(team);
			blue -= num;
			red += num;
			for(int i = 1 ; i <= num ; i++ ){
				setTeam(team, n-i);
				if(this.getCurrent(Team.BLUE) == 0){
					Lobby.teamWon(team);
				}
			}
		}
	}

	public Team getWinning(){
		return red >= blue ? Team.RED : Team.BLUE;
	}

	public void resetMap(){
		for(Location block : placedBlocks){
			block.getBlock().setType(Material.AIR);
		}
		for(Blocks block : turfBlocks){
			for(Location loc : block.blocks){
				if(loc.getBlock().getType().equals(Material.STAINED_CLAY)){
					loc.getBlock().setType(Material.SPONGE);
				}
			}
		}
	}

	public void setTeam(Team team, int index){
		this.turfBlocks.get(index).team = team;
		for(Location b : turfBlocks.get(index).blocks){
			if(b.getBlock().getType().equals(Material.STAINED_CLAY)){
				b.getBlock().setType(Material.STAINED_CLAY);
				b.getBlock().setData(team.color);
			}	
		}
	}

}
