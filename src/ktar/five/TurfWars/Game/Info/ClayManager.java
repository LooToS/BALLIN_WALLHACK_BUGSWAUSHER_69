package ktar.five.TurfWars.Game.Info;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.metadata.FixedMetadataValue;

public class ClayManager {

	public List<Blocks> turfBlocks;
	public List<Location> placedBlocks;
	public Location min,max;

	public ClayManager(WorldInfo info) {
		turfBlocks = new ArrayList<>();
		placedBlocks = new ArrayList<>();
		int xmin = Math.min(info.boxCorner2.getBlockX(), info.boxCorner1.getBlockZ());
		int ymin = Math.min(info.boxCorner2.getBlockY(), info.boxCorner1.getBlockY());
		int zmin = Math.min(info.boxCorner2.getBlockZ(), info.boxCorner1.getBlockZ());

		int xmax = Math.min(info.boxCorner2.getBlockX(), info.boxCorner1.getBlockZ());
		int ymax = Math.min(info.boxCorner2.getBlockY(), info.boxCorner1.getBlockY());
		int zmax = Math.min(info.boxCorner2.getBlockZ(), info.boxCorner1.getBlockZ());

		for(int x = xmin ; x <= xmax ; x++){
			List<Location> blocks = new ArrayList<>();
			for(int y = ymin ; y <= ymax ; y++){
				for(int z = zmin ; z <= zmax ; z++){
					blocks.add(info.boxCorner2.getWorld().getBlockAt(x, y, z).getLocation());
				}
			}
			turfBlocks.add(new Blocks(Team.SPECTATOR, blocks));
		}

		for(int i = 0 ; i < turfBlocks.size()/2 ; i++ ){
			for(Location b : turfBlocks.get(i).blocks){
				if(b.getBlock().getType().equals(Material.SPONGE)){
					b.getBlock().setType(Material.STAINED_CLAY);
					b.getBlock().setData(Team.BLUE.color);
					b.getBlock().setMetadata("floor", new FixedMetadataValue(plugin, Team.BLUE));
				}	
			}
			turfBlocks.get(i).team = Team.BLUE;
		}
		
		for(int i = turfBlocks.size()-1 ; i > turfBlocks.size()/2 ; i-- ){
			for(Location b : turfBlocks.get(i).blocks){
				if(b.getBlock().getType().equals(Material.SPONGE)){
					b.getBlock().setType(Material.STAINED_CLAY);
					b.getBlock().setData(Team.RED.color);
					b.getBlock().setMetadata("floor", new FixedMetadataValue(plugin, Team.RED));
				}	
			}
			turfBlocks.get(i).team = Team.RED;
		}
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
			for(int i = 1 ; i <= num ; i++ ){
				setTeam(team, n+i);
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
