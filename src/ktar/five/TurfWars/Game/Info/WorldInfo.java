package ktar.five.TurfWars.Game.Info;

import org.bukkit.Location;

public class WorldInfo {

    public final Location lobbySpawn;
    public final Location redSpawn;
    public final Location blueSpawn;
    public final Location boxCorner1;
    public final Location boxCorner2;

    public WorldInfo(Location lobbySpawn, Location redSpawn, Location blueSpawn, Location boxCorner1, Location boxCorner2) {
        this.lobbySpawn = lobbySpawn;
        this.redSpawn = redSpawn;
        this.blueSpawn = blueSpawn;
        this.boxCorner1 = boxCorner1;
        this.boxCorner2 = boxCorner2;
    }

}
