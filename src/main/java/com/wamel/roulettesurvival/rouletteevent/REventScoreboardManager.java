package com.wamel.roulettesurvival.rouletteevent;

import com.wamel.roulettesurvival.object.RoulettePlayer;
import com.wamel.roulettesurvival.object.RoulettePlayerFactory;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.ArrayList;
import java.util.HashMap;

public class REventScoreboardManager {

    private static final String DISPLAY_NAME = ChatColor.of("#8A2BE2") + "       §l룰렛 서바이벌       ";

    public static HashMap<RoulettePlayer, ArrayList<REventBase>> boardMap = new HashMap<>();

    public static void register(Player player, REventBase base) {
        ArrayList<REventBase> boardList;
        RoulettePlayer rPlayer = RoulettePlayerFactory.create(player);

        if(boardMap.get(rPlayer) == null)
            boardList = new ArrayList<>();
        else
            boardList = boardMap.get(rPlayer);

        boardList.add(base);
        boardMap.put(rPlayer, boardList);

        Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective obj = board.registerNewObjective("RouletteSurvival", "dummy", DISPLAY_NAME);
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);

        Integer max = boardList.size();

        obj.getScore("§f").setScore(max);

        int i = 1;
        for(REventBase rEvent : boardList) {
            obj.getScore("§f§l" + ChatColor.stripColor(rEvent.getName())).setScore(max - i);
            i++;
        }

        player.setScoreboard(board);
    }

    public static void refresh(Player player) {
        ArrayList<REventBase> tempList;
        RoulettePlayer rPlayer = RoulettePlayerFactory.create(player);

        if(boardMap.get(rPlayer) == null) {
            player.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
            return;
        }

        tempList = boardMap.get(rPlayer);

        ArrayList<REventBase> boardList = new ArrayList<>();

        for(REventBase base : tempList) {
            if(base.getIsEnabled(player) == true)
                boardList.add(base);
        }

        Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective obj = board.registerNewObjective("RouletteSurvival", "dummy", DISPLAY_NAME);
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);

        Integer max = boardList.size();

        obj.getScore("§f").setScore(max);

        int i = 1;
        for(REventBase base : boardList) {
            obj.getScore("§f§l" + ChatColor.stripColor(base.getName())).setScore(max - i);
            i++;
        }

        player.setScoreboard(board);
        boardMap.put(rPlayer, boardList);
    }

    public static void unRegister(Player player) {
        player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());

        RoulettePlayer rPlayer = RoulettePlayerFactory.create(player);

        if(boardMap.get(rPlayer) != null) {
            boardMap.remove(rPlayer);
            return;
        }
    }
}
