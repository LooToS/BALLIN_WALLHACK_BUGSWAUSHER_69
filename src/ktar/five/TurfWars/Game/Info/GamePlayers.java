package ktar.five.TurfWars.Game.Info;

import ktar.five.TurfWars.Game.Player.Team;
import ktar.five.TurfWars.Game.Player.TurfPlayer;
import ktar.five.TurfWars.Main;

import java.sql.SQLException;
import java.util.*;

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
    	return getAll().get(uu) != null ? getAll().get(uu) : new TurfPlayer(uu);
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
    	}else if(team == Team.BLUE){
    		return redTeam;
    	}else if(team == Team.RED && opposite){
    		return blueTeam;
    	}else if(team == Team.RED){
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

    public boolean playerInGame(UUID uu) {
        return this.getAll().containsKey(uu);
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
        if (playerInGame(player.playerUUID)) {
            return false;
        } else {
            putInTeam(getTeamWithLess(), player);
            return true;
        }
    }

    public byte getTeamByte(TurfPlayer player) {
        return getPlayerTeam(player).color;
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
    		this.blueTeam.remove(player.playerUUID);
    	}else if(team == Team.RED){
    		this.redTeam.remove(player.playerUUID);
    	}else if(team == Team.SPECTATOR){
    		this.spectators.remove(player.playerUUID);
    	}
    }

    public void remove(TurfPlayer player){
    	this.removeFromTeam(player, getPlayerTeam(player));
    }
    
    public void updateDatabase(){
    	List<String> stmts = new ArrayList<>();
    	for(TurfPlayer p : this.getAll().values()){
    		stmts.add(p.getQuery());
    	}
    	
    	try {
			Main.instance.sql.sendBatchStatement(stmts.toArray(new String[stmts.size()]));
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
    }

    public void clear(){
        this.blueTeam.clear();
        this.redTeam.clear();
        this.spectators.clear();
    }

}
