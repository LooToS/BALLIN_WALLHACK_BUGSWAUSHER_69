package ktar.five.TurfWars.Game.Player;

import org.bukkit.DyeColor;

public enum Team {
    SPECTATOR,
    RED(DyeColor.RED.getWoolData()),
    BLUE(DyeColor.BLUE.getWoolData());

    public byte color;

    private Team(byte color) {
        this.color = color;
    }

    private Team() {

    }
    
    public Team getOppositeTeam(){
    	if(this == Team.RED){
    		return Team.BLUE;
    	}else if(this == Team.BLUE){
    		return Team.RED;
    	}else{
    		return null;
    	}
    }
}
