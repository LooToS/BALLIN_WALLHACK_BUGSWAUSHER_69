package ktar.five.TurfWars.Game.Info;

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
}
