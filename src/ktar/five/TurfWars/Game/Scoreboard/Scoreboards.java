package ktar.five.TurfWars.Game.Scoreboard;

import ktar.five.TurfWars.Game.Info.GameStatus;
import ktar.five.TurfWars.Game.Info.Phase;
import ktar.five.TurfWars.Game.Player.TurfPlayer;
import ktar.five.TurfWars.Lobby.Lobby;
import org.bukkit.Bukkit;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

public class Scoreboards {

    public static void getLobbyScoreboard(TurfPlayer p){
        Scoreboard sb = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective ob = sb.registerNewObjective(p.playerUUID.toString(), "dummy");
        ob.setDisplayName(Lobby.status == GameStatus.WAITING_FOR_PLAYERS
                ? "Waiting For Players" : "Game starts in " + (Lobby.lobbyCountdown - Lobby.seconds) + " seconds");
        ob.setDisplaySlot(DisplaySlot.SIDEBAR);
        ob.getScore(" ").setScore(15);
        ob.getScore("Players").setScore(14);
        ob.getScore(Lobby.players.getAll().size() + "/16").setScore(13);
        ob.getScore("  ").setScore(12);
        ob.getScore("Kit").setScore(11);
        ob.getScore(p.kit.name()).setScore(10);
        ob.getScore("   ").setScore(9);
        ob.getScore("Gems").setScore(8);
        ob.getScore(String.valueOf(p.getBalance())).setScore(7);
        p.getPlayer().setScoreboard(sb);
    }

    public static void getGameScoreboard(TurfPlayer p){
        Scoreboard sb = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective ob = sb.registerNewObjective(p.playerUUID.toString(), "dummy");
        ob.setDisplayName("SKYCRAFT");
        ob.setDisplaySlot(DisplaySlot.SIDEBAR);
        ob.getScore(" ").setScore(15);
        ob.getScore(Lobby.info.blue + " Blue").setScore(14);
        ob.getScore("  ").setScore(13);
        ob.getScore(Lobby.info.red + " Red").setScore(12);
        ob.getScore("   ").setScore(11);
        ob.getScore(Lobby.getGame().phase.getType() == Phase.PhaseType.BUILDING ? "Build Time" : "Combat Time").setScore(10);
        ob.getScore(String.valueOf(Lobby.getGame().phase.getSeconds() - Lobby.seconds)).setScore(9);
        p.getPlayer().setScoreboard(sb);
    }


}
