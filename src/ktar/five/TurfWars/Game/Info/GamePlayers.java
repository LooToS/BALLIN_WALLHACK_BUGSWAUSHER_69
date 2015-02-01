package ktar.five.TurfWars.Game.Info;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Player;

import ktar.five.TurfWars.Main;

public class GamePlayers {

    public Map<UUID, TurfPlayer> spectators, redTeam, blueTeam;
    public final int maxPerTeam = 8;

    public GamePlayers() {
        this.redTeam = new HashMap<>();
        this.blueTeam = new HashMap<>();
        this.spectators = new HashMap<>();
    }

    public boolean isFull(Team team) {
        if (team == Team.BLUE) {
            return blueTeam.size() == maxPerTeam;
        } else {
            return redTeam.size() == maxPerTeam;
        }
    }
    
    public TurfPlayer getTurfPlayer(UUID uu){
    	return getAll().get(uu);
    }
    
    public Team getPlayerTeam(UUID uu){
    	return getPlayerTeam(getTurfPlayer(uu));
    }
    
    public boolean areOnSameTeam(UUID uu1, UUID uu2){
    	return areOnSameTeam(getTurfPlayer(uu1), getTurfPlayer(uu2));
    }
    
    public boolean areOnSameTeam(TurfPlayer one, TurfPlayer two){
    	return (getPlayerTeam(one).equals(getPlayerTeam(two)));
    }
    
    public Map<UUID, TurfPlayer> getTurfPlayers(Team team, boolean opposite){
    	if(team == Team.BLUE && !opposite){
    		return blueTeam;
    	}else if(team == Team.BLUE && opposite){
    		return redTeam;
    	}else if(team == Team.RED && opposite){
    		return blueTeam;
    	}else if(team == Team.RED && !opposite){
    		return redTeam;
    	}
		return null;
    }
    
    public Map<UUID, TurfPlayer> getAll() {
        Map<UUID, TurfPlayer> players = new HashMap<>();
        players.putAll(blueTeam);
        players.putAll(redTeam);
        return players;
    }

    public boolean gameFull() {
        return (isFull(Team.BLUE) && isFull(Team.RED));
    }

    public Team getTeamWithLess() {
        if (redTeam.size() < blueTeam.size()) {
            return Team.RED;
        } else if (redTeam.size() > blueTeam.size()) {
            return Team.BLUE;
        } else {
            return null;
        }
    }

    public boolean playerAlreadyInGame(TurfPlayer player) {
        return getPlayerTeam(player) != null;
    }

    public Team getPlayerTeam(TurfPlayer player) {
        if (spectators.containsValue(player)) {
            return Team.SPECTATOR;
        } else if (redTeam.containsValue(player)) {
            return Team.RED;
        } else if (blueTeam.containsValue(player)) {
            return Team.BLUE;
        } else {
            return null;
        }
    }

    public boolean putInLowerTeam(TurfPlayer player) {
        if (playerAlreadyInGame(player)) {
            return false;
        } else {
            putInTeam(getTeamWithLess(), player);
            return true;
        }
    }

    public byte getTeamByte(TurfPlayer player) {
        return this.getPlayerTeam(player).color;
    }

    public void putInTeam(Team team, TurfPlayer player) {
        if (team == Team.BLUE) {
            blueTeam.put(player.playerUUID, player);
        } else if (team == Team.RED) {
            redTeam.put(player.playerUUID, player);
        }else if(team == Team.SPECTATOR){
        	spectators.put(player.playerUUID, player);
        }
    }
    
    public boolean switchTeam(TurfPlayer player){
    	Team team = getPlayerTeam(player);
    	if(!isFull(team.getOppositeTeam())){
        	remove(player);
        	putInTeam(team.getOppositeTeam(), player);
        	return true;
    	}
    	return false;
    }
    
    public void removeFromTeam(TurfPlayer player, Team team){
    	if(team == Team.BLUE){
    		this.blueTeam.remove(player);
    	}else if(team == Team.RED){
    		this.redTeam.remove(player)
    	}else if(team == Team.SPECTATOR){
    		this.spectators.remove(player);
    	}
    }
    
    public void remove(TurfPlayer player){
    	this.removeFromTeam(player, getPlayerTeam(player));
    }
    
    public void updateDatabase(Main instance){//TODO
    	List<String> stmts = new ArrayList<>();
    	for(TurfPlayer p : this.getAll().values()){
    		stmts.add(p.getQuery());
    	}
    	
    	try {
			instance.sql.sendBatchStatement(stmts.toArray(new String[stmts.size()]));
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
    }

}
