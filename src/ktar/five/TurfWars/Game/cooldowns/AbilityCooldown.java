package ktar.five.TurfWars.Game.cooldowns;

import java.util.HashMap;
import java.util.UUID;

public class AbilityCooldown {

	
	//system.getCurrentTimeMillis()
	
    public UUID player;
    public long seconds;
    public long systime;
    
    public HashMap<String, AbilityCooldown> cooldownMap = new HashMap<>();
    
    public AbilityCooldown(UUID player, long seconds, long systime) {
        this.player = player;
        this.seconds = seconds;
        this.systime = systime;
    }
    
    public AbilityCooldown(UUID player) {
        this.player = player;
    }
	
}
